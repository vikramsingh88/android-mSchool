package com.vikram.school.ui.slideshow;

public class Classes {
    private String _id;
    private String classTeacherName;
    private String className;
    private String classFees;
    private String classExamFees;
    private String date;

    public Classes() {
    }

    public Classes(String classTeacherName, String className, String classFees, String classExamFees) {
        this.classTeacherName = classTeacherName;
        this.className = className;
        this.classFees = classFees;
        this.classExamFees = classExamFees;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
