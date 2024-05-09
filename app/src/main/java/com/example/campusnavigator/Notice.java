package com.example.campusnavigator;

public class Notice {
    private String heading;
    private String content;
    private String fileUrl;
    private String date;


    public Notice() {
    }


    public Notice(String heading, String content, String fileUrl) {
        this.heading = heading;
        this.content = content;
        this.fileUrl = fileUrl;
    }


    public Notice(String heading, String content, String fileUrl, String date) {
        this.heading = heading;
        this.content = content;
        this.fileUrl = fileUrl;
        this.date = date;
    }


    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
