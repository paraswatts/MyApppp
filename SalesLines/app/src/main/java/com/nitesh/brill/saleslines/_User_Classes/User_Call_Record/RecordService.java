/*
 *  Copyright 2012 Kobi Krasnoff
 *
 * This file is part of Call recorder For Android.

    Call recorder For Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Call recorder For Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Call recorder For Android.  If not, see <http://www.gnu.org/licenses/>
 */
package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Authenticate.Login_Activity;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity;
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordService extends Service {
    static String mAudioFileName;
    static File mAudiofile;
    private static String type;
    AudioDbHelper audioDbHelper;
    Audio audio;
    boolean option = false;
    private MediaRecorder recorder = null;
    private String phoneNumber = null;
    private SaveData objSaveData;
    private String fileName;
    private boolean onCall = false;
    private boolean recording = false;
    private boolean silentMode = false;
    private boolean onForeground = false;
    private static String mDemoId = null;
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @param
     * @return
     */
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        objSaveData = new SaveData(this);

//        SaveData saveData = new SaveData(this);
//        option = saveData.getBoolean("record");
//        Log.e("save data yes or no",""+ saveData.getBoolean("record"));
//        if(option)
//        {
//            saveData.saveBoolean("record",false);
//            Log.e("I am here ",""+option);
//            setSharedPreferences(false);
//
//        }


       // Log.d("", "RecordService onStartCommand");
        if (intent != null) {
            int commandType = intent.getIntExtra("commandType", 0);
            if (commandType != 0) {
                if (commandType == Constants.RECORDING_ENABLED) {
                   // Log.e(Constants.TAG, "RecordService RECORDING_ENABLED");
                    silentMode = intent.getBooleanExtra("silentMode", true);
                    //!silentMode &&
                    if (phoneNumber != null && onCall
                            && !recording)
                        commandType = Constants.STATE_START_RECORDING;

                } else if (commandType == Constants.RECORDING_DISABLED) {
                   // Log.e(Constants.TAG, "RecordService RECORDING_DISABLED");
                    silentMode = intent.getBooleanExtra("silentMode", true);
                    if (onCall && phoneNumber != null && recording)
                        commandType = Constants.STATE_START_RECORDING;
                }

                if (commandType == Constants.STATE_INCOMING_NUMBER) {
                    Log.e(Constants.TAG, "RecordService STATE_INCOMING_NUMBER");
                    startService();
//
              //      Log.e(Constants.TAG, "Phone check kar " +intent.getStringExtra("phoneNumber"));


                    if (phoneNumber == null)
                        phoneNumber = intent.getStringExtra("phoneNumber");
                    if (phoneNumber.startsWith("+91")) {
                        phoneNumber = phoneNumber.substring(3);
                        objSaveData.save("MobileNumber", phoneNumber);
                    } else if (phoneNumber.startsWith("0")) {
                        phoneNumber = phoneNumber.substring(1);
                        objSaveData.save("MobileNumber", phoneNumber);
                    } else {
                        objSaveData.save("MobileNumber", phoneNumber);
                    }

                    //silentMode = intent.getBooleanExtra("silentMode", true);

                    //Intent customDialog = new Intent(this,CustomDialog.class);
                    //customDialog.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    //this.startActivity(customDialog);
                } else if (commandType == Constants.STATE_CALL_START) {
                    objSaveData.saveBoolean("onCall",true);
                    Log.e(Constants.TAG, "RecordService STATE_CALL_START");
                    onCall = true;
                   // Log.e(Constants.TAG, "Phone check kar dujji wari  " +objSaveData.getString("phoneNumber"));
                    phoneNumber=objSaveData.getString("phoneNumber");
                    //Log.e(Constants.TAG, "RecordService" +silentMode +phoneNumber+onCall);
                    //!silentMode &&
                    if (phoneNumber != null && onCall
                            && !recording) {
                        startService();
                        startRecording();
                    }
                } else if (commandType == Constants.STATE_CALL_END) {
                    Log.e(Constants.TAG, "RecordService STATE_CALL_END");
                    onCall = false;
                    phoneNumber = null;
                    objSaveData.saveBoolean("onCall",false);


                    stopAndReleaseRecorder();
                    recording = false;


                    stopService();

                } else if (commandType == Constants.STATE_START_RECORDING) {
                    Log.e(Constants.TAG, "RecordService STATE_START_RECORDING");
                    //!silentMode &&
                    if (phoneNumber != null && onCall) {
                        startService();
                        startRecording();
                    }
                } else if (commandType == Constants.STATE_STOP_RECORDING) {
                    Log.e(Constants.TAG, "RecordService STATE_STOP_RECORDING");

                    stopAndReleaseRecorder();
                    recording = false;


                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * in case it is impossible to record
     */
    private void terminateAndEraseFile() {
        Log.d(Constants.TAG, "RecordService terminateAndEraseFile");
        stopAndReleaseRecorder();
        recording = false;
        deleteFile();
    }

    private void stopService() {
        Log.d(Constants.TAG, "RecordService stopService");
        stopForeground(true);
        onForeground = false;
        this.stopSelf();
    }

    private void deleteFile() {
        Log.d(Constants.TAG, "RecordService deleteFile");
        FileHelper.deleteFile(fileName);
        fileName = null;
    }

    private void stopAndReleaseRecorder() {


        Log.e("I am in stop ", "Recorder");
        if (recorder == null) {
            Log.e("I am in stop ", "Recorder null");
            return;
        }

        Log.d(Constants.TAG, "RecordService stopAndReleaseRecorder");
        boolean recorderStopped = false;
        boolean exception = false;

        try {
            recorder.stop();
            recorderStopped = true;
        } catch (IllegalStateException e) {
            Log.e(Constants.TAG, "IllegalStateException");
            e.printStackTrace();
            exception = true;
        } catch (RuntimeException e) {
            Log.e(Constants.TAG, "RuntimeException");
            exception = true;
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }
        try {
            recorder.reset();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }
        try {
            recorder.release();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }


        recorder = null;

        if (exception) {
            Log.e("exception", "" + exception);
            deleteFile();
        }


        if (recorderStopped) {
            Log.e("I am in stop ", "recorderStopped");
            String phoneNumber = objSaveData.getString("MobileNumber");
            if (phoneNumber.matches("[0-9]+")) {
                if (objSaveData.getString("role_id").equals("5")) {
                    saveToDatabaseActivity();
                    Log.e("Phone matches", phoneNumber);
                    if(isConnected(getBaseContext())){
                        checkLeadExistOrNot(phoneNumber);
                    }
                }
                else{
                    File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
                            for (File f : file.listFiles()) {
                                if (f.getName().startsWith(objSaveData.getString("MobileNumber"))) {
                                  //  Log.e("File delete karan laga", f.getAbsolutePath() + "");
                                    f.delete();
                                }
                            }
                }


//                else{
//                    saveToDatabaseActivity();
//                }

            }

            Log.e("Phone Number call", phoneNumber);
            String syncState = objSaveData.getString("mSyncState");
            Log.e("Sync state", "" + syncState);

        }




    }


    private void checkLeadExistOrNot(final String phoneNumber) {
      //  Log.d("checkLeadExistOrNot", "==checkLeadExistOrNot===");
        ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);

        Call<JsonElement> call = apiEndpointInterface.mCheckAddUpdateLead("" + phoneNumber, objSaveData.getString("client_id"), objSaveData.getString("user_id"));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
               Log.d("URL", "=====" + response.raw().request().url());
                Log.e("=============", response.body().toString());
                String mExist = response.body().toString();
                try {
                    if (mExist.equals("0")) {
                        //    Log.e("Lead", "does not exist");

//                        if (objSaveData.getString("role_id").equals("5")) {
//                            if (objSaveData.getString(objSaveData.getString("MobileNumber")).equals("yes")) {
//                             //   Log.e("Deleting files", "Else case");
//                                File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//                                final File[] files = file.listFiles();
//                                for (File f : file.listFiles()) {
//                                    if (f.getName().startsWith(objSaveData.getString("MobileNumber"))) {
//                                     //   Log.e("File delete karan laga", f.getAbsolutePath() + "");
//                                        f.delete();
//                                    }
//                                }
//
//
//                                objSaveData.remove("MobileNumber");
//                                if (objSaveData.getString("MobileNumber") != null) {
//                                  //  Log.e("Key Removed", objSaveData.getString("MobileNumber"));
//                                }
//                            } else {
                        if(!objSaveData.getBoolean(phoneNumber)) {
                            Intent addLeadYesOrNo = new Intent(RecordService.this, CustomDialog.class);
                            addLeadYesOrNo.putExtra("phoneNumber", phoneNumber);
                            //addLeadYesOrNo.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            addLeadYesOrNo.putExtra("leadExist", "no");
                            addLeadYesOrNo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(addLeadYesOrNo);
                        }
//                            }
//                        } else {
//
//                         //   Log.e("Deleting files", "Else case");
//                            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//
//                            final File[] files = file.listFiles();
//                            for (File f : file.listFiles()) {
//                                if (f.getName().startsWith(objSaveData.getString("MobileNumber"))) {
//                                  //  Log.e("File delete karan laga", f.getAbsolutePath() + "");
//                                    f.delete();
//                                }
//                            }
//                            objSaveData.remove("MobileNumber");
//                            if (objSaveData.getString("MobileNumber") != null) {
//                               // Log.e("Key Removed", objSaveData.getString("MobileNumber"));
//                            }


//                    }
                    }
                    else {

                            //  Log.e("", "Lead Already Exists");
                            String phoneNumber = objSaveData.getString("MobileNumber");
//                            if (objSaveData.getString("role_id").equals("5")) {
                                //  Log.e("Update Lead", "From user");
                                getLeadEnquiryDetails(phoneNumber);
                            //}


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

    public void uploadFilesUpdateLead(String enqID)
    {
        //Log.e("EngID id uploading", " " +enqID);

        String syncState = objSaveData.getString("mSyncState");
       // Log.e("Sync state", "" + syncState);
        if (syncState.equals("0")) {
            if (isConnectedWifi(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);
            } else {
               // Log.e("FilePath", "" + fileName);
                //saveToDatabaseActivity();
            }
        } else if (syncState.equals("2")) {
            if (isConnectedMobile(getApplicationContext())) {

                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);

            } else {
              //  Log.e("FilePath", "" + fileName);
                //saveToDatabaseActivity();
            }
        } else {
            if (isConnected(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), UploadService.class);
                intent.putExtra("enqID",enqID);
                startService(intent);

            } else {
               // Log.e("FilePath", "" + fileName);
                //saveToDatabaseActivity();
            }

        }
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


    private void getLeadEnquiryDetails(String phoneNumber) {

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

                    final JSONObject item = array.getJSONObject(0);

                    final Intent  updateLeadIntent = new Intent(getApplicationContext(), User_Home_Activity.class);


                    updateLeadIntent.putExtra("addLeadFromCall", "updateLead");
                    if (!item.getString("LeadId").equals(null) && !item.getString("LeadId").equals("null")) {
                        //Log.e("Lead id ", "Updatecall is " + item.getString("LeadId"));
                        updateLeadIntent.putExtra("leadId", item.getString("LeadId"));
                    }

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
                    //uploadFilesUpdateLead(item.getString("eid"));

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


    private void saveToDatabaseActivity() {
        String fileName;
        audio =  new Audio();
        audioDbHelper = new AudioDbHelper(this);
        Log.e("Saving audio","File to database");
        try {
            //fileName = FileHelper.getFilename(objSaveData.getString("phoneNumber"));
            long fileSizeInBytes = mAudiofile.length();
            long fileSizeInKB = fileSizeInBytes / 1024;

            fileName = mAudiofile.getAbsolutePath();
            String time = new SimpleDateFormat("dd/MM/yyyy hh:mm:a").format(Calendar.getInstance().getTime());
            audio.setAudioDate(time);
            audio.setAudioMobile(objSaveData.getString("MobileNumber"));
            audio.setSize(String.valueOf(fileSizeInKB));
            audio.setLeadId(objSaveData.getString("user_id"));
            audio.setAudioRecording(fileName);

            audioDbHelper.addAudio(audio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "RecordService onDestroy");
        stopAndReleaseRecorder();
        stopService();
        super.onDestroy();
    }

    private void startRecording() {

        String out = new SimpleDateFormat("dd-MM-yyyyhh-mm").format(new Date());
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/SalesLineCallRecordings");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        String file_name = "call";
        try {
            objSaveData.getString("MobileNumber");

            mAudioFileName = file_name + "_" + out;
            mAudiofile = new File(sampleDir, objSaveData.getString("MobileNumber") + file_name + "-" + out + ".amr");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("save exception", "error" + e);
        }


        boolean exception = false;
        recorder = new MediaRecorder();

        try {
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            fileName = mAudiofile.getAbsolutePath();
            recorder.setOutputFile(mAudiofile.getAbsolutePath());

            OnErrorListener errorListener = new OnErrorListener() {
                public void onError(MediaRecorder arg0, int arg1, int arg2) {
                    Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
                    terminateAndEraseFile();
                }
            };
            recorder.setOnErrorListener(errorListener);

            OnInfoListener infoListener = new OnInfoListener() {
                public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
                    Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
                    terminateAndEraseFile();
                }
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            recorder.start();
            recording = true;
            Log.d(Constants.TAG, "RecordService recorderStarted");
        } catch (IllegalStateException e) {
            Log.e(Constants.TAG, "IllegalStateException");
            e.printStackTrace();
            exception = true;
        } catch (IOException e) {
            Log.e(Constants.TAG, "IOException");
            e.printStackTrace();
            exception = true;
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
            exception = true;
        }

        if (exception) {
            terminateAndEraseFile();
        }

        if (recording) {
            Toast toast = Toast.makeText(this,
                    this.getString(R.string.receiver_start_call),
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this,
                    this.getString(R.string.record_impossible),
                    Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void setSharedPreferences(boolean silentMode) {
        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", silentMode);
        editor.commit();

        Intent myIntent = new Intent(this, RecordService.class);
        myIntent.putExtra("commandType",
                silentMode ? Constants.RECORDING_DISABLED
                        : Constants.RECORDING_ENABLED);
        myIntent.putExtra("silentMode", silentMode);
        startService(myIntent);
        startRecording();

    }

    private void startService() {
        if (!onForeground) {
            Log.d(Constants.TAG, "RecordService startService");
            PendingIntent pendingIntent;
            Intent intent = null;
            if(objSaveData.getBoolean("loginState")) {
                if(objSaveData.getString("role_id").equals("5")) {
                    intent = new Intent(this, User_Home_Activity.class);
                }
                if(objSaveData.getString("role_id").equals("4")) {
                    intent = new Intent(this, Manager_Home_Activity.class);
                }
                if(objSaveData.getString("role_id").equals("3")) {
                    intent = new Intent(this, ASM_Home_Activity.class);
                }
                if(objSaveData.getString("role_id").equals("2")) {
                    intent = new Intent(this, AGM_Home_Activity.class);
                }
                if(objSaveData.getString("role_id").equals("1")) {
                    intent = new Intent(this, GM_Home_Activity.class);
                }
                // intent.setAction(Intent.ACTION_VIEW);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            }
            else{
                intent = new Intent(this,Login_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            pendingIntent = PendingIntent.getActivity(
                    getBaseContext(), 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(
                    getBaseContext())
                    .setContentTitle(
                            this.getString(R.string.notification_title))
                    .setTicker(this.getString(R.string.notification_ticker))
                    .setContentText(this.getString(R.string.notification_text))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pendingIntent).setOngoing(true)
                    .getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            startForeground(1337, notification);
            onForeground = true;
        }
    }
}


///*
// *  Copyright 2012 Kobi Krasnoff
// *
// * This file is part of Call recorder For Android.
//
//    Call recorder For Android is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Call recorder For Android is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Call recorder For Android.  If not, see <http://www.gnu.org/licenses/>
// */
//package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;
//
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.media.AudioManager;
//import android.media.MediaRecorder;
//import android.media.MediaRecorder.OnErrorListener;
//import android.media.MediaRecorder.OnInfoListener;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.nitesh.brill.saleslines.Common.SaveData;
//import com.nitesh.brill.saleslines.R;
//import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
//import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
//
//import java.io.IOException;
//
//
//public class RecordService extends Service {
//    AudioDbHelper audioDbHelper;
//    Audio audio;
//    private static final String YES_ACTION = "YES_ACTION";
//    private static final String NO_ACTION = "NO_ACTION";
//    private MediaRecorder recorder = null;
//    private String phoneNumber = null;
//    private static String type;
//
//    private static String fileName;
//    private boolean onCall = false;
//    private boolean recording = false;
//    private boolean silentMode = false;
//    private boolean onForeground = false;
//    static int commandType;
//    static boolean FLAG = true;
//    private SaveData objSaveData;
//    boolean option = false;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
//        SaveData saveData = new SaveData(this);
//        option = saveData.getBoolean("record");
//        Log.e("save data yes or no",""+ saveData.getBoolean("record"));
//        if(option)
//        {
//            Log.e("I am here ","value is true");
//            setSharedPreferences(false);
//
//        }
//
//
//        Log.d(Constants.TAG, "RecordService onStartCommand");
//        if (intent != null) {
//            int commandType = intent.getIntExtra("commandType", 0);
//            if (commandType != 0) {
//                if (commandType == Constants.RECORDING_ENABLED) {
//                    Log.d(Constants.TAG, "RecordService RECORDING_ENABLED");
//                    silentMode = intent.getBooleanExtra("silentMode", true);
//                    if (!silentMode && phoneNumber != null && onCall
//                            && !recording)
//                        commandType = Constants.STATE_START_RECORDING;
//
//                } else if (commandType == Constants.RECORDING_DISABLED) {
//                    Log.d(Constants.TAG, "RecordService RECORDING_DISABLED");
//                    silentMode = intent.getBooleanExtra("silentMode", true);
//                    if (onCall && phoneNumber != null && recording)
//                        commandType = Constants.STATE_STOP_RECORDING;
//                }
//
//                if (commandType == Constants.STATE_INCOMING_NUMBER) {
//                    Log.d(Constants.TAG, "RecordService STATE_INCOMING_NUMBER");
//                    startService();
//                    if (phoneNumber == null)
//                        phoneNumber = intent.getStringExtra("phoneNumber");
//
//                    silentMode = intent.getBooleanExtra("silentMode", true);
//
//                    Intent customDialog = new Intent(this,CustomDialog.class);
//
//                    this.startActivity(customDialog);
//                } else if (commandType == Constants.STATE_CALL_START) {
//                    Log.d(Constants.TAG, "RecordService STATE_CALL_START");
//                    onCall = true;
//
//                    if (!silentMode && phoneNumber != null && onCall
//                            && !recording) {
//                        startService();
//                        startRecording();
//                    }
//                } else if (commandType == Constants.STATE_CALL_END) {
//                    Log.d(Constants.TAG, "RecordService STATE_CALL_END");
//                    onCall = false;
//                    phoneNumber = null;
//
//
//                    stopAndReleaseRecorder();
//                    recording = false;
//
//
//                    stopService();
//                } else if (commandType == Constants.STATE_START_RECORDING) {
//                    Log.d(Constants.TAG, "RecordService STATE_START_RECORDING");
//                    if (!silentMode && phoneNumber != null && onCall) {
//                        startService();
//                        startRecording();
//                    }
//                } else if (commandType == Constants.STATE_STOP_RECORDING) {
//                    Log.d(Constants.TAG, "RecordService STATE_STOP_RECORDING");
//
//                    stopAndReleaseRecorder();
//                    recording = false;
//
//
//                }
//            }
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//
//    /**
//     * in case it is impossible to record
//     */
//    private void terminateAndEraseFile() {
//        Log.d(Constants.TAG, "RecordService terminateAndEraseFile");
//        stopAndReleaseRecorder();
//        recording = false;
//        deleteFile();
//    }
//
//    private void stopService() {
//        SaveData saveData;
//        saveData = new SaveData(this);
//
//        saveData.saveBoolean("record",false);
//
//        Log.d(Constants.TAG, "RecordService stopService");
//        stopForeground(true);
//        onForeground = false;
//        this.stopSelf();
//    }
//
//    private void deleteFile() {
//        Log.d(Constants.TAG, "RecordService deleteFile");
//        FileHelper.deleteFile(fileName);
//        fileName = null;
//    }
//
//    private void stopAndReleaseRecorder() {
//
//        if (recorder == null)
//            return;
//        Log.d(Constants.TAG, "RecordService stopAndReleaseRecorder");
//        boolean recorderStopped = false;
//        boolean exception = false;
//
//        try {
//            recorder.stop();
//            recorderStopped = true;
//        } catch (IllegalStateException e) {
//            Log.e(Constants.TAG, "IllegalStateException");
//            e.printStackTrace();
//            exception = true;
//        } catch (RuntimeException e) {
//            Log.e(Constants.TAG, "RuntimeException");
//            exception = true;
//        } catch (Exception e) {
//            Log.e(Constants.TAG, "Exception");
//            e.printStackTrace();
//            exception = true;
//        }
//        try {
//            recorder.reset();
//        } catch (Exception e) {
//            Log.e(Constants.TAG, "Exception");
//            e.printStackTrace();
//            exception = true;
//        }
//        try {
//            recorder.release();
//        } catch (Exception e) {
//            Log.e(Constants.TAG, "Exception");
//            e.printStackTrace();
//            exception = true;
//        }
//
//
//        recorder = null;
//
//        if (exception) {
//            Log.e("exception", "" + exception);
//            deleteFile();
//        }
//        if (recorderStopped) {
//
//
//            ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//            Log.e("Network", networkInfo + "");
//            if (networkInfo != null && networkInfo.isConnected()) {
//                Log.e("Network is up", "" + networkInfo.isConnected());
//                type = "yes";
//                Log.e("stopped", "");
//                uploadActivity();
//
//            } else {
//                Log.e("FilePath", "" + fileName);
//                saveToDatabaseActivity();
//            }
//            Toast toast = Toast.makeText(this,
//                    this.getString(R.string.receiver_end_call),
//                    Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//
//    }
//
//
//    private void uploadActivity() {
//        String fileName = null;
//        Log.e("========", fileName + "=======here youi are======");
//
//        try {
//            fileName = FileHelper.getFilename(objSaveData.getString("phoneNumber"));
//
//            Log.e("", "File path paras" + fileName);
//            //new UploadAsync().execute(mFilePath);
//            type = "yes";
//            Intent intent = new Intent(this, UploadService.class);
//            intent.putExtra("type", type);
//            intent.putExtra("audioFile", fileName);
//            this.startService(intent);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//
//    }
//
//
//    private void saveToDatabaseActivity() {
//        String fileName;
//        try {
//            fileName = FileHelper.getFilename(objSaveData.getString("phoneNumber"));
//
//            audio.setAudioRecording(fileName);
//
//            audioDbHelper.addAudio(audio);
//
//
//        } catch (Exception e) {
//
//        }
//
//
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d(Constants.TAG, "RecordService onDestroy");
//
//        stopAndReleaseRecorder();
//        stopService();
//        super.onDestroy();
//    }
//
//    private void startRecording() {
//
//
//        SharedPreferences preferences = getSharedPreferences("NotificationActions", 0);
//        Boolean notificationAction = preferences.getBoolean("YES_ACTION", false);
//        Log.e("Notification action", "" + notificationAction);
//
//        if (notificationAction) {
//            boolean exception = false;
//            recorder = new MediaRecorder();
//
//            try {
//                AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
//
//                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                fileName = FileHelper.getFilename(phoneNumber);
//                recorder.setOutputFile(fileName);
//
//                OnErrorListener errorListener = new OnErrorListener() {
//                    public void onError(MediaRecorder arg0, int arg1, int arg2) {
//                        Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
//                        terminateAndEraseFile();
//                    }
//                };
//                recorder.setOnErrorListener(errorListener);
//
//                OnInfoListener infoListener = new OnInfoListener() {
//                    public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
//                        Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
//                        terminateAndEraseFile();
//                    }
//                };
//                recorder.setOnInfoListener(infoListener);
//
//                recorder.prepare();
//                // Sometimes prepare takes some time to complete
//                Thread.sleep(2000);
//                recorder.start();
//                recording = true;
//                Log.d(Constants.TAG, "RecordService recorderStarted");
//            } catch (IllegalStateException e) {
//                Log.e(Constants.TAG, "IllegalStateException");
//                e.printStackTrace();
//                exception = true;
//            } catch (IOException e) {
//                Log.e(Constants.TAG, "IOException");
//                e.printStackTrace();
//                exception = true;
//            } catch (Exception e) {
//                Log.e(Constants.TAG, "Exception");
//                e.printStackTrace();
//                exception = true;
//            }
//
//            if (exception) {
//                terminateAndEraseFile();
//            }
//
//            if (recording) {
//                Toast toast = Toast.makeText(this,
//                        this.getString(R.string.receiver_start_call),
//                        Toast.LENGTH_SHORT);
//                toast.show();
//            } else {
//                Toast toast = Toast.makeText(this,
//                        this.getString(R.string.record_impossible),
//                        Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }
//    }
//
////
////    private void startRecording(Intent intent) {
////        Log.d(Constants.TAG, "RecordService startRecording");
////        boolean exception = false;
////
////        try {
////            recorder = new MediaRecorder();
////
////            //	recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
////            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
////            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
////            fileName = FileHelper.getFilename(phoneNumber);
////            recorder.setOutputFile(fileName);
////
////            OnErrorListener errorListener = new OnErrorListener() {
////                public void onError(MediaRecorder arg0, int arg1, int arg2) {
////                    Log.e(Constants.TAG, "OnErrorListener " + arg1 + "," + arg2);
////                    terminateAndEraseFile();
////                }
////            };
////            recorder.setOnErrorListener(errorListener);
////
////            OnInfoListener infoListener = new OnInfoListener() {
////                public void onInfo(MediaRecorder arg0, int arg1, int arg2) {
////                    Log.e(Constants.TAG, "OnInfoListener " + arg1 + "," + arg2);
////                    terminateAndEraseFile();
////                }
////            };
////            recorder.setOnInfoListener(infoListener);
////
////            recorder.prepare();
////            // Sometimes prepare takes some time to complete
////            Thread.sleep(2000);
////            recorder.start();
////            recording = true;
////            Log.d(Constants.TAG, "RecordService recorderStarted");
////        } catch (Exception e) {
////
////            exception = true;
////        }
////
////		if (exception) {
////			terminateAndEraseFile();
////		}
////
////        if (recording) {
////            Toast toast = Toast.makeText(this,
////                    this.getString(R.string.receiver_start_call),
////                    Toast.LENGTH_SHORT);
////            toast.show();
////        } else {
////            Toast toast = Toast.makeText(this,
////                    this.getString(R.string.record_impossible),
////                    Toast.LENGTH_LONG);
////            toast.show();
////        }
////    }
//    private void startService() {
//        if (!onForeground) {
//            Log.d(Constants.TAG, "RecordService startService");
//            Intent intent = new Intent(this, User_Home_Activity.class);
//            // intent.setAction(Intent.ACTION_VIEW);
//            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            PendingIntent pendingIntent = PendingIntent.getActivity(
//                    getBaseContext(), 0, intent, 0);
//
//            Notification notification = new NotificationCompat.Builder(
//                    getBaseContext())
//                    .setContentTitle(
//                            this.getString(R.string.notification_title))
//                    .setTicker(this.getString(R.string.notification_ticker))
//                    .setContentText(this.getString(R.string.notification_text))
//                    .setSmallIcon(R.drawable.ic_launcher)
//                    .setContentIntent(pendingIntent).setOngoing(true)
//                    .getNotification();
//
//            notification.flags = Notification.FLAG_NO_CLEAR;
//
//            startForeground(1337, notification);
//            onForeground = true;
//        }
//    }
//
//    private void setSharedPreferences(boolean silentMode) {
//        SharedPreferences settings = this.getSharedPreferences(
//                Constants.LISTEN_ENABLED, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("silentMode", silentMode);
//        editor.commit();
//
//        Intent myIntent = new Intent(this, RecordService.class);
//        myIntent.putExtra("commandType",
//                silentMode ? Constants.RECORDING_DISABLED
//                        : Constants.RECORDING_ENABLED);
//        myIntent.putExtra("silentMode", silentMode);
//        startService(myIntent);
//        startRecording();
//
//    }
//
//}
