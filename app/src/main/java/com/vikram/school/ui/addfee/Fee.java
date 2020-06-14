package com.vikram.school.ui.addfee;

public class Fee {
    private String _id;
    private String studentId;
    private String feeType;
    private String amount;
    private String remainingFee;
    private String advanceFee;
    private String month;
    private String date;
    private String session;
    private String mobile;
    private String name;
    private String createdBy;

    public Fee(String studentId, String feeType, String amount, String month,
               String remainingFee, String advanceFee) {
        this.studentId = studentId;
        this.feeType = feeType;
        this.amount = amount;
        this.month = month;
        this.remainingFee = remainingFee;
        this.advanceFee = advanceFee;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemainingFee() {
        return remainingFee;
    }

    public void setRemainingFee(String remainingFee) {
        this.remainingFee = remainingFee;
    }

    public String getAdvanceFee() {
        return advanceFee;
    }

    public void setAdvanceFee(String advanceFee) {
        this.advanceFee = advanceFee;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
