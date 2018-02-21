package com.nitesh.brill.saleslines._User_Classes.User_Location;

/**
 * Created by Paras-Android on 06-02-2018.
 */


import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
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
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.APIClient;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.RetrofitAPI;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.UserCoordinates;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GPSTracker_DUP extends Service implements LocationListener {


    private  Context mContext=null;
    RetrofitAPI retrofitAPI;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    SaveData objSaveData;
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

    public GPSTracker_DUP(Context context) {
        this.mContext = context;
        //getLocation();
    }

    @Override
    public void onCreate() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);


            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
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
                //        LocationRequest locationRequest = new LocationRequest();
                //          locationRequest.setInterval(0);
                //            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//                Intent intent = new Intent(this, LocationReceiver.class);

                //              PendingIntent locationIntent = PendingIntent.getBroadcast(getApplicationContext(), 14872, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    Log.e("Provider ",  provider + " has been selected."+location.getLatitude()+"==="+location.getLongitude());

                    saveLocation(location.getLatitude(),location.getLongitude());

                    //onLocationChanged(location);
                }
            }


            // getting network status
//            isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GPSTracker_DUP(){}


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);


            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
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
        //        LocationRequest locationRequest = new LocationRequest();
      //          locationRequest.setInterval(0);
    //            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//                Intent intent = new Intent(this, LocationReceiver.class);

  //              PendingIntent locationIntent = PendingIntent.getBroadcast(getApplicationContext(), 14872, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    Log.e("Provider ",  provider + " has been selected."+location.getLatitude()+"==="+location.getLongitude());

                    saveLocation(location.getLatitude(),location.getLongitude());

                    //onLocationChanged(location);
                }
            }


            // getting network status
//            isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker_DUP.this);
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
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
    public void saveLocation(Double latitude,Double longitude){
        objSaveData = new SaveData(mContext);

            Log.e("Saving Coordinates", latitude + " " + longitude);
            AudioDbHelper audioDbHelper= new AudioDbHelper(mContext);
            UserCoordinates userCoordinates = new UserCoordinates();
            userCoordinates.setLatitude(String.valueOf(latitude));
            userCoordinates.setLongitude(String.valueOf(longitude));
            userCoordinates.setUploaded("no");
            SaveData objSaveData = new SaveData(mContext);
            userCoordinates.setUserEmail(objSaveData.getString("LoginId"));
            String time = new SimpleDateFormat("hh:mm: aa").format(Calendar.getInstance().getTime());
            userCoordinates.setLocationTime(time);
            audioDbHelper.addCoordinates(userCoordinates);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUsingGPS();
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

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}