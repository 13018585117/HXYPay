package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class MccRes extends PublicRes implements Serializable {


    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String mcc;
        private String mccName;

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }

        public String getMccName() {
            return mccName;
        }

        public void setMccName(String mccName) {
            this.mccName = mccName;
        }
    }


}
