package com.hxypay.response;

import java.io.Serializable;

public class HeadPicRes extends PublicRes implements Serializable {

    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
