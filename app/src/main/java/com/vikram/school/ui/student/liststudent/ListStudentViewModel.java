package com.vikram.school.ui.student.liststudent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ListStudentViewModel extends ViewModel {
    private LiveData<ListStudentResponse> listStudentResult;

    private ListStudentRepository listStudentRepository = new ListStudentRepository();

    public LiveData<ListStudentResponse> getStudentByClass(String className) {
        listStudentResult = listStudentRepository.getStudentByClass(className);
        return listStudentResult;
    }
}
