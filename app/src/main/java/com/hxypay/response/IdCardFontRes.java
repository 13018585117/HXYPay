package com.hxypay.response;

import java.io.Serializable;

public class IdCardFontRes extends PublicRes implements Serializable {
    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable{
        private String images;
        private String username;
        private String idCard;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }
    }


}
