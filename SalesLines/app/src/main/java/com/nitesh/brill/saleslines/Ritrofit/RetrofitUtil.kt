package com.nitesh.brill.saleslines.Ritrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by root on 10/1/17.
 */

object RetrofitUtil {

    //============ New Ip===========\\
    val Base_Url = " http://console.salelinecrm.com/saleslineapi/"
    val Base_Url1 = "http://dd01779f.ngrok.io/android_login_api/"


    val hostRetrofit: Retrofit
        get() {

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Base_Url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit
        }

    val hostRetrofit1: Retrofit
        get() {

            val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Base_Url1)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit
        }


    //   GM>   lrgm@gmail.com/lrgm           genral manager
    //  AGM>lragm@gmail.com/lragm      Area  genral manager
    //  ASM> lrasm1@gmail.com/lrasm     area sale manager
    //  MGR>lrmgr2@gmail.com/lrmgr2            manager
    //  SALEMAn >lrsm1@gmail.com/lrsm1


}

