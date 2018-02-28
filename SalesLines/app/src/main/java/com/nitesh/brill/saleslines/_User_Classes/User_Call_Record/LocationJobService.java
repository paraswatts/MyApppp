package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
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
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.ConstantValue;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker;
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker_DUP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nitesh Android on 18-09-2017.
 */

public class LocationJobService extends JobService implements LocationListener {
    RetrofitAPI retrofitAPI;
    SaveData objSaveData;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    static double startLat = 0.0;
    static double startLng = 0.0;
    String type;
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



    @Override
    public boolean onStartJob(JobParameters job) {
        String time1 = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

        //Toast.makeText(this,"Job fired Location"+time1, Toast.LENGTH_SHORT).show();
        Log.e("Job Fired Location","Job Fired" +time1+"===="+job.getTag());
        AudioDbHelper dbHelper = new AudioDbHelper(this);
        SaveData objSaveData = new SaveData(this);


        if(objSaveData.getBoolean("loginState")){
            if(objSaveData.getString("role_id").equals("5")) {
                if(job.getTag().equals(objSaveData.getString("user_id"))){
                Log.e("Sharing", "Location");
                //GPSTracker gpsTracker1 = new GPSTracker(this );
               // GPSTracker gpsTracker1 = new GPSTracker(getBaseContext());
                //gpsTracker1.getLocation();
//


//                GPSTracker_DUP gpsTracker = new GPSTracker_DUP(this);
//                gpsTracker.getLocation();
                //Intent intent = new Intent(this,GPSTracker_DUP.class);
               // startService(intent);
                //Log.e("Lat long",gpsTracker.getLatitude()+"===="+gpsTracker.getLongitude());



                //=======================================//

                try {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setPowerRequirement(Criteria.POWER_HIGH);
                    provider = locationManager.getBestProvider(criteria, false);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return false;
                    }

                    // getting GPS status
                    isGPSEnabled = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if(!isGPSEnabled)
                    {
                        showSettingsAlert();

                    }
                    else
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*60*2,5000,this);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null) {
                            Log.e("Provider ",  provider + " has been selected."+location.getLatitude()+"==="+location.getLongitude());

                            if(startLat<=0.0) {

                                startLat = location.getLatitude();
                                startLng = location.getLongitude();
                                Log.e("Lat Lng if",startLat+"===="+startLng);
                                saveLocation(location.getLatitude(),location.getLongitude(),"insert");
                            }
                            else{
                                Location loc1 = new Location("");
                                loc1.setLatitude(startLat);
                                loc1.setLongitude(startLng);

                                Location loc2 = new Location("");
                                loc2.setLatitude(location.getLatitude());
                                loc2.setLongitude(location.getLongitude());

                                float distanceInMeters = loc1.distanceTo(loc2);

                                Log.e("Distance in meters",""+distanceInMeters +startLat+"===="+startLng+"====="+location.getLatitude()+"======="+location.getLongitude());

                                if(distanceInMeters>300){
                                    startLat = location.getLatitude();
                                    startLng = location.getLongitude();
                                    Log.e("Lat Lng if distance",startLat+"===="+startLng);
                                    saveLocation(startLat,startLng,"insert");
                                }
                                else{
                                    startLat = location.getLatitude();
                                    startLng = location.getLongitude();
                                    Log.e("Lat Lng else distance",startLat+"===="+startLng);
                                    saveLocation(startLat,startLng,"update");
                                }
                            }



                            onLocationChanged(location);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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

        if(isConnected(this)){
            Log.e("Internet","Available");
        }
        else{
            Log.e("Internet","Not Available");
        }

        return true;
    }


    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });



        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e("onlocation","changed"+latitude+"======="+longitude);

        //saveLocation(location.getLatitude(),location.getLongitude());

    }
    public void saveLocation(Double latitude,Double longitude,String type){
        objSaveData = new SaveData(this);

        Log.e("Saving Coordinates", latitude + " " + longitude);
        AudioDbHelper audioDbHelper= new AudioDbHelper(this);
        UserCoordinates userCoordinates = new UserCoordinates();
        userCoordinates.setLatitude(String.valueOf(latitude));
        userCoordinates.setLongitude(String.valueOf(longitude));
        userCoordinates.setUploaded("no");
        SaveData objSaveData = new SaveData(this);
        userCoordinates.setUserEmail(objSaveData.getString("LoginId"));
        String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        userCoordinates.setLocationTime(time);
        userCoordinates.setType(type);
        audioDbHelper.addCoordinates(userCoordinates);
    }


    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
