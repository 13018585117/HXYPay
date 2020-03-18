package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class ShareRes extends PublicRes implements Serializable {

    private List<Info1> data;
    private List<Info2> img;
    private Share share;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Info2> getImg() {
        return img;
    }

    public void setImg(List<Info2> img) {
        this.img = img;
    }


    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String id;
        private String src;
        private String img_id;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getImg_id() {
            return img_id;
        }

        public void setImg_id(String img_id) {
            this.img_id = img_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }


    public class Info2 implements Serializable {
        private String src;
        public String getSrc() {
            return src;
        }
        public void setSrc(String src) {
            this.src = src;
        }
    }


    public class Share implements Serializable {
        private String title;
        private String content;
        private String shareUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }

}
