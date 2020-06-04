package com.vikram.school.ui.listfee;

import com.vikram.school.ui.addfee.Fee;

import java.util.List;

public class ListFeesResponse {
    private String message;
    private boolean isSuccess;
    private List<Fee> fees;

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

    public List<Fee> getFees() {
        return fees;
    }

    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }
}
