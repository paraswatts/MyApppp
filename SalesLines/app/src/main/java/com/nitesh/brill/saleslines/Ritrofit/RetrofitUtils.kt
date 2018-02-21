package com.nitesh.brill.saleslines.Ritrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by root on 10/1/17.
 */

object RetrofitUtils {



    //============ New Ip===========\\
    //console.salelinecrm.com
    val Base_Url = " http://console.salelinecrm.com/saleslineapi/"
    val apihostRetrofit: Retrofit
        get() {
            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

            val retrofitt = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Base_Url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofitt
        }

}

