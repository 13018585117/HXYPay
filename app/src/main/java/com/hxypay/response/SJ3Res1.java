package com.hxypay.response;

import java.io.Serializable;

public class SJ3Res1 extends PublicRes implements Serializable {

    public Order getOrder;

    public Order getGetOrder() {
        return getOrder;
    }

    public void setGetOrder(Order getOrder) {
        this.getOrder = getOrder;
    }

    public class Order {
        public String appid;
        public String partnerid;
        public String prepayid;
        public String noncestr;
        public String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
