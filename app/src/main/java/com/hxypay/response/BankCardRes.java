package com.hxypay.response;

import java.io.Serializable;

public class BankCardRes extends PublicRes implements Serializable {
    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String images;
        private String bankCard;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }
    }


}
