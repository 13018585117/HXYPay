package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class BannerRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable{
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }


}
