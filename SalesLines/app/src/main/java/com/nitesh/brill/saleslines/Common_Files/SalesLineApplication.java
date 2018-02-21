package com.nitesh.brill.saleslines.Common_Files;

import android.app.Application;


/**
 * Created by Nitesh Android on 14-09-2017.
 */

public class SalesLineApplication extends Application {
    SaveData objSaveData;
    private static SalesLineApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();


        mInstance = this;
    }

    public static synchronized SalesLineApplication getApp(){
        return mInstance;
    }


}
