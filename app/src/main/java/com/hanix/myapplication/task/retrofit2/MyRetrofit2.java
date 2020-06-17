package com.hanix.myapplication.task.retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanix.myapplication.common.constants.URLApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit2 빌더
 */
public class MyRetrofit2 {

    static Retrofit mRetrofit;

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
        httpclient.addInterceptor(logger);

        Gson gson = new GsonBuilder()                                               // 데이터를 자동으로 컨버팅할 수 있게 GsonFactory 사용
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLApi.getServerURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpclient.build())
                .build();

        return mRetrofit;
    }

}
