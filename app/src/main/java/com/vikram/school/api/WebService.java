package com.vikram.school.api;

import com.vikram.school.utility.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {
    private Retrofit retrofit = null;

    private WebService() {

    }

    private static class SingletonHolder {
        private static final WebService INSTANCE = new WebService();
    }

    public static final WebService instance() {
        return WebService.SingletonHolder.INSTANCE;
    }

    public Retrofit getRetrofitClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
