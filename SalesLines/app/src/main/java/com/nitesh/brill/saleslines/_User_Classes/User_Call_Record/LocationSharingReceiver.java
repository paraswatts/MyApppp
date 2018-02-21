package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.TodayFollowUpActivity;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker_DUP;
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.TodayFollowUp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSharingReceiver extends WakefulBroadcastReceiver
{

    private  Context mContext=null;
    RetrofitAPI retrofitAPI;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private String provider;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;



	SaveData objSaveData;
	@Override
	 public void onReceive(Context context, Intent intent)
	{
		objSaveData =  new SaveData(context);
		if(objSaveData.getBoolean("loginState")){
			if(objSaveData.getString("role_id").equals("5")) {
				Log.e("Sharing", "Location");
				//GPSTracker gpsTracker1 = new GPSTracker(this );

//				GPSTracker_DUP gpsTracker = new GPSTracker_DUP(context);
//				gpsTracker.getLocation();
//
//				GPSTracker gpsTracker1 = new GPSTracker();
//				gpsTracker1.getLocation();
//
//				Log.e("Lat long",gpsTracker.getLatitude()+"===="+gpsTracker.getLongitude());
							}
		}
	 }




}
