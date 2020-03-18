package com.hxypay.response;

import java.io.Serializable;

public class FastPayRes extends PublicRes implements Serializable {

    private Info data;

    public Info getData() {
        return data;
    }

    public void setData(Info data) {
        this.data = data;
    }

    public class Info implements Serializable {
        private String transcode;
        private String merchno;
        private String dsorderid;
        private String sign;
        private String ordersn;
        private String returncode;
        private String errtext;
        private String orderid;
        private String transtype;
        private String pay_info;
        private String pay_code;


        public String getTranscode() {
            return transcode;
        }

        public void setTranscode(String transcode) {
            this.transcode = transcode;
        }

        public String getMerchno() {
            return merchno;
        }

        public void setMerchno(String merchno) {
            this.merchno = merchno;
        }

        public String getDsorderid() {
            return dsorderid;
        }

        public void setDsorderid(String dsorderid) {
            this.dsorderid = dsorderid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getOrdersn() {
            return ordersn;
        }

        public void setOrdersn(String ordersn) {
            this.ordersn = ordersn;
        }

        public String getReturncode() {
            return returncode;
        }

        public void setReturncode(String returncode) {
            this.returncode = returncode;
        }

        public String getErrtext() {
            return errtext;
        }

        public void setErrtext(String errtext) {
            this.errtext = errtext;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getTranstype() {
            return transtype;
        }

        public void setTranstype(String transtype) {
            this.transtype = transtype;
        }

        public String getPay_info() {
            return pay_info;
        }

        public void setPay_info(String pay_info) {
            this.pay_info = pay_info;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }
    }




}
