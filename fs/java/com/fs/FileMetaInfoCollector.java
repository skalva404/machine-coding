package com.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileMetaInfoCollector {

    public Map<String, FileInfo> metaInfoList = new HashMap<String, FileInfo>();

    private FileMetaInfoCollector() {
    }

    public static FileMetaInfoCollector create() {
        return new FileMetaInfoCollector();
    }

    public void addFileMetaInfo(FileInfo metaInfo) {
        metaInfoList.put(metaInfo.getFileName(), metaInfo);
    }

    public FileInfo getFileInfo(String fileName) {
        return metaInfoList.get(fileName);
    }

    public List<FileInfo> getFileMetaInfos() {
        return new ArrayList<FileInfo>(metaInfoList.values());
    }
}
