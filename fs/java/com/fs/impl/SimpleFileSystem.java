package com.fs.impl;

import com.fs.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleFileSystem implements FileSystem {

    private int blockSize;
    private RandomAccessFile file;
    private BlockInfo fileSystemBlock = null;
    private Queue<BlockInfo> freeBlocks = new LinkedList<BlockInfo>();
    private FileMetaInfoCollector fileMetaInfoCollector = FileMetaInfoCollector.create();

    public SimpleFileSystem(String name, int fileSizeInBytes, int totalBlocks) {

        fileSizeInBytes = round(fileSizeInBytes);
        try {
            File metaFile = new File("/Users/sunil.kalva/FS/data/" + name);
            file = new RandomAccessFile(metaFile, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File System Can not be Created Now, Try later ", e);
        }

        blockSize = (fileSizeInBytes / totalBlocks);
        for (int i = 1; i <= totalBlocks; i++) {
            long pointer = blockSize * (i - 1);
            BlockInfo blockInfo = new BlockInfo(pointer, -1l, blockSize - 16l);
            if (i != totalBlocks) {
                freeBlocks.add(blockInfo);
            } else {
                fileSystemBlock = blockInfo;
            }
            moveToStartOfBlock(blockInfo);
            writeBlockMetaData(blockInfo);
        }
    }

    @Override
    public synchronized FileClient openFile(String fileName, Mode mode) {

        FileInfo metaInfo = FileMetaInfoCollector.create().getFileInfo(fileName);
        if (Mode.READ.equals(mode) && null == metaInfo) {
            throw new RuntimeException("File not Found <" + fileName + ">");
        }

        if (Mode.WRITE.equals(mode)) {

            BlockInfo freeBlock = getFreeBlock();
            if (null == freeBlock) {
                throw new RuntimeException("No space on filesystem to create, try later.....!");
            }
            FileInfo fileInfo = new FileInfo(fileName, freeBlock);
            fileMetaInfoCollector.addFileMetaInfo(fileInfo);
            flushDataToBlock(fileInfo.toString(), fileSystemBlock);
        }
        return new SimpleFileClient(this, fileName);
    }

    @Override
    public synchronized void write(String fileName, String data) {
        try {
            FileInfo fileInfo = fileMetaInfoCollector.getFileInfo(fileName);
            if (null == fileInfo) {
                throw new RuntimeException("File not Found <" + fileName + ">");
            }
            fileInfo.setBlockInfo(updateNextBlock(data, fileInfo.getBlockInfo()));
            flushDataToBlock(data, fileInfo.getBlockInfo());
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    @Override
    public String read(String fileName) {

        try {
            BlockInfo blockInfo = moveToStartOfFile(fileName);
            Long nextBlockAddress = blockInfo.getNextBlockAddress();
            StringBuilder builder = new StringBuilder();

            while (true) {
                file.seek(blockInfo.getMyBlockId() + 16);
                int sizeOfCurrentBlock = (int) (blockSize - blockInfo.getRemSizeOfCurrentBlock() - 16);
                byte[] data = new byte[sizeOfCurrentBlock];
                file.read(data);
                builder.append(new String(data));
                if (-1 == nextBlockAddress) {
                    break;
                }
                file.seek(nextBlockAddress);
                blockInfo = readBlockMetaData();
                nextBlockAddress = blockInfo.getNextBlockAddress();
            }
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    @Override
    public synchronized byte[] read(String fileName, int n) {

        try {
            BlockInfo blockInfo = moveToStartOfFile(fileName);
            Long nextBlockAddress = blockInfo.getNextBlockAddress();
            StringBuilder builder = new StringBuilder();

            int remSize = n;
            while (true) {
                file.seek(blockInfo.getMyBlockId() + 16);
                int sizeOfCurrentBlock = (int) (blockSize - blockInfo.getRemSizeOfCurrentBlock() - 16);
                if (sizeOfCurrentBlock > remSize) {
                    sizeOfCurrentBlock = remSize;
                    remSize = 0;
                } else {
                    remSize -= sizeOfCurrentBlock;
                }
                byte[] data = new byte[sizeOfCurrentBlock];
                file.read(data);
                builder.append(new String(data));
                if (-1 == nextBlockAddress || 0 >= remSize) {
                    break;
                }
                file.seek(nextBlockAddress);
                blockInfo = readBlockMetaData();
                nextBlockAddress = blockInfo.getNextBlockAddress();
            }
            return builder.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    @Override
    public synchronized String readString(String fileName, int n) {
        FileInfo fileInfo = fileMetaInfoCollector.getFileInfo(fileName);
        moveToDataOfBlock(fileInfo.getBlockInfo());
        try {
            byte[] b = new byte[n];
            file.read(b);
            return new String(b);
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private void flushDataToBlock(String data, BlockInfo info) {
        try {

            moveToEndOfBlock(info);
            file.writeBytes(data);

            moveToStartOfBlock(info);
            BlockInfo blockInfo = readBlockMetaData();
            if (null != blockInfo) {
                info.setRemSizeOfCurrentBlock(blockInfo.getRemSizeOfCurrentBlock() - data.length());
            } else {
                info.setNextBlockAddress(0l);
                info.setRemSizeOfCurrentBlock((long) data.getBytes().length);
            }
            file.seek(info.getMyBlockId());
            writeBlockMetaData(info);
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private BlockInfo moveToStartOfFile(String fileName) {
        try {
            moveToDataOfBlock(fileSystemBlock);
            String fileDetails = file.readLine();
            String[] split = fileDetails.split(";");
            for (String s : split) {
                String[] name = s.split(",");
                String fileNameJson = name[0].substring(1, name.length);
                if (fileName.equals(fileNameJson)) {
                    String fileLocation = name[1].substring(0, name[1].length() - 1);
                    long loc = Long.parseLong(fileLocation);
                    file.seek(loc);
                    BlockInfo blockInfo = new BlockInfo(loc, file.readLong(), file.readLong());
                    file.seek(loc);
                    return blockInfo;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("File System Error", e);
        }
        return null;
    }

    private void moveToStartOfBlock(BlockInfo info) {
        try {
            file.seek(info.getMyBlockId());
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private void moveToDataOfBlock(BlockInfo info) {
        try {
            file.seek(info.getMyBlockId() + 16);
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private void moveToEndOfBlock(BlockInfo info) {
        try {
            file.seek(info.getMyBlockId() + (blockSize - info.getRemSizeOfCurrentBlock()));
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private void moveToEndOfFile(BlockInfo info) {
        try {
            file.seek(info.getMyBlockId() + (blockSize - info.getRemSizeOfCurrentBlock()));
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private BlockInfo updateNextBlock(String data, BlockInfo currentBlock) {

        BlockInfo freeBlock;
        moveToStartOfBlock(currentBlock);
        Long sizeOfCurrentBlock = readBlockMetaData().getRemSizeOfCurrentBlock() - data.length();
        if (0 > sizeOfCurrentBlock) {
            freeBlock = getFreeBlock();
            if (null == freeBlock) {
                throw new RuntimeException("No space on filesystem to create, try later.....!");
            }
        } else {
            return currentBlock;
        }

        currentBlock.setNextBlockAddress(freeBlock.getMyBlockId());
        moveToStartOfBlock(currentBlock);
        try {
            file.writeLong(freeBlock.getMyBlockId());
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
        moveToStartOfBlock(freeBlock);
        return freeBlock;
    }

    private BlockInfo readBlockMetaData() {
        try {
            long filePointer = file.getFilePointer();
            BlockInfo blockInfo = new BlockInfo(filePointer, file.readLong(), file.readLong());
            file.seek(filePointer);
            return blockInfo;
        } catch (Exception e) {
            return null;
        }
    }

    private void writeBlockMetaData(BlockInfo info) {
        try {
            file.writeLong(info.getNextBlockAddress());
            file.writeLong(info.getRemSizeOfCurrentBlock());
        } catch (IOException e) {
            throw new RuntimeException("File System Error", e);
        }
    }

    private BlockInfo getFreeBlock() {
        try {
            return freeBlocks.remove();
        } catch (Exception e) {
            throw new RuntimeException("No space on filesystem to create, try later.....!");
        }
    }

    private int round(int num) {
        if (num % 100 == 0)
            return num;
        else if (num % 100 < 50)
            return 100;
        else
            num = num + (100 - num % 100);
        return num;
    }
}
