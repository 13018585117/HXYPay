package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class BusCardListInfoRes extends PublicRes implements Serializable {
    private List<Data> data;
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private String id;
        private String maxExchangePrice;
        private String menberMaxExchangePrice;
        private String name;
        private String picture;
        private String settlemenTypeCount;
        private String sort;
        private String startExchangeInteger;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMaxExchangePrice() {
            return maxExchangePrice;
        }

        public void setMaxExchangePrice(String maxExchangePrice) {
            this.maxExchangePrice = maxExchangePrice;
        }

        public String getMenberMaxExchangePrice() {
            return menberMaxExchangePrice;
        }

        public void setMenberMaxExchangePrice(String menberMaxExchangePrice) {
            this.menberMaxExchangePrice = menberMaxExchangePrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSettlemenTypeCount() {
            return settlemenTypeCount;
        }

        public void setSettlemenTypeCount(String settlemenTypeCount) {
            this.settlemenTypeCount = settlemenTypeCount;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStartExchangeInteger() {
            return startExchangeInteger;
        }

        public void setStartExchangeInteger(String startExchangeInteger) {
            this.startExchangeInteger = startExchangeInteger;
        }
    }
}
