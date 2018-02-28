package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paras-Android on 28-09-2017.
 */

public class CoordinatesUploadAsyncTask extends AsyncTask<Void, Void, Void> {
    private SaveData objSaveData;
    String roleType;
    Context ctx;
    RetrofitAPI retrofitAPI;
    public CoordinatesUploadAsyncTask(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.e("I am here","In coordinates Async Task");
        PendingIntent pendingIntent;
        AudioDbHelper userDbHelper = new AudioDbHelper(ctx);
        SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();

        objSaveData = new SaveData(ctx);

        String selection = "user_email=?";

        String email = objSaveData.getString("LoginId");
        String[] selectionArgs = {email};

        Log.e("String selection",""+selection+" "+email);

        Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_USER_COORDINATES, //Table to query
                null, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //filter by row groups
                null, null, null);

        Log.e("Count =",""+query.getCount()+"query "+query.toString());
        try
        {
            while(query.moveToNext()) {


                String emailId = query.getString(3);
                String uploadedOrNot = query.getString(5);


//                if (uploadedOrNot.equals("no")) {
//                    ContentValues content_Values = new ContentValues();
//                    content_Values.put(AudioContract.AudioEntry.COLUMN_USER_COORDINATES_UPLOADED, "yes");
//                    sqLiteDatabase.update(AudioContract.AudioEntry.TABLE_USER_COORDINATES,
//                            content_Values,
//                            AudioContract.AudioEntry.COLUMN_USER_EMAIL + "= ?",
//                            new String[]{email});
                    saveLocation(Double.valueOf(query.getString(1)),Double.valueOf(query.getString(2)),query.getInt(0),query.getString(4),query.getString(6));
                //}
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    public void deleteCoordinates(int id) {


        AudioDbHelper audioDbHelper = new AudioDbHelper(ctx);
        String query = "Delete from " + AudioContract.AudioEntry.TABLE_USER_COORDINATES + " where _id = " + id;
        Log.e("Delete Query", "" + query);
        SQLiteDatabase sqLiteDatabase = audioDbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL(query);

        sqLiteDatabase.close();


    }

    public void saveLocation(Double latitude,Double longitude,final int id,String time,String type){
        objSaveData = new SaveData(ctx);


            try {
                String updateType;
                if(type.equals("insert"))
                    updateType = "0";
                else
                    updateType = "1";
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String date = df.format(c.getTime());
                Log.e("Coordinates Uploading", latitude + " " + longitude+"Time is "+time +"===="+date);

                Log.e("Date time ",date);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserId",Integer.parseInt(objSaveData.getString("user_id")));
                jsonObject.put("ManagerId",Integer.parseInt(objSaveData.getString("manager_id")));
                jsonObject.put("ClientId",Integer.parseInt(objSaveData.getString("client_id")));
                jsonObject.put("Latitude",latitude);
                jsonObject.put("Longitude",longitude);
                jsonObject.put("LocationDatetime",time);
                jsonObject.put("UpdateType",Integer.parseInt(updateType));
                Log.e("=", "================JSOn ObJeCt" + jsonObject.toString());


                retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);

                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<JsonElement> call = retrofitAPI.mSaveUserLocation(body);

                call.enqueue(new Callback<JsonElement>() {

                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        Log.d("URL", "=====" + response.raw().request().url());

                        try {
                            Log.e("=============", response.body().toString());
                            deleteCoordinates(id);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }


                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                });

            }catch (JSONException e){
                e.printStackTrace();
            }


    }





}
