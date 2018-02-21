package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Sonu Android on 01-06-2017.
 */

public class NetworkReceiver extends android.content.BroadcastReceiver {
    NotificationCompat.Builder builder;
    private static String type;
    static Context mContext;
    static Boolean onlineStatus, getOnlineStatus;
    NotificationManager notificationManager;

    SaveData objSaveData;
    @Override
    public void onReceive(Context context, Intent intent) {
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Uploading Call Recordings").setContentText("Uploading in Progress").setSmallIcon(R.drawable.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, User_Home_Activity.class), 0);
        builder.setContentIntent(pendingIntent);
        mContext = context;
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo network = connMgr.getActiveNetworkInfo();

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        objSaveData = new SaveData(context);

        String syncState = objSaveData.getString("mSyncState");
        Log.e("Sync state",""+syncState);
        if(syncState.equals("0"))
        {
            if(isConnectedWifi(context))
            {
                Log.e("Wifi is connected","");
                type = "yes";
                Log.e("stopped", "");

                File file = new File(Environment.getExternalStorageDirectory() + "/SalesLine/CallRecordings");
                File[] files = file.listFiles();
                if (files != null) {

                    for (int j = 0; j < files.length; j++) {
                        files[j].getAbsolutePath();

                        //type = "yes";
                        Log.e("File Path Upload " + j, files[j].getAbsolutePath() + "");
                    /*
                    Intent intent1 = new Intent();
                    intent1.setAction("com.brillinfosystem.voicerecorder.UploadService");
                    intent1.putExtra("type",type);
                    intent1.putExtra("audioFile",files[j].getPath());
                    context.startService(intent);*/

                        uploadActivityFromFile(files[j].getAbsolutePath());

                    }
                }


            }
            else
            {
                Log.e("Not connected ", "to Wifi" );


            }
        }

        else if (syncState.equals("2"))
        {
            if(isConnectedMobile(context))
            {
                File file = new File(Environment.getExternalStorageDirectory() + "/SalesLine/CallRecordings");
                File[] files = file.listFiles();
                if (files != null) {

                    for (int j = 0; j < files.length; j++) {
                        files[j].getAbsolutePath();

                        //type = "yes";
                        Log.e("File Path Upload " + j, files[j].getAbsolutePath() + "");
                    /*
                    Intent intent1 = new Intent();
                    intent1.setAction("com.brillinfosystem.voicerecorder.UploadService");
                    intent1.putExtra("type",type);
                    intent1.putExtra("audioFile",files[j].getPath());
                    context.startService(intent);*/

                        uploadActivityFromFile(files[j].getAbsolutePath());

                    }
                }


            }
            else
            {
                Log.e("Not connected ", "to internet" );

            }
        }

        else
        {
            if(isConnected(context))
            {
                File file = new File(Environment.getExternalStorageDirectory() + "/SalesLine/CallRecordings");
                File[] files = file.listFiles();
                if (files != null) {

                    for (int j = 0; j < files.length; j++) {
                        files[j].getAbsolutePath();

                        //type = "yes";
                        Log.e("File Path Upload " + j, files[j].getAbsolutePath() + "");
                    /*
                    Intent intent1 = new Intent();
                    intent1.setAction("com.brillinfosystem.voicerecorder.UploadService");
                    intent1.putExtra("type",type);
                    intent1.putExtra("audioFile",files[j].getPath());
                    context.startService(intent);*/

                        uploadActivityFromFile(files[j].getAbsolutePath());

                    }
                }


            }
            else
            {
                Log.e("Not connected", " to mobile data");
            }

        }






//        if (network != null && network.isConnected() )  {
//            //uploadActivity(true);
//            // Do something
//
//            File file = new File(Environment.getExternalStorageDirectory() + "/SalesLineCallRecordings");
//            File[] files = file.listFiles();
//            if (files != null) {
//
//                    for (int j = 0; j < files.length; j++) {
//                        files[j].getAbsolutePath();
//
//                        //type = "yes";
//                        Log.e("File Path Upload " + j, files[j].getAbsolutePath() + "");
//                    /*
//                    Intent intent1 = new Intent();
//                    intent1.setAction("com.brillinfosystem.voicerecorder.UploadService");
//                    intent1.putExtra("type",type);
//                    intent1.putExtra("audioFile",files[j].getPath());
//                    context.startService(intent);*/
//
//                        uploadActivityFromFile(files[j].getAbsolutePath());
//
//                }
//            }
//
//
//            mContext = context;
//            AudioDbHelper dbHelper = new AudioDbHelper(mContext);
//
//
//            String query = "Select filename from " + AudioContract.AudioEntry.TABLE_AUDIO;
//            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
//            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//            cursor.moveToFirst();
//
//            int count = sqLiteDatabase.rawQuery(query, null).getCount();
//            Log.e("Count", "" + count);
//            try {
//
//                if (count > 0) {
//                    if (count == 1) {
//                        int colIndex = cursor.getColumnIndex("filename");
//                        String audioFile = cursor.getString(colIndex);
//                        Log.e("Audio File", audioFile + "");
//                        uploadActivityFromDB(audioFile);
//                    } else {
//                        int colIndex = cursor.getColumnIndex("filename");
//                        String audioFile1 = cursor.getString(colIndex);
//                        Log.e("Audio File", audioFile1 + "");
//                        uploadActivityFromDB(audioFile1);
//                        while (cursor.moveToNext()) {
//                            int columnIndex = cursor.getColumnIndex("filename");
//                            String audioFile = cursor.getString(columnIndex);
//                            Log.e("Audio File", audioFile + "");
//                            uploadActivityFromDB(audioFile);
//                        }
//                    }
//                }
//            } finally {
//                if (cursor_ != null && !cursor_.isClosed()) ;
//                cursor.close();
//
//                sqLiteDatabase.close();
//
//            }
//
//
//        } else {
//            //uploadActivity(false);
//        }


    }

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @param
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @param
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    private void uploadActivityFromFile(String audioFile) {

        type = "yes";
          Intent intent = new Intent(mContext.getApplicationContext(), UploadService.class);
        intent.putExtra("type",type);
         intent.putExtra("audioFile",audioFile);
        mContext.startService(intent);
        //new UploadAsync().execute(audioFile);

    }

    //Upload Activity from DB
    private void uploadActivityFromDB(String audioFile) {

        type = "yes";

        Intent intent = new Intent(mContext.getApplicationContext(), UploadService.class);
        intent.putExtra("type",type);
        intent.putExtra("audioFile",audioFile);
        mContext.startService(intent);
      //  new UploadAsync().execute(audioFile);

    }




    public static void myMethod(Boolean onlineStat) {
        onlineStatus = onlineStat;
        getOnlineStatus = onlineStat;
        Log.e("online status", getOnlineStatus + "");
    }

    /* check whether connected to internet or not by pinging DNS server*/

    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /* Async task  to upload */





    /*  Delete Uploaded Files from the sqlite database*/



    /*  Delete Uploaded Files from the external storage directory*/

}
