package com.example.myapplication.model;


public class DataPart {
    public String fileName;
    public byte[] content;
    public String type;

    public DataPart() {
    }

    public DataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public String getFileName() {
        return fileName;
    }

    public  byte[] getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

}
