package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class NewHeadRes extends PublicRes implements Serializable {
    public List<Inif> getData() {
        return data;
    }

    public void setData(List<Inif> data) {
        this.data = data;
    }

    private List<Inif> data;

    public class Inif{
        private String icon;
        private String id;
        private String name;
        private String remarks;
        private String sign;
        private String src;
        private String time;
        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }
}
