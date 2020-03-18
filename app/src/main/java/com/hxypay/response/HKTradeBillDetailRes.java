package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class HKTradeBillDetailRes extends PublicRes implements Serializable {

    private List<Info1> data;
    private String bankName;
    private String account;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {

        private String id;
        private String customerId;
        private String bind_id;
        private String type;
        private String amount;
        private String feeRate;
        private String formNo;
        private String payTime;
        private String downPayFee;
        private String downDrawFee;
        private String channelsId;
        private String state;
        private String message;
        private String mcc;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getBind_id() {
            return bind_id;
        }

        public void setBind_id(String bind_id) {
            this.bind_id = bind_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFeeRate() {
            return feeRate;
        }

        public void setFeeRate(String feeRate) {
            this.feeRate = feeRate;
        }

        public String getFormNo() {
            return formNo;
        }

        public void setFormNo(String formNo) {
            this.formNo = formNo;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getDownPayFee() {
            return downPayFee;
        }

        public void setDownPayFee(String downPayFee) {
            this.downPayFee = downPayFee;
        }

        public String getDownDrawFee() {
            return downDrawFee;
        }

        public void setDownDrawFee(String downDrawFee) {
            this.downDrawFee = downDrawFee;
        }

        public String getChannelsId() {
            return channelsId;
        }

        public void setChannelsId(String channelsId) {
            this.channelsId = channelsId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }
    }


}
