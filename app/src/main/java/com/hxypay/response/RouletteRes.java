package com.hxypay.response;

import java.io.Serializable;

public class RouletteRes extends PublicRes implements Serializable  {
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data;

    public class Data{
        private String handlingFee;
        private String subordinate;
        private String superior;
        private String workingFund;

        public String getHandlingFee() {
            return handlingFee;
        }

        public void setHandlingFee(String handlingFee) {
            this.handlingFee = handlingFee;
        }

        public String getSubordinate() {
            return subordinate;
        }

        public void setSubordinate(String subordinate) {
            this.subordinate = subordinate;
        }

        public String getSuperior() {
            return superior;
        }

        public void setSuperior(String superior) {
            this.superior = superior;
        }

        public String getWorkingFund() {
            return workingFund;
        }

        public void setWorkingFund(String workingFund) {
            this.workingFund = workingFund;
        }

    }
}
