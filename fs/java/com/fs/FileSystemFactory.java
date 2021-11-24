package com.fs;

import com.fs.impl.SimpleFileSystem;

import java.util.HashMap;
import java.util.Map;

public class FileSystemFactory {

    private static FileSystemFactory ourInstance = new FileSystemFactory();
    private Map<String, SimpleFileSystem> fsMap = new HashMap<String, SimpleFileSystem>();

    private FileSystemFactory() {
    }

    public static FileSystemFactory getInstance() {
        return ourInstance;
    }

    public synchronized FileSystem createFileSystem(String name, int bytes, int totalBlocks) {
        SimpleFileSystem fs = fsMap.get(name);
        if (null == fs) {
            fs = new SimpleFileSystem(name, bytes, totalBlocks);
            fsMap.put(name, fs);
        }
        return fs;
    }

}
