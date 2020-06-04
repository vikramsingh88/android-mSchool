package com.vikram.school.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private LiveData<ClassesListResponse> classResult;
    private LiveData<ClassFeesResponse> classFeesResult;

    private HomeRepository homeRepository = new HomeRepository();

    public LiveData<ClassesListResponse> getClasses() {
        classResult = homeRepository.getClasses();
        return classResult;
    }

    public LiveData<ClassFeesResponse> getClassFeesByClass(String className) {
        classFeesResult = homeRepository.getClassFeesByClass(className);
        return classFeesResult;
    }
}