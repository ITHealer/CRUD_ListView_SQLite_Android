package com.example.khoaphamandroid;

import java.io.Serializable;

public class CongViec implements Serializable {
    private int IdCV;
    private String TenCV;

    public CongViec() {
    }

    public CongViec(int idCV, String tenCV) {
        IdCV = idCV;
        TenCV = tenCV;
    }

    public int getIdCV() {
        return IdCV;
    }

    public void setIdCV(int idCV) {
        IdCV = idCV;
    }

    public String getTenCV() {
        return TenCV;
    }

    public void setTenCV(String tenCV) {
        TenCV = tenCV;
    }
}
