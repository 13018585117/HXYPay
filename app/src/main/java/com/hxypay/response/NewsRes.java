package com.hxypay.response;

import java.io.Serializable;
import java.util.List;

public class NewsRes extends PublicRes implements Serializable {

    private List<Info1> data;

    public List<Info1> getData() {
        return data;
    }

    public void setData(List<Info1> data) {
        this.data = data;
    }

    public class Info1 implements Serializable {
        private String title;
        private String images;
        private String created;
        private String text;
        private String author;
        private String content;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
