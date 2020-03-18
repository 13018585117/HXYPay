package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class BusCommodityListInfoRes extends PublicRes implements Serializable {
    private List<Data> data;
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private String aboutUrl;
        private String aboutUrlName;
        private String agentPrice;
        private String bankId;
        private String bankName;
        private String createTime;
        private String cusSerId;
        private String exampMessage;
        private String exampPicture;
        private String exchageCourse;
        private String exchageCourseUrl;
        private String fillType;
        private String id;
        private String integral;
        private String isCashReturn;
        private String isShow;
        private String memberPrice;
        private String name;
        private String physicalModel;
        private String picture;
        private String reminder;
        private String settlementPrice;
        private String settlementTime;
        private String settlementType;
        private String sort;
        private String status;
        private String unifiedName;
        private String videoCourseUrl;
        private String menberMaxExchangePrice;

        public String getMenberMaxExchangePrice() {
            return menberMaxExchangePrice;
        }

        public void setMenberMaxExchangePrice(String menberMaxExchangePrice) {
            this.menberMaxExchangePrice = menberMaxExchangePrice;
        }


        public String getAboutUrl() {
            return aboutUrl;
        }

        public void setAboutUrl(String aboutUrl) {
            this.aboutUrl = aboutUrl;
        }

        public String getAboutUrlName() {
            return aboutUrlName;
        }

        public void setAboutUrlName(String aboutUrlName) {
            this.aboutUrlName = aboutUrlName;
        }

        public String getAgentPrice() {
            return agentPrice;
        }

        public void setAgentPrice(String agentPrice) {
            this.agentPrice = agentPrice;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCusSerId() {
            return cusSerId;
        }

        public void setCusSerId(String cusSerId) {
            this.cusSerId = cusSerId;
        }

        public String getExampMessage() {
            return exampMessage;
        }

        public void setExampMessage(String exampMessage) {
            this.exampMessage = exampMessage;
        }

        public String getExampPicture() {
            return exampPicture;
        }

        public void setExampPicture(String exampPicture) {
            this.exampPicture = exampPicture;
        }

        public String getExchageCourse() {
            return exchageCourse;
        }

        public void setExchageCourse(String exchageCourse) {
            this.exchageCourse = exchageCourse;
        }

        public String getExchageCourseUrl() {
            return exchageCourseUrl;
        }

        public void setExchageCourseUrl(String exchageCourseUrl) {
            this.exchageCourseUrl = exchageCourseUrl;
        }

        public String getFillType() {
            return fillType;
        }

        public void setFillType(String fillType) {
            this.fillType = fillType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getIsCashReturn() {
            return isCashReturn;
        }

        public void setIsCashReturn(String isCashReturn) {
            this.isCashReturn = isCashReturn;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public String getMemberPrice() {
            return memberPrice;
        }

        public void setMemberPrice(String memberPrice) {
            this.memberPrice = memberPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhysicalModel() {
            return physicalModel;
        }

        public void setPhysicalModel(String physicalModel) {
            this.physicalModel = physicalModel;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }

        public String getSettlementPrice() {
            return settlementPrice;
        }

        public void setSettlementPrice(String settlementPrice) {
            this.settlementPrice = settlementPrice;
        }

        public String getSettlementTime() {
            return settlementTime;
        }

        public void setSettlementTime(String settlementTime) {
            this.settlementTime = settlementTime;
        }

        public String getSettlementType() {
            return settlementType;
        }

        public void setSettlementType(String settlementType) {
            this.settlementType = settlementType;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUnifiedName() {
            return unifiedName;
        }

        public void setUnifiedName(String unifiedName) {
            this.unifiedName = unifiedName;
        }

        public String getVideoCourseUrl() {
            return videoCourseUrl;
        }

        public void setVideoCourseUrl(String videoCourseUrl) {
            this.videoCourseUrl = videoCourseUrl;
        }
    }
}
