package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class HomePlainRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String id;
        private String accountName;
        private String account;
        private String repaymentDay;
        private String billDay;
        private String beginTime;
        private String repayment;
        private String repayment_number;
        private String bankLogo;
        private String percentage;
        private String bankName;
        private String bankBack;
        private String type;
        private String alert;
        private String pay_after;
        private String mid;
       /* private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }*/



        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getPay_after() {
            return pay_after;
        }

        public void setPay_after(String pay_after) {
            this.pay_after = pay_after;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRepaymentDay() {
            return repaymentDay;
        }

        public void setRepaymentDay(String repaymentDay) {
            this.repaymentDay = repaymentDay;
        }

        public String getBillDay() {
            return billDay;
        }

        public void setBillDay(String billDay) {
            this.billDay = billDay;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getRepayment() {
            return repayment;
        }

        public void setRepayment(String repayment) {
            this.repayment = repayment;
        }

        public String getRepayment_number() {
            return repayment_number;
        }

        public void setRepayment_number(String repayment_number) {
            this.repayment_number = repayment_number;
        }

        public String getBankLogo() {
            return bankLogo;
        }

        public void setBankLogo(String bankLogo) {
            this.bankLogo = bankLogo;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankBack() {
            return bankBack;
        }

        public void setBankBack(String bankBack) {
            this.bankBack = bankBack;
        }
    }


}
