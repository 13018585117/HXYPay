package com.hxypay.response;

import java.io.Serializable;

public class MyBdRes extends PublicRes implements Serializable {
    private Info1 data;

    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1 implements Serializable{
        private String accountName;
        private String listNumber;
        private String insuranName;
        private String listPhone;
        private String expDate;
        private String effectTime;
        private String day;
        private String money;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getListNumber() {
            return listNumber;
        }

        public void setListNumber(String listNumber) {
            this.listNumber = listNumber;
        }

        public String getInsuranName() {
            return insuranName;
        }

        public void setInsuranName(String insuranName) {
            this.insuranName = insuranName;
        }

        public String getListPhone() {
            return listPhone;
        }

        public void setListPhone(String listPhone) {
            this.listPhone = listPhone;
        }

        public String getExpDate() {
            return expDate;
        }

        public void setExpDate(String expDate) {
            this.expDate = expDate;
        }

        public String getEffectTime() {
            return effectTime;
        }

        public void setEffectTime(String effectTime) {
            this.effectTime = effectTime;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }


}
