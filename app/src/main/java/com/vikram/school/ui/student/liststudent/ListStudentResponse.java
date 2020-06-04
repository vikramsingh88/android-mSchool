package com.vikram.school.ui.student.liststudent;

import com.vikram.school.ui.student.Student;

import java.util.List;

public class ListStudentResponse {
    private String message;
    private boolean isSuccess;
    private List<Student> students;

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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
