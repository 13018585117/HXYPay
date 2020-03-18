package com.hxypay.bean;

public class WXChactBean {
    public String code;
    public Order getOrder;
    public class Order{
        public String appid;
        public String noncestr;
        public String sign;
        public String partnerid;
        public String prepayid;
        public String timestamp;
    }
}
