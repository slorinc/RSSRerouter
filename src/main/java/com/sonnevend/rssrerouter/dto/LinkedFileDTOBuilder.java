package com.sonnevend.rssrerouter.dto;

public class LinkedFileDTOBuilder {

    private byte[] file;
    private String fileName;

    public LinkedFileDTOBuilder setFile(byte[] file) {
        this.file = file;
        return this;
    }

    public LinkedFileDTOBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public LinkedFileDTO createTorrentFile() {
        return new LinkedFileDTO(file, fileName);
    }
}