package com.example.mehed.pikanotesapp;

public class object {
    String content,title;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public object(String content, String title, String time) {

        this.content = content;
        this.title = title;
        this.time = time;
    }

    public object() {
    }

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
