package com.sonnevend.rssrerouter.dto;

/**
 * Created by s_lor_000 on 4/16/2016.
 */
public class LinkedFileDTO {

    private byte[] file;
    private String contentDisposition;

    public LinkedFileDTO(byte[] file, String contentDisposition) {
        this.file = file;
        this.contentDisposition = contentDisposition;
    }

    public byte[] getFile() {
        return file;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

}
