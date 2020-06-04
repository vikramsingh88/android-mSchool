package com.vikram.school.ui.student.liststudent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.ui.student.StudentResponse;
import com.vikram.school.utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListStudentRepository {
    private String TAG = "ListStudentRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<ListStudentResponse> getStudentByClass(String className) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ListStudentResponse> listStudentResult = new MutableLiveData<ListStudentResponse>();
        Call<ListStudentResponse> call = iSchoolAPI.getStudentByClass(className);
        Log.d(Constants.TAG, TAG+" Trying to get students by classes");
        call.enqueue(new Callback<ListStudentResponse>() {
            @Override
            public void onResponse(Call<ListStudentResponse> call, Response<ListStudentResponse> response) {
                Log.d(Constants.TAG, TAG+" get student by class request success");
                ListStudentResponse tempResult = response.body();
                listStudentResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ListStudentResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in getting students by class name");
            }
        });
        return listStudentResult;
    }
}
