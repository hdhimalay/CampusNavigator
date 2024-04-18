package com.example.campusnavigator;

public class Notice {
    private String heading;
    private String content;
    private String fileUrl;
    private String date; // Add a field for the date

    // Default constructor required for Firebase
    public Notice() {
    }

    // Constructor without date parameter
    public Notice(String heading, String content, String fileUrl) {
        this.heading = heading;
        this.content = content;
        this.fileUrl = fileUrl;
    }

    // Constructor with date parameter
    public Notice(String heading, String content, String fileUrl, String date) {
        this.heading = heading;
        this.content = content;
        this.fileUrl = fileUrl;
        this.date = date;
    }

    // Getters and setters for the fields
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
