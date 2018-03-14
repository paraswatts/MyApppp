package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.TodayFollowUpActivity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker_DUP;
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.TodayFollowUp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSharingReceiver extends JobService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    public boolean onStartJob(JobParameters job) {
        String time1 = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

        //Toast.makeText(this,"Job fired Location"+time1, Toast.LENGTH_SHORT).show();
        Log.e("Job Fired Location", "Job Fired" + time1 + "====" + job.getTag());
        AudioDbHelper dbHelper = new AudioDbHelper(this);
        SaveData objSaveData = new SaveData(this);


        if (objSaveData.getBoolean("loginState")) {
            if (objSaveData.getString("role_id").equals("5")) {
                if (job.getTag().equals(objSaveData.getString("user_id"))) {
                    Log.e("Sharing", "Location");
                    //GPSTracker gpsTracker1 = new GPSTracker(this );
                    // GPSTracker gpsTracker1 = new GPSTracker(getBaseContext());
                    //gpsTracker1.getLocation();
//
                    mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();

                    if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    }

                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
                    Job myJob = dispatcher.newJobBuilder()
                            .setService(LocationJobService.class) // the JobService that will be called
                            .setRecurring(false)
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            //.setTrigger(Trigger.executionWindow(0, 60))
                            .setTrigger(Trigger.executionWindow(10*60, 15*60))
                            .setLifetime(Lifetime.FOREVER)
                            .setTag(objSaveData.getString("user_id"))        // uniquely identifies the job
                            .build();
                    dispatcher.mustSchedule(myJob);
                }
            }
        }

//        if(isConnected(this)){
//            Log.e("Internet","Available");
//        }
//        else{
//            Log.e("Internet","Not Available");
//        }

        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, ,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//            }
//            return false;
//        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
//        if (mMapLocationListener != null) {
//            mMapLocationListener.onLocationChanged(location);
//        }
    }

    protected void startLocationUpdates() {



    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient,
//                REQUEST,
//                mMapLocationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}