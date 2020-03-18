package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class HKTradeBillRes extends PublicRes implements Serializable {

    private List<Info1> info;
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

    public List<Info1> getInfo() {
        return info;
    }

    public void setInfo(List<Info1> info) {
        this.info = info;
    }

    public class Info1 implements Serializable {
        private String id;
        private String message;
        private String repaymoney;
        private String repayment;
        private String fee;
        private String created;
        private String name;
        private String state;
        private String beginTime;
        private String endTime;
        private String type ;
        private String Reserve;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getReserve() {
            return Reserve;
        }

        public void setReserve(String reserve) {
            Reserve = reserve;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRepaymoney() {
            return repaymoney;
        }

        public void setRepaymoney(String repaymoney) {
            this.repaymoney = repaymoney;
        }

        public String getRepayment() {
            return repayment;
        }

        public void setRepayment(String repayment) {
            this.repayment = repayment;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }


}
