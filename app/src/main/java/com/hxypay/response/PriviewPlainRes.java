package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class PriviewPlainRes extends PublicRes implements Serializable {
    private List<Info1> index_data;
    private String allpayfee;  //总手续费；
    private String deposit_xin;
    private String parentOrder;
    private String downDraw;  //结算费
    private String payfee;   //手续费；

    public String getDownDraw() {
        return downDraw;
    }

    public void setDownDraw(String downDraw) {
        this.downDraw = downDraw;
    }

    public String getPayfee() {
        return payfee;
    }

    public void setPayfee(String payfee) {
        this.payfee = payfee;
    }

    public List<Info1> getIndex_data() {
        return index_data;
    }

    public void setIndex_data(List<Info1> index_data) {
        this.index_data = index_data;
    }

    public String getAllpayfee() {
        return allpayfee;
    }

    public void setAllpayfee(String allpayfee) {
        this.allpayfee = allpayfee;
    }

    public String getDeposit_xin() {
        return deposit_xin;
    }

    public void setDeposit_xin(String deposit_xin) {
        this.deposit_xin = deposit_xin;
    }



    public String getParentOrder() {
        return parentOrder;
    }

    public void setParentOrder(String parentOrder) {
        this.parentOrder = parentOrder;
    }

    public class Info1 implements Serializable {
        private String amount;
        private String customerId;
        private String type;
        private String mcc;
        private String mccName;
        private String payTime;
        private String formNo;
        private String created;
        private String downPayFee;
        private String downDrawFee;
        private String feeRate;
        private String money;

        public String getMccName() {
            return mccName;
        }

        public void setMccName(String mccName) {
            this.mccName = mccName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getFormNo() {
            return formNo;
        }

        public void setFormNo(String formNo) {
            this.formNo = formNo;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
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

        public String getFeeRate() {
            return feeRate;
        }

        public void setFeeRate(String feeRate) {
            this.feeRate = feeRate;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }


}
