package com.palebluedot.potion.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PotionApiUtil {
    private static PotionApiUtil mPotionApiUtil = new PotionApiUtil();
    private PotionApi mApi;

    //singleton
    public static PotionApiUtil getInstance() {
        return mPotionApiUtil;
    }
    private PotionApiUtil(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();
        Retrofit mRetrofit = new Retrofit.Builder().baseUrl(PotionApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(PotionApi.class);
    }
    public PotionApi getApi(){
        return mApi;
    }

    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e("PotionAPI data :", message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
