package com.example.campusnavigator;

public class NoticeItem {
    private String heading;
    private String date;
    private String fileUrl; // Add this field
    private String content; // Add this field

    // Default constructor required by Firebase
    public NoticeItem() {
        // Default constructor required by Firebase
    }

    // Constructor
    public NoticeItem(String heading, String date, String fileUrl, String content) {
        this.heading = heading;
        this.date = date;
        this.fileUrl = fileUrl;
        this.content = content;
    }

    // Getter and setter methods for all fields
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
