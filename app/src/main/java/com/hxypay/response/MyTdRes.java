package com.hxypay.response;

import java.io.Serializable;

public class MyTdRes extends PublicRes implements Serializable {
    private Info1 data;
    public Info1 getData() {
        return data;
    }

    public void setData(Info1 data) {
        this.data = data;
    }

    public class Info1{
        private int teamNumber;
        private int teamVIPNumber;
        private int teamSVIPNumber;
        private String upgradeMoney;
        private String profitMoney;
        private String cardMoney;
        private String loanMoney;
        private String testCardMoney;
        private String teamIncome;
        public String getTeamIncome() {
            return teamIncome;
        }

        public void setTeamIncome(String teamIncome) {
            this.teamIncome = teamIncome;
        }



        public int getTeamNumber() {
            return teamNumber;
        }

        public void setTeamNumber(int teamNumber) {
            this.teamNumber = teamNumber;
        }

        public int getTeamVIPNumber() {
            return teamVIPNumber;
        }

        public void setTeamVIPNumber(int teamVIPNumber) {
            this.teamVIPNumber = teamVIPNumber;
        }

        public int getTeamSVIPNumber() {
            return teamSVIPNumber;
        }

        public void setTeamSVIPNumber(int teamSVIPNumber) {
            this.teamSVIPNumber = teamSVIPNumber;
        }

        public String getUpgradeMoney() {
            return upgradeMoney;
        }

        public void setUpgradeMoney(String upgradeMoney) {
            this.upgradeMoney = upgradeMoney;
        }

        public String getProfitMoney() {
            return profitMoney;
        }

        public void setProfitMoney(String profitMoney) {
            this.profitMoney = profitMoney;
        }

        public String getCardMoney() {
            return cardMoney;
        }

        public void setCardMoney(String cardMoney) {
            this.cardMoney = cardMoney;
        }

        public String getLoanMoney() {
            return loanMoney;
        }

        public void setLoanMoney(String loanMoney) {
            this.loanMoney = loanMoney;
        }

        public String getTestCardMoney() {
            return testCardMoney;
        }

        public void setTestCardMoney(String testCardMoney) {
            this.testCardMoney = testCardMoney;
        }
    }
}
