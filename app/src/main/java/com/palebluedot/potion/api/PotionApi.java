package com.palebluedot.potion.api;

import com.palebluedot.clientsdk.model.Customized;
import com.palebluedot.clientsdk.model.Material;
import com.palebluedot.potion.api.model.CustomizedBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PotionApi {
    String BASE_URL = "https://81ig0cfs1a.execute-api.us-east-1.amazonaws.com/android/";
    /*resource: material*/
    @GET("material")
    Call<Material> getMaterialsByName(@Query("name") String keyword);
    @GET("material")
    Call<Material> getMaterialsByEffect(@Query("effect") String keyword);

    /*resource: customized*/
    @GET("customized")
    Call<Customized> getCustomizedById(@Query("id") int id);

    //@FormUrlEncoded
    @POST("customized")
    Call<Integer> postCustomized(
            @Body CustomizedBody newItem
            );

    /*
    Call<ApiResponse> postCustomized(
            @Field("name") String name,
            @Field("factory") String factory,
            @Field("effect") String effect,
            @Field("wayToTake") String way,
            @Field("remark") String remark
    );*/

    @PATCH("customized")
    Call<Integer> patchCustomized(
            @Query("id") int id,
            @Body CustomizedBody newItem
    );

    @DELETE("customized")
    Call<Integer> deleteCustomized(@Query("id") int id);

}
