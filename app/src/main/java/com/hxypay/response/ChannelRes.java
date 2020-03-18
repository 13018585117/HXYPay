package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class ChannelRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String type;
        private String pay_classname;
        private String content;
        private String state;// 1大额 2小额
        public String array;
        /*private List<Array> array;
        public class Array{

        }*/

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPay_classname() {
            return pay_classname;
        }

        public void setPay_classname(String pay_classname) {
            this.pay_classname = pay_classname;
        }
    }


}
