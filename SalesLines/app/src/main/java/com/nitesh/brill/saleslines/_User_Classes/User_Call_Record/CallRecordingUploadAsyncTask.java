package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
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
 * Created by Paras-Android on 28-09-2017.
 */

public class CallRecordingUploadAsyncTask extends AsyncTask<Void, Void, Void> {
    private SaveData objSaveData;
    String roleType;
    Context ctx;
    RetrofitAPI retrofitAPI;
    public CallRecordingUploadAsyncTask(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.e("I am here","In call recording Async Task");
        PendingIntent pendingIntent;
        AudioDbHelper userDbHelper = new AudioDbHelper(ctx);
        SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();

        objSaveData = new SaveData(ctx);

        String selection = "audio_leadid=?";

        String lead_id = objSaveData.getString("user_id");
        String[] selectionArgs = {lead_id};

        Log.e("String selection",""+selection+" "+lead_id);

        Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_AUDIO, //Table to query
                null, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //filter by row groups
                null, null, null);

        Log.e("Count call recordings=",""+query.getCount()+"query "+query.toString());
        try
        {
            while(query.moveToNext()) {
                if(Long.parseLong(query.getString(3))<5){
                    File file = new File(query.getString(1));
                    file.getAbsolutePath();
                    Log.e("File Path Delete", file.getAbsolutePath() + "");
                    file.delete();

                    deleteRecords(query.getString(1));
                }
                else {
                    checkLeadExistOrNot(query.getString(4), query.getString(1));
                }

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    private void checkLeadExistOrNot(final String phoneNumber,final String filepath) {
        //  Log.d("checkLeadExistOrNot", "==checkLeadExistOrNot===");
        final SaveData objSaveData = new SaveData(ctx);
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

                            File file = new File(filepath);
                            file.getAbsolutePath();
                            Log.e("File Path Delete", file.getAbsolutePath() + "");
                            file.delete();

                                deleteRecords(filepath);


                    } else {

                        //  Log.e("", "Lead Already Exists");
                        String phoneNumber = objSaveData.getString("MobileNumber");
                            //  Log.e("Update Lead", "From user");
                            getLeadEnquiryDetails(phoneNumber);



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

    public void deleteRecords(String filepath) {

        AudioDbHelper audioDbHelper = new AudioDbHelper(ctx);
        String query = "Delete from " + AudioContract.AudioEntry.TABLE_AUDIO + "where filename = " + filepath;
        SQLiteDatabase sqLiteDatabase = audioDbHelper.getWritableDatabase();

        int count = sqLiteDatabase.rawQuery(query, null).getCount();
        Log.e("Count Delete", "" + count);
        try {

            if (count > 0) {
                sqLiteDatabase.execSQL(query);
            }
        } finally {


            sqLiteDatabase.close();

        }

    }

    private void getLeadEnquiryDetails(String phoneNumber) {

        SaveData objSaveData = new SaveData(ctx);
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


                    if (!item.getString("eid").equals(null) && !item.getString("eid").equals("null")) {

                        uploadFilesUpdateLead(item.getString("eid"));
                    }

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




    public void uploadFilesUpdateLead(String enqID)
    {
        //Log.e("EngID id uploading", " " +enqID);
        SaveData objSaveData = new SaveData(ctx);

        String syncState = objSaveData.getString("mSyncState");
        // Log.e("Sync state", "" + syncState);
        if (syncState.equals("0")) {
            if (isConnectedWifi(ctx)) {
                Intent intent = new Intent(ctx, UploadService.class);
                intent.putExtra("enqID",enqID);
                ctx.startService(intent);
            }
        } else if (syncState.equals("2")) {
            if (isConnectedMobile(ctx)) {

                Intent intent = new Intent(ctx, UploadService.class);
                intent.putExtra("enqID",enqID);
                ctx.startService(intent);

            }
        } else {
            if (isConnected(ctx)) {
                Intent intent = new Intent(ctx, UploadService.class);
                intent.putExtra("enqID",enqID);
                ctx.startService(intent);

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






}
