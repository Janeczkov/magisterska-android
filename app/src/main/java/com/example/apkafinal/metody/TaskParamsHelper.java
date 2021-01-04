package com.example.apkafinal.metody;

public class TaskParamsHelper {
    String ip;
    byte[] content;
    String filename;
    String typ;
    String category;
    String username;

/*
    TaskParamsHelper(String ip, byte[] content, String filename, String typ, String category, String username){
        this.ip = ip;
        this.content = content;
        this.filename = filename;
        this.typ = typ;
        this.category = category;
        this.username = username;
    }

 */
public TaskParamsHelper(String ip, byte[] content, String filename, String username){
    this.ip = ip;
    this.content = content;
    this.filename = filename;
    this.username = username;
}
}
