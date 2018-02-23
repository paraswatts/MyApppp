package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anupamchugh on 05/01/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {



        retrofit = new Retrofit.Builder()
                .baseUrl("http://console.salelinecrm.com/saleslineapi/")
                .addConverterFactory(GsonConverterFactory.create())
              //client(client)
                .build();



        return retrofit;
    }

    public static Retrofit getMapsClient() {



        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                //client(client)
                .build();



        return retrofit;
    }

}
