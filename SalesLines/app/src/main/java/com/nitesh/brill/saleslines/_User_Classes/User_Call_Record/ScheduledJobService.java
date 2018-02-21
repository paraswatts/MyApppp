package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

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
import com.nitesh.brill.saleslines.Common_Files.SaveData;
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

    @Override
    public boolean onStartJob(JobParameters job) {
        String time1 = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

       // Toast.makeText(this,"Job fired "+time1, Toast.LENGTH_SHORT).show();
       // Log.e("Job Fired","Job Fired" +time1);
        AudioDbHelper dbHelper = new AudioDbHelper(this);
        SaveData objSaveData = new SaveData(this);




            if(objSaveData.getBoolean("loginState")){
                if(objSaveData.getString("role_id").equals("5"))
                    {
                        if (dbHelper.getNoCountUserLocation(objSaveData.getString("LoginId"), "no") > 0) {
                            String time = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());

                            Log.e("No Upload count", "" + dbHelper.getNoCountUserLocation(objSaveData.getString("LoginId"), "no") + "and time is " + time);
                            new CoordinatesUploadAsyncTask(this).execute();
                        }
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


    private void checkLeadExistOrNot(final String phoneNumber) {
        //  Log.d("checkLeadExistOrNot", "==checkLeadExistOrNot===");
        final SaveData objSaveData = new SaveData(this);
        ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);

        Call<JsonElement> call = apiEndpointInterface.mCheckAddUpdateLead("" + phoneNumber, objSaveData.getString("client_id"), objSaveData.getString("user_id"));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //    Log.d("URL", "=====" + response.raw().request().url());
//                Log.e("=============", response.body().toString());
                String mExist = response.body().toString();
                try {
                    if (mExist.equals("0")) {
                        //    Log.e("Lead", "does not exist");

                        if (objSaveData.getString("role_id").equals("5")) {
                            if (objSaveData.getString(objSaveData.getString("MobileNumber")).equals("yes")) {
                                //   Log.e("Deleting files", "Else case");
                                File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");

                                final File[] files = file.listFiles();
                                for (File f : file.listFiles()) {
                                    if (f.getName().startsWith(objSaveData.getString("MobileNumber"))) {
                                        //   Log.e("File delete karan laga", f.getAbsolutePath() + "");
                                        f.delete();
                                    }
                                }


                                objSaveData.remove("MobileNumber");
                                if (objSaveData.getString("MobileNumber") != null) {
                                    //  Log.e("Key Removed", objSaveData.getString("MobileNumber"));
                                }
                            }
                        } else {

                            //   Log.e("Deleting files", "Else case");
                            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");

                            final File[] files = file.listFiles();
                            for (File f : file.listFiles()) {
                                if (f.getName().startsWith(objSaveData.getString("MobileNumber"))) {
                                    //  Log.e("File delete karan laga", f.getAbsolutePath() + "");
                                    f.delete();
                                }
                            }
                            objSaveData.remove("MobileNumber");
                            if (objSaveData.getString("MobileNumber") != null) {
                                // Log.e("Key Removed", objSaveData.getString("MobileNumber"));
                            }

                        }

                    } else {

                        //  Log.e("", "Lead Already Exists");
                        String phoneNumber = objSaveData.getString("MobileNumber");
                        if (objSaveData.getString("role_id").equals("5")) {
                            //  Log.e("Update Lead", "From user");
                            getLeadEnquiryDetails(phoneNumber);
                        }


                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Log.e("Response Lead", "Does Not Exist");
            }

        });

    }

    private void getLeadEnquiryDetails(String phoneNumber) {

        SaveData objSaveData = new SaveData(this);
        //Log.e("Get lead", "Enquiry Details" + phoneNumber + objSaveData.getString("user_id") + objSaveData.getString("client_id"));
        ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);

        Call<JsonElement> call = apiEndpointInterface.GetLeadCallEnquiryDetails(phoneNumber, objSaveData.getString("user_id"), objSaveData.getString("client_id"));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("URL", "=====" + response.raw().request().url());
                Log.e("=============", response.body().toString());


                //UsefullData.Log("User_Response    " + response.body().toString())
                try {
                    JSONArray array = new JSONArray(response.body().toString());

//                    for ( int i = 0; i< array.length() ;i++) {
                    final JSONObject item = array.getJSONObject(0);

                    final Intent  updateLeadIntent = new Intent(getApplicationContext(), User_Home_Activity.class);

//                        if(objSaveData.getString("role_id").equals("4")) {
//                            Log.e("Update Lead","From manager");
//                            updateLeadIntent = new Intent(getApplicationContext(), Manager_Home_Activity.class);
//                        }
//
//                        if(objSaveData.getString("role_id").equals("3")) {
//                            Log.e("Update Lead","From asm");
//                            updateLeadIntent = new Intent(getApplicationContext(), ASM_Home_Activity.class);
//                        }
//
//                        if(objSaveData.getString("role_id").equals("2")) {
//                            Log.e("Update Lead","From agm");
//                            updateLeadIntent = new Intent(getApplicationContext(), AGM_Home_Activity.class);
//                        }
//
//                        if(objSaveData.getString("role_id").equals("1")) {
//                            Log.e("Update Lead","From gm");
//                            updateLeadIntent = new Intent(getApplicationContext(), GM_Home_Activity.class);
//                        }

                    updateLeadIntent.putExtra("addLeadFromCall", "updateLead");
                    if (!item.getString("LeadId").equals(null) && !item.getString("LeadId").equals("null")) {
                        //Log.e("Lead id ", "Updatecall is " + item.getString("LeadId"));
                        updateLeadIntent.putExtra("leadId", item.getString("LeadId"));
                    }

//                    if (!item.getString("UserId").equals(null) && !item.getString("UserId").equals("null")) {
//                        Log.e("UserId id ", "Updatecall is " + item.getString("UserId"));
//                        updateLeadIntent.putExtra("userId", item.getString("UserId"));
//
//                    }

                    if (!item.getString("Rating").equals(null) && !item.getString("Rating").equals("null")) {
                        //Log.e("Rating id ", "Updatecall is " + item.getString("Rating"));
                        updateLeadIntent.putExtra("rating", item.getString("Rating"));

                    }

                    if (!item.getString("eid").equals(null) && !item.getString("eid").equals("null")) {

                        getDemoIdFromServer(item.getString("eid"), new DemoID() {
                            @Override
                            public void onSuccess(String value) {
                                try {
                                    Log.e("Demo id is Paras", value + "Sahi aa rahi hai");
                                    updateLeadIntent.putExtra("did", value);
                                    updateLeadIntent.putExtra("eid", item.getString("eid"));
                                    updateLeadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(updateLeadIntent);
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });



                        //mDemoId = getDemoIdFromServer(item.getString("eid"));

                    }

//                    if (!item.getString("eid").equals(null) && !item.getString("eid").equals("null")) {
//                        Log.e("EngID id Paras", "Updatecall is " + item.getString("eid"));
//
//                        //getDemoIdFromServer(item.getString("eid"));
//                    }
//
//                    if (!item.getString("managerid").equals(null) && !item.getString("managerid").equals("null")) {
//                        Log.e("managerid id ", "Updatecall is " + item.getString("managerid"));
//                        updateLeadIntent.putExtra("managerId", item.getString("managerid"));
//
//
//                    }
                    uploadFilesUpdateLead(item.getString("eid"));

                    //Log.e("Going to update","Updating");

                    //}
                } catch (Exception e) {
                    Log.e("Exception update",e.getMessage());

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Response Lead", "Does Not Exist");
            }
        });
    }

    private void getDemoIdFromServer(String enqId,final DemoID callbacks)
    {

        ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);
        Call<JsonElement> call = apiEndpointInterface.mGetDemoId(enqId);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("URL", "=====" + response.raw().request().url());
                Log.e("Demo ID", response.body().toString());


                //UsefullData.Log("User_Response    " + response.body().toString())
                try {

                    callbacks.onSuccess(response.body().toString());
//                    for ( int i = 0; i< array.length() ;i++) {
                    //JSONObject item = array.getJSONObject(0);
                    //}
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Response Lead", "Does Not Exist");
            }
        });
        //Log.e("Demo ID Paras", ""+mDemoId);


    }


    public void uploadFilesUpdateLead(String enqID)
    {
        //Log.e("EngID id uploading", " " +enqID);
        SaveData objSaveData = new SaveData(this);

        String syncState = objSaveData.getString("mSyncState");
        // Log.e("Sync state", "" + syncState);
        if (syncState.equals("0")) {
            if (isConnectedWifi(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);
            } else {
                // Log.e("FilePath", "" + fileName);
            }
        } else if (syncState.equals("2")) {
            if (isConnectedMobile(getApplicationContext())) {

                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);

            } else {
                //  Log.e("FilePath", "" + fileName);
            }
        } else {
            if (isConnected(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);

            } else {
                // Log.e("FilePath", "" + fileName);
            }

        }
    }



    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @param
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    private void uploadPendingData() {


    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
