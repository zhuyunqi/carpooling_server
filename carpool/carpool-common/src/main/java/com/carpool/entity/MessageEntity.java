package com.carpool.entity;

public class MessageEntity {
    private String subject;
    private String toMailAddress;
    private String content;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getToMailAddress() {
        return toMailAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setToMailAddress(String toMailAddress) {
        this.toMailAddress = toMailAddress;
    }
}
