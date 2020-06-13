package com.vikram.school.ui.message;

public class Message {
    private String message;
    private String stdClass;
    private String session;

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
}
