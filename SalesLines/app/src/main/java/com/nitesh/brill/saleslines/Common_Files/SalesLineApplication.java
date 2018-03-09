package com.nitesh.brill.saleslines.Common_Files;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


/**
 * Created by Nitesh Android on 14-09-2017.
 */

public class SalesLineApplication extends MultiDexApplication {
    SaveData objSaveData;
    private static SalesLineApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();


        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static synchronized SalesLineApplication getApp(){
        return mInstance;
    }


}
