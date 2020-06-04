package com.vikram.school.ui.listfee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ListFeesViewModel extends ViewModel {
    private LiveData<ListFeesResponse> listFeesResult;

    private ListFeesRepository listFeesRepository = new ListFeesRepository();

    public LiveData<ListFeesResponse> getStudentFeesByStudentId(String studentId) {
        listFeesResult = listFeesRepository.getStudentFeesByStudentId(studentId);
        return listFeesResult;
    }
}
