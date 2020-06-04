package com.vikram.school.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private String TAG = "HomeRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<ClassesListResponse> getClasses() {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ClassesListResponse> classResult = new MutableLiveData<ClassesListResponse>();
        Call<ClassesListResponse> call = iSchoolAPI.getClasses();
        Log.d(Constants.TAG, TAG+" Try to get list of classes");
        call.enqueue(new Callback<ClassesListResponse>() {
            @Override
            public void onResponse(Call<ClassesListResponse> call, Response<ClassesListResponse> response) {
                Log.d(Constants.TAG, TAG+" get classes request success");
                ClassesListResponse tempResult = response.body();
                classResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ClassesListResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in getting list of classes");
            }
        });
        return classResult;
    }

    //get class fees by class
    public LiveData<ClassFeesResponse> getClassFeesByClass(String className) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ClassFeesResponse> classResult = new MutableLiveData<ClassFeesResponse>();
        Call<ClassFeesResponse> call = iSchoolAPI.getClassFeesByClass(className);
        Log.d(Constants.TAG, TAG+" Try to get class fees by class name");
        call.enqueue(new Callback<ClassFeesResponse>() {
            @Override
            public void onResponse(Call<ClassFeesResponse> call, Response<ClassFeesResponse> response) {
                Log.d(Constants.TAG, TAG+" get classes fees by class request success");
                ClassFeesResponse tempResult = response.body();
                classResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ClassFeesResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in getting getting class fees by class");
            }
        });
        return classResult;
    }
}
