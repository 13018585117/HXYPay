package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class BusCommodityDetailInfoRes extends PublicRes implements Serializable {


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private List<Data> data;
    public class Data implements Serializable {
        private String picture;
        private String name;
        private String integral;
        private String reminder;
        private String videoCourseUrl;
        private String exchageCourseUrl;
        private String exchageCourse;
        private String aboutUrl;
        private String aboutUrlName;
        private String exampMessage;
        private String bankId;
        private String exampPicture;
        private String fillType;
        private String settlementType;
        private String bankName;
        private String menberMaxExchangePrice;



        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }

        public String getVideoCourseUrl() {
            return videoCourseUrl;
        }

        public void setVideoCourseUrl(String videoCourseUrl) {
            this.videoCourseUrl = videoCourseUrl;
        }

        public String getExchageCourseUrl() {
            return exchageCourseUrl;
        }

        public void setExchageCourseUrl(String exchageCourseUrl) {
            this.exchageCourseUrl = exchageCourseUrl;
        }

        public String getExchageCourse() {
            return exchageCourse;
        }

        public void setExchageCourse(String exchageCourse) {
            this.exchageCourse = exchageCourse;
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

        public String getExampMessage() {
            return exampMessage;
        }

        public void setExampMessage(String exampMessage) {
            this.exampMessage = exampMessage;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getExampPicture() {
            return exampPicture;
        }

        public void setExampPicture(String exampPicture) {
            this.exampPicture = exampPicture;
        }

        public String getFillType() {
            return fillType;
        }

        public void setFillType(String fillType) {
            this.fillType = fillType;
        }

        public String getSettlementType() {
            return settlementType;
        }

        public void setSettlementType(String settlementType) {
            this.settlementType = settlementType;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }
}
