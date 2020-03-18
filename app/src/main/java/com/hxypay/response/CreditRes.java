package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class CreditRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String id;
        private String account;
        private String accountName;
        private String bankName;
        private String bankLogo;
        private String type;
        private String repaymentDay;
        private String billDay;
        private String bankBack;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankLogo() {
            return bankLogo;
        }

        public void setBankLogo(String bankLogo) {
            this.bankLogo = bankLogo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getBankBack() {
            return bankBack;
        }

        public void setBankBack(String bankBack) {
            this.bankBack = bankBack;
        }
    }


}
