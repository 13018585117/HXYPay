package com.hxypay.response;

import java.io.Serializable;

public class SJ1Res extends PublicRes implements Serializable {

    private Info data;

    public Info getData() {
        return data;
    }

    public void setData(Info data) {
        this.data = data;
    }

    public class Info {
        private String level;
        private String pay;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }
    }
}
