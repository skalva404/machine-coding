package com.fs;

public class FileInfo {
    private String fileName;
    private BlockInfo blockInfo;

    public FileInfo(String fileName, BlockInfo blockInfo) {
        this.fileName = fileName;
        this.blockInfo = blockInfo;
    }

    public FileInfo(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public BlockInfo getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
    }

    @Override
    public String toString() {
        return "{" + fileName + "," + blockInfo.getMyBlockId() + "};";
    }
}
