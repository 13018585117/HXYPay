package com.hxypay.response;

import java.io.Serializable;

public class RQRes extends PublicRes implements Serializable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
