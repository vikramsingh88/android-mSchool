package com.vikram.school.ui.addfee;

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

public class AddFeeRepository {
    private String TAG = "AddFeeRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<AddFeeResponse> addFee(Fee fee) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<AddFeeResponse> addFeeResult = new MutableLiveData<AddFeeResponse>();
        Call<AddFeeResponse> call = iSchoolAPI.addFee(fee, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to add student fee");
        call.enqueue(new Callback<AddFeeResponse>() {
            @Override
            public void onResponse(Call<AddFeeResponse> call, Response<AddFeeResponse> response) {
                Log.d(Constants.TAG, TAG+" add student fee request success");
                AddFeeResponse tempResult = response.body();
                addFeeResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<AddFeeResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in adding student fee");
                addFeeResult.setValue(null);
            }
        });
        return addFeeResult;
    }

    public LiveData<AddFeeResponse> updateFee(Fee fee) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<AddFeeResponse> updateFeeResult = new MutableLiveData<AddFeeResponse>();
        Call<AddFeeResponse> call = iSchoolAPI.updateFee(fee, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to update student fee");
        call.enqueue(new Callback<AddFeeResponse>() {
            @Override
            public void onResponse(Call<AddFeeResponse> call, Response<AddFeeResponse> response) {
                Log.d(Constants.TAG, TAG+" update student fee request success");
                AddFeeResponse tempResult = response.body();
                updateFeeResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<AddFeeResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in updating student fee");
                updateFeeResult.setValue(null);
            }
        });
        return updateFeeResult;
    }
}
