package com.vikram.school.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class StudentViewModel extends ViewModel {
    private LiveData<StudentResponse> studentResult;
    private LiveData<ClassTeacherResponse> classTeacherResult;

    private StudentRepository studentRepository = new StudentRepository();

    public LiveData<StudentResponse> addStudent(Student student) {
        studentResult = studentRepository.addStudent(student);
        return studentResult;
    }

    public LiveData<StudentResponse> updateStudent(Student student) {
        studentResult = studentRepository.updateStudent(student);
        return studentResult;
    }

    public LiveData<ClassTeacherResponse> getClassTeacherByClass(String className) {
        classTeacherResult = studentRepository.getClassTeacherByClass(className);
        return classTeacherResult;
    }
}
