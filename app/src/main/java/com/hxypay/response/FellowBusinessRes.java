package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class FellowBusinessRes extends PublicRes implements Serializable {
    private Inif data;
    public Inif getData() {
        return data;
    }

    public void setData(Inif data) {
        this.data = data;
    }

    public class Inif{
        private List<Friend> friend;
        private List<MonthTotal> monthTotal;
        public List<Friend> getFriend() {
            return friend;
        }

        public void setFriend(List<Friend> friend) {
            this.friend = friend;
        }

        public List<MonthTotal> getMonthTotal() {
            return monthTotal;
        }

        public void setMonthTotal(List<MonthTotal> monthTotal) {
            this.monthTotal = monthTotal;
        }


        public class Friend implements Serializable{
            private List<Detail> detail;
            private String name;
            private String total;
            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
            public class Detail implements Serializable{
                private String date;
                private String money;
                private String type;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }
            }
        }

        public class MonthTotal{
            private String date;
            private String money;
            private List<Detail> detail;
            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public class Detail implements Serializable{
                private String date;
                private String money;
                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }
            }
        }
    }
}
