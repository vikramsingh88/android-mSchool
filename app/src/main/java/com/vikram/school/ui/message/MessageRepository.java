package com.vikram.school.ui.message;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vikram.school.api.ISchoolAPI;
import com.vikram.school.api.WebService;
import com.vikram.school.ui.message.list.ListMessageResponse;
import com.vikram.school.utility.Constants;
import com.vikram.school.utility.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRepository {
    private String TAG = "MessageRepository";
    private ISchoolAPI iSchoolAPI;

    public LiveData<MessageResponse> sendMessage(Message message) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<MessageResponse> messageResult = new MutableLiveData<MessageResponse>();
        Call<MessageResponse> call = iSchoolAPI.sendMessage(message, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to send message");
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d(Constants.TAG, TAG+" send message request success");
                MessageResponse tempResult = response.body();
                messageResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in sending message");
                messageResult.setValue(null);
            }
        });
        return messageResult;
    }

    public LiveData<ListMessageResponse> getMessages(String session) {
        iSchoolAPI = WebService.instance().getRetrofitClient().create(ISchoolAPI.class);
        final MutableLiveData<ListMessageResponse> listMessagesResult = new MutableLiveData<ListMessageResponse>();
        Call<ListMessageResponse> call = iSchoolAPI.getMessages(session, PreferenceManager.instance().getToken());
        Log.d(Constants.TAG, TAG+" Try to get messages");
        call.enqueue(new Callback<ListMessageResponse>() {
            @Override
            public void onResponse(Call<ListMessageResponse> call, Response<ListMessageResponse> response) {
                Log.d(Constants.TAG, TAG+" get messages request success");
                ListMessageResponse tempResult = response.body();
                listMessagesResult.setValue(tempResult);
            }

            @Override
            public void onFailure(Call<ListMessageResponse> call, Throwable t) {
                Log.e(Constants.TAG, TAG+" Error in getting messages");
                listMessagesResult.setValue(null);
            }
        });
        return listMessagesResult;
    }
}
