package com.fs;

public interface FileClient {

    byte[] read(int n);

    String readString(int n);

    void write(String data);

    String readFile();
}
