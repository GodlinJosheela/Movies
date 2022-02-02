package com.gjrs.greedygame.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.gjrs.greedygame.util.Constants;

public class NetworkAdapter {

    private static final Object LOCK = new Object();
    private static volatile Retrofit sInstance;

    public static Retrofit getRetrofitInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new Retrofit.Builder()
                            .baseUrl(Constants.TMDB_BASE_URL)
                            .client(getOkhttpBuild())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return sInstance;
    }

    private static OkHttpClient getOkhttpBuild() {
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

}
