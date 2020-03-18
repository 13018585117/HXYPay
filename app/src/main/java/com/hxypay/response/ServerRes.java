package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class ServerRes extends PublicRes implements Serializable {

    private List<Info> data;

    public List<Info> getData() {
        return data;
    }

    public void setData(List<Info> data) {
        this.data = data;
    }

    public class Info {
        private String id;
        private String type;
        private String information;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }
    }
}
