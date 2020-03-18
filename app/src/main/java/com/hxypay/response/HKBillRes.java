package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class HKBillRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String payTime;
        private String amount;
        private String state;//0:未还款  1：已还款  2：还款失败 3：处理中 4:已退款
        private String type;
        private String feeRate;
        private String bankName;
        private String account;
        private String bankLogo;

        public String getBankLogo() {
            return bankLogo;
        }

        public void setBankLogo(String bankLogo) {
            this.bankLogo = bankLogo;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFeeRate() {
            return feeRate;
        }

        public void setFeeRate(String feeRate) {
            this.feeRate = feeRate;
        }

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
    }


}
