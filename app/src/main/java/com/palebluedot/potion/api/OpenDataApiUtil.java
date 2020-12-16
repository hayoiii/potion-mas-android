package com.palebluedot.potion.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenDataApiUtil {
    private static OpenDataApiUtil mOpenDataApiUtil = new OpenDataApiUtil();
    private OpenDataApi mApi;

    //singleton
    public static OpenDataApiUtil getInstance() {
        return mOpenDataApiUtil;
    }
    private OpenDataApiUtil(){
        Retrofit mRetrofit = new Retrofit.Builder().baseUrl(OpenDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(OpenDataApi.class);
    }
    public OpenDataApi getApi(){
        return mApi;
    }
}
