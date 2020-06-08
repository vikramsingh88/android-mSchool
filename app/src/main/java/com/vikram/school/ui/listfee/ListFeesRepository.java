package com.vikram.school.ui.listfee;

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

public class ListFeesRepository {
    private String TAG = "ListFeesRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<ListFeesResponse> getStudentFeesByStudentId(String studentId) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ListFeesResponse> listStudentFeesResult = new MutableLiveData<ListFeesResponse>();
        Call<ListFeesResponse> call = iSchoolAPI.getStudentFeesByStudentId(studentId, PreferenceManager.instance().getSession());
        Log.d(Constants.TAG, TAG+" Trying to get student fees by student id");
        call.enqueue(new Callback<ListFeesResponse>() {
            @Override
            public void onResponse(Call<ListFeesResponse> call, Response<ListFeesResponse> response) {
                Log.d(Constants.TAG, TAG+" get student fees by student id request success");
                ListFeesResponse tempResult = response.body();
                listStudentFeesResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ListFeesResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in getting students fees by student id");
            }
        });
        return listStudentFeesResult;
    }
}
