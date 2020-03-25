package com.example.dc_para;

public class News {
        private int pics;
        private String title;
        private String content;

    public News(){

    }

    public News(int pics, String title, String content){
        this.pics = pics;
        this.title= title;
        this.content = content;
    }

    public int getPics() {
        return pics;
    }

    public void setPics(int pics) {
        this.pics = pics;
    }

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

}
