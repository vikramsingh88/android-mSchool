package com.vikram.school.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private String TAG = "LoginRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<LoginResult> login(User user) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<LoginResult> loginResult = new MutableLiveData<LoginResult>();
        Call<LoginResult> call = iSchoolAPI.login(user);
        Log.d(Constants.TAG, TAG+" Try to login");
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                Log.d(Constants.TAG, TAG+" login request success");
                LoginResult tempResult = response.body();
                loginResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in authenticating user");
            }
        });
        return loginResult;
    }
}
