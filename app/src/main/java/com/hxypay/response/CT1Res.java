package com.hxypay.response;

import java.io.Serializable;

public class CT1Res extends PublicRes implements Serializable {
    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String money;
        private String order_id;
        private String alipay;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }
    }


}
