package com.vikram.school.ui.slideshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRepository {

    private String TAG = "ClassRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<ClassesResponse> addClasses(Classes classes) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ClassesResponse> classesResult = new MutableLiveData<ClassesResponse>();
        Call<ClassesResponse> call = iSchoolAPI.addClasses(classes, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to add classes");
        call.enqueue(new Callback<ClassesResponse>() {
            @Override
            public void onResponse(Call<ClassesResponse> call, Response<ClassesResponse> response) {
                Log.d(Constants.TAG, TAG+" add classes request success");
                ClassesResponse tempResult = response.body();
                classesResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ClassesResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in adding classes");
            }
        });
        return classesResult;
    }

    //update existing class record
    public LiveData<ClassesResponse> updateClasses(Classes classes) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ClassesResponse> classesResult = new MutableLiveData<ClassesResponse>();
        Call<ClassesResponse> call = iSchoolAPI.updateClasses(classes, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to update classes");
        call.enqueue(new Callback<ClassesResponse>() {
            @Override
            public void onResponse(Call<ClassesResponse> call, Response<ClassesResponse> response) {
                Log.d(Constants.TAG, TAG+" update classes request success");
                ClassesResponse tempResult = response.body();
                classesResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ClassesResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in updating classes");
            }
        });
        return classesResult;
    }
}
