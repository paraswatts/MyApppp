package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
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
import com.nitesh.brill.saleslines.Common_Files.UpdateProfilePicture;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nitesh Android on 18-09-2017.
 */

public class ScheduledJobService extends JobService {
    SaveData objSaveData;
    RetrofitAPI retrofitAPI;
    Activity activity;
    @Override
    public boolean onStartJob(JobParameters job) {
        String time1 = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

       // Toast.makeText(this,"Job fired "+time1, Toast.LENGTH_SHORT).show();
       // Log.e("Job Fired","Job Fired" +time1);
        AudioDbHelper dbHelper = new AudioDbHelper(this);
        SaveData objSaveData = new SaveData(this);



            if(objSaveData.getBoolean("loginState")){
                Intent intent = new Intent("INTENT_PROFILE");
                sendBroadcast(intent);
                if(objSaveData.getString("role_id").equals("5"))
                    {
                        if (dbHelper.getNoCountUserLocation(objSaveData.getString("LoginId"), "no") > 0) {
                            String time = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

                            Log.e("No Upload count", "" + dbHelper.getNoCountUserLocation(objSaveData.getString("LoginId"), "no") + "and time is " + time);
                            new CoordinatesUploadAsyncTask(this).execute();
                        }
                        new CallRecordingUploadAsyncTask(this).execute();

                    }
                }
            Log.e("Internet","Available");

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(ScheduledJobService.class) // the JobService that will be called
                .setRecurring(false)
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                        // only run when the device is charging
                )
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setLifetime(Lifetime.FOREVER)
                .setTag("demo")        // uniquely identifies the job
                .build();
        dispatcher.mustSchedule(myJob);
        return true;
    }






    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
