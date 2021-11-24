package com.fs;

public interface FileSystem {

    FileClient openFile(String fileName, Mode mode);

    void write(String fileName, String data);

    String read(String fileName);

    byte[] read(String fileName, int n);

    String readString(String fileName, int n);

    void shutdown();
}
