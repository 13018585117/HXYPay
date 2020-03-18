package com.hxypay.response;

import java.io.Serializable;

public class MemberNewRes extends PublicRes implements Serializable {

    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
