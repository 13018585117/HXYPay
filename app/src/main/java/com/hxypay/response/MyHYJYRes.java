package com.hxypay.response;

import java.io.Serializable;

public class MyHYJYRes extends PublicRes implements Serializable {
    private Info1 data;
    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1{
        private String DailyTrading;
        private String MonthlyTransaction;
        public String getDailyTrading() {
            return DailyTrading;
        }

        public void setDailyTrading(String dailyTrading) {
            DailyTrading = dailyTrading;
        }

        public String getMonthlyTransaction() {
            return MonthlyTransaction;
        }

        public void setMonthlyTransaction(String monthlyTransaction) {
            MonthlyTransaction = monthlyTransaction;
        }

    }
}
