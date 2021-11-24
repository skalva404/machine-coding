package com.fs.impl;

import com.fs.FileClient;
import com.fs.FileSystem;

public class SimpleFileClient implements FileClient {
    private FileSystem fs;
    private String fileName;

    protected SimpleFileClient(FileSystem fs, String fileName) {
        this.fs = fs;
        this.fileName = fileName;
    }

    @Override
    public byte[] read(int n) {
        return fs.read(fileName, n);
    }

    @Override
    public String readString(int n) {
        return fs.readString(fileName, n);
    }

    @Override
    public void write(String data) {
        fs.write(fileName, data);
    }

    @Override
    public String readFile() {
        return fs.read(fileName);
    }
}
