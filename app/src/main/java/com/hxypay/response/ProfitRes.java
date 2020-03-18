package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class ProfitRes extends PublicRes implements Serializable {
    private String cash;
    private String notCash;
    private String deposit;
    private List<Info1> data;

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getNotCash() {
        return notCash;
    }

    public void setNotCash(String notCash) {
        this.notCash = notCash;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String money;
        private String days;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }
    }


}
