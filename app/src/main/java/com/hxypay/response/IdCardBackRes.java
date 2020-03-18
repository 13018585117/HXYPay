package com.hxypay.response;

import java.io.Serializable;

public class IdCardBackRes extends PublicRes implements Serializable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
