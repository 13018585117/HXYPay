package com.hxypay.response;

import java.io.Serializable;

public class AboutUsRes implements Serializable {
    private String content;
    private String title;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
