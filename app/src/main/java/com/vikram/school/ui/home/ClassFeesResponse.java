package com.vikram.school.ui.home;

public class ClassFeesResponse {
    private String message;
    private boolean isSuccess;
    private String classFees;
    private String classExamFees;

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

    public String getClassFees() {
        return classFees;
    }

    public void setClassFees(String classFees) {
        this.classFees = classFees;
    }

    public String getClassExamFees() {
        return classExamFees;
    }

    public void setClassExamFees(String classExamFees) {
        this.classExamFees = classExamFees;
    }
}
