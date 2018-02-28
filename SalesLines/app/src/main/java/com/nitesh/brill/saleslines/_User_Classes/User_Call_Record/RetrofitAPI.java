package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import com.google.gson.JsonElement;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    //https://maps.googleapis.com/maps/api/directions/json?origin=30.70858894,76.69189905&waypoints=optimize:true|30.70860742,76.69193445|30.70858054,76.69188208|30.70849768,76.69218831|30.70857879,76.69185513|30.70857537,76.69182934|30.70861325,76.69195388|30.70858151,76.6918046|30.70858886,76.69177956|&destination=30.70858886,76.69177956&sensor=false
    //http://console.salelinecrm.com/saleslineapi/GetGPSLocationsManagerDay/{ManagerId}/{ClientId}/{Date}

    @GET("GetGPSLocationsManagerDay/{ManagerId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsManagerDay(
            @Path("ManagerId")String managerId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );

    @GET("GetGPSLocationsGMDay/{GMId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsGMDay(
            @Path("GMId")String managerId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );


    @GET("GetGPSLocationsASMDay/{ASMId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsASMDay(
            @Path("ASMId")String managerId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );

    @GET("GetGPSLocationsAGMDay/{AGMId}/{ClientId}/{Date}")
    Call<JsonElement> getGPSLocationsAGMDay(
            @Path("AGMId")String managerId,
            @Path("ClientId")String clientId,
            @Path("Date")String date
    );

    @GET("directions/json")
    Call<JsonElement> getMapPath(@Query(value = "origin",encoded = true) String origin,@Query(value = "waypoints",encoded = true)String waypoints,@Query(value = "destination",encoded = true) String destination,@Query("sensor") String sensor,@Query("key") String apikey);

}
