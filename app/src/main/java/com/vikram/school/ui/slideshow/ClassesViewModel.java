package com.vikram.school.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassesViewModel extends ViewModel {
    private LiveData<ClassesResponse> classesResult;

    private ClassRepository classRepository = new ClassRepository();

    public LiveData<ClassesResponse> addClasses(Classes classes) {
        classesResult = classRepository.addClasses(classes);
        return classesResult;
    }

    public LiveData<ClassesResponse> updateClasses(Classes classes) {
        classesResult = classRepository.updateClasses(classes);
        return classesResult;
    }
}