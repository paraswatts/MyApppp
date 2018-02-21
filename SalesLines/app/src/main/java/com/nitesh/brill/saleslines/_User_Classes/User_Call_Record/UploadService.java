package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Common_Files.UsefullData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Sonu Android on 01-06-2017.
 */

public class UploadService extends IntentService {
    private static String type;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private static String str;
    SaveData saveData ;
    public static MediaRecorder mRecorder = new MediaRecorder();
    RetrofitAPI retrofitAPI;
//    public static AudioRecorder audioRecorder = AudioRecorder.getInstance();
//    public static MediaRecorder mRecorder = new MediaRecorder();
    UsefullData usefullData;
    SaveData objSaveData;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    static String filePath;
    Context context;

    public UploadService() {
        super("Download Service");
    }

    public void deleteRecords() {

        AudioDbHelper audioDbHelper = new AudioDbHelper(getApplicationContext());
        String query = "Delete from " + AudioContract.AudioEntry.TABLE_AUDIO + "where filename = " + filePath;
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

    /*  Delete Uploaded Files from the external storage directory*/

    private void deleteFiles(String path) {
        File file = new File(path);
        file.getAbsolutePath();
        Log.e("File Path Delete", file.getAbsolutePath() + "");
        file.delete();
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        usefullData = new UsefullData(this);
        objSaveData = new SaveData(this);
        Log.e("phoneNumber",        objSaveData.getString("MobileNumber"));

        saveData = new SaveData(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("Uploading Call Recordings").setContentText("Uploading in Progress").setSmallIcon(R.drawable.ic_launcher);
        builder.setProgress(0, 0, false);
        notificationManager.notify(1, builder.build());





        File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");

        final File[] files = file.listFiles();


        if (files != null) {

            for (int j = 0; j < files.length; j++) {
                files[j].getAbsolutePath();
                //type = "yes";
                Log.e("File Path Upload " + j, files[j].getAbsolutePath() + "");

                JSONObject jsonObject = new JSONObject();
                String fileName = files[j].getAbsolutePath().substring(files[j].getAbsolutePath().lastIndexOf("/") + 1);
                if (fileName.startsWith(objSaveData.getString("MobileNumber"))) {
                    Log.e("Uploading file","Name start with "+objSaveData.getString("MobileNumber"));
                    final int index = j;

                    try {
                        FileInputStream fis = new FileInputStream(files[j]);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        int read = 0;
                        byte[] buffer = new byte[1024];
                        while (read != -1) {
                            read = fis.read(buffer);
                            if (read != -1)
                                out.write(buffer, 0, read);
                        }
                        out.close();
                        Log.e("File name in ", "upload service" + fileName);


                        byte[] bytes = convert(intent, files[j].getAbsolutePath());

                        String audioBase64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

                        prinTFullLog("Audio Base 64 " + audioBase64String);
                        Log.e("=", "================JSOn ObJeCt" + saveData.getString("enqID")
                                + saveData.getString("leadID")
                                + saveData.getString("userID")
                                + saveData.getString("managerID")
                        );
//saveData.getString("enqID")
                        Log.e("Enq id after upload",intent.getStringExtra("enqID"));

                        jsonObject.put("EnquiryId", intent.getStringExtra("enqID"));
                        jsonObject.put("LeadId", saveData.getString("leadID"));
                        jsonObject.put("UserId", saveData.getString("userID"));
                        jsonObject.put("Role", 5);
                        jsonObject.put("ManagerId", saveData.getString("managerID"));
                        jsonObject.put("EmployeeCode", saveData.getString("userID"));
                        jsonObject.put("ClientId", saveData.getString("clientID"));
                        jsonObject.put("AudioName", fileName);
                        jsonObject.put("AudioContent", ".amr");


                        jsonObject.put("AudioData", audioBase64String);
                        jsonObject.put("CreatedUserId", saveData.getString("userID"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    Log.e("=", "================JSOn ObJeCt" + jsonObject.toString());


                    retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);

                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<JsonElement> call = retrofitAPI.SaveAudioRecord(body);

                    call.enqueue(new Callback<JsonElement>() {

                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            Log.d("URL", "=====" + response.raw().request().url());
                            Log.e("=============", response.body().toString());
//                        if (type.equals("yes")) {
                            try {
                                builder.setContentText("Upload Complete");
                                builder.setProgress(0, 0, false);
                                notificationManager.notify(1, builder.build());

                                notificationManager.cancelAll();
                                Log.e("I am here", "");
                                deleteFiles(files[index].getAbsolutePath());
                                //deleteRecords();


//                        }
//                        if (type.equals("no")) {
//                            Log.e("I am here in second if", "");
//
//                            builder.setContentText("Upload Complete");
//                            builder.setProgress(0, 0, false);
//                            notificationManager.notify(1, builder.build());
//                            notificationManager.cancelAll();
//
//                            deleteFiles();
//                            deleteRecords();
//
//                        }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }


                        public void onFailure(Call<JsonElement> call, Throwable t) {

                        }
                    });


                }
            }
        }





        //String file = intent.getStringExtra("audioFile");

        //Log.e("audio file", file);
        //filePath = file;
        //type = intent.getStringExtra("type");

//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int read = 0;
//            byte[] buffer = new byte[1024];
//            while (read != -1) {
//                read = fis.read(buffer);
//                if (read != -1)
//                    out.write(buffer, 0, read);
//            }
//            out.close();
//
//
//            byte[] bytes = convert(intent);
//
//            String audioBase64String = Base64.encodeToString(bytes, Base64.NO_WRAP);
//
//
//            Log.e("=", "================JSOn ObJeCt"+ saveData.getString("enqID")
//                    + saveData.getString("leadID")
//                    + saveData.getString("userID")
//                    + saveData.getString("managerID")
//            );
//
//
//            jsonObject.put("EnquiryId", saveData.getString("enqID"));
//            jsonObject.put("LeadId", saveData.getString("leadID"));
//            jsonObject.put("UserId", saveData.getString("userID"));
//            jsonObject.put("Role", 5);
//            jsonObject.put("ManagerId", saveData.getString("managerID"));
//            jsonObject.put("EmployeeCode", saveData.getString("userID"));
//            jsonObject.put("ClientId", 1);
//            jsonObject.put("AudioName", intent.getStringExtra("fileName"));
//            jsonObject.put("AudioContent", "Testing Audio content");
//            jsonObject.put("AudioData", audioBase64String);
//            jsonObject.put("CreatedUserId", saveData.getString("userID"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//
//        Log.e("=", "================JSOn ObJeCt"+jsonObject.toString());
//
//        retrofitAPI = APIClient.getClient().create(RetrofitAPI.class);
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
//        Call<JsonElement> call = retrofitAPI.SaveAudioRecord(body);
//
//        call.enqueue(new Callback<JsonElement>() {
//
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                Log.d("URL", "=====" + response.raw().request().url());
//                Log.e("=============", response.body().toString());
//                if (type.equals("yes")) {
//
//                    builder.setContentText("Upload Complete");
//                    builder.setProgress(0, 0, false);
//                    notificationManager.notify(1, builder.build());
//
//                    notificationManager.cancelAll();
//                    Log.e("I am here", "");
//                    deleteFiles();
//
//
//                }
//                if (type.equals("no")) {
//                    Log.e("I am here in second if", "");
//
//                    builder.setContentText("Upload Complete");
//                    builder.setProgress(0, 0, false);
//                    notificationManager.notify(1, builder.build());
//                    notificationManager.cancelAll();
//
//                    deleteFiles();
//                    deleteRecords();
//
//                }
//            }
//
//
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//
//            }
//        });


    }



    private void prinTFullLog(String veryLongString) {



        int maxLogSize = 1000;
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.e("======", veryLongString.substring(start, end));
        }

    }


    public byte[] convert(Intent intent,String file) throws IOException {
        //String file = intent.getStringExtra("audioFile");

//        Log.e("audio file", file);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[2048];

        for (int readNum; (readNum = fis.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }


}
