package com.vikram.school.ui.home;

import com.vikram.school.ui.slideshow.Classes;

import java.util.List;

public class ClassesListResponse {
    private String message;
    private boolean isSuccess;
    private List<Classes> classes;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<Classes> getClasses() {
        return classes;
    }

    public void setClasses(List<Classes> classes) {
        this.classes = classes;
    }
}
