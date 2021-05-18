package com.example.tcpsocketclient.Entities;

public class Respuesta {
    private String data;
    private int msg_code;
    private String msg_detail;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(int msg_code) {
        this.msg_code = msg_code;
    }

    public String getMsg_detail() {
        return msg_detail;
    }

    public void setMsg_detail(String msg_detail) {
        this.msg_detail = msg_detail;
    }
}
