package com.hxypay.response;

import java.io.Serializable;

public class PopNewRes extends PublicRes implements Serializable {

    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String content;
        private String inter;
        private String state;
        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }


        public String getInter() {
            return inter;
        }

        public void setInter(String inter) {
            this.inter = inter;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
