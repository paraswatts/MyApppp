package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import com.google.gson.JsonElement;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Paras Android on 29-08-2017.
 */

public interface RetrofitAPI {

    @POST("SaveAudioRecord")
    Call<JsonElement> SaveAudioRecord(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("SaveGPSLocation")
    Call<JsonElement> mSaveUserLocation(@Body RequestBody body);

    //http://console.salelinecrm.com/saleslineapi/GetGPSLocationsUserDay/{UserId}/{ClientId}/{Date}

    @GET("GetGPSLocationsUserDay/{UserId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsUserDay(
            @Path("UserId")String userId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );


    //http://console.salelinecrm.com/saleslineapi/GetGPSLocationsManagerDay/{ManagerId}/{ClientId}/{Date}

    @GET("GetGPSLocationsManagerDay/{ManagerId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsManagerDay(
            @Path("ManagerId")String managerId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );

}
