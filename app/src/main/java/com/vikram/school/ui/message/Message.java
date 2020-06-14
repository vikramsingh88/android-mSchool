package com.vikram.school.ui.message;

import java.util.Date;

public class Message {
    private String message;
    private String stdClass;
    private String session;
    private Date date;

    public Message(String message, String stdClass, String session) {
        this.message = message;
        this.stdClass = stdClass;
        this.session = session;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
