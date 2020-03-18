package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class FriendsRes extends PublicRes implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        public Data.SVIP getSVIP() {
            return SVIP;
        }

        public void setSVIP(Data.SVIP SVIP) {
            this.SVIP = SVIP;
        }

        public Data.User getUser() {
            return User;
        }

        public void setUser(Data.User user) {
            User = user;
        }

        public Data.VIP getVIP() {
            return VIP;
        }

        public void setVIP(Data.VIP VIP) {
            this.VIP = VIP;
        }

        private SVIP SVIP;
        private User User;
        private VIP VIP;

        public class SVIP{
            private List<Info1> direct;
            private List<Info1> indirect;
            public List<Info1> getDirect() {
                return direct;
            }

            public void setDirect(List<Info1> direct) {
                this.direct = direct;
            }

            public List<Info1> getIndirect() {
                return indirect;
            }

            public void setIndirect(List<Info1> indirect) {
                this.indirect = indirect;
            }
        }
        public class User{
            private List<Info1> direct;
            private List<Info1> indirect;
            public List<Info1> getDirect() {
                return direct;
            }

            public void setDirect(List<Info1> direct) {
                this.direct = direct;
            }

            public List<Info1> getIndirect() {
                return indirect;
            }

            public void setIndirect(List<Info1> indirect) {
                this.indirect = indirect;
            }
        }
        public class VIP{
            private List<Info1> direct;
            private List<Info1> indirect;
            public List<Info1> getDirect() {
                return direct;
            }

            public void setDirect(List<Info1> direct) {
                this.direct = direct;
            }

            public List<Info1> getIndirect() {
                return indirect;
            }

            public void setIndirect(List<Info1> indirect) {
                this.indirect = indirect;
            }
        }
    }


    public class Info1 {
        private String phoneNum;
        private String created;
        private String isReal;
        private String type_id;
        private String id;
        private String loginId;

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getIsReal() {
            return isReal;
        }

        public void setIsReal(String isReal) {
            this.isReal = isReal;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }

}
