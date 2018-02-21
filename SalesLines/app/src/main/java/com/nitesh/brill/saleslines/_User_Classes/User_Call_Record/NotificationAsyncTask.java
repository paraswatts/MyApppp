package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity;
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Paras-Android on 28-09-2017.
 */

public class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {

    Context ctx;
    SaveData objSaveData ;
    public NotificationAsyncTask(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
         objSaveData = new SaveData(ctx);
        String email = objSaveData.getString("LoginId");
        String[] selectionArgs = {email};
        PendingIntent pendingIntent;
        String selection = "email=?";

        AudioDbHelper userDbHelper = new AudioDbHelper(ctx);
        SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();
        Uri alarmSound = Uri.parse(objSaveData.getString("notificationSound"));

        String columns[] = {AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FIRED, AudioContract.AudioEntry._ID};
        Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, //Table to query
                null, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //filter by row groups
                null, null, null);//The values for the WHERE clause
        String yes = "yes";
        String userRole = objSaveData.getString("userRole");
        Log.e("UserRole in async",""+userRole);
        Log.e("Email",""+email);

        try
        {
           // Log.e("I am here","async while loop");

            while(query.moveToNext()) {
                  //  Log.e("I am here","async while loop");
                if (email.equals(query.getString(14)) ) {


                    //Log.e("Email in async is", query.getString(1) +"yes or no"+query.getString(12));

                    if (query.getString(12).equals("no")) {

                        if(query.getString(4).equals("assignlead"))
                        {
                            if (userRole.equals("user")) {
                                ContentValues content_Values = new ContentValues();
                                content_Values.put(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FIRED, "yes");
                                sqLiteDatabase.update(AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS,
                                        content_Values,
                                        AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EMAIL + "= ?",
                                        new String[]{email});
                                Log.e("Message is ",query.getString(18));
                                sendNotification(query.getString(18),query.getString(6));
                            }

                        }}}


//                        String notificationType = query.getString(4);
//                        if (userRole.equals("user")) {
//                            Log.e("In user","Async");
//                            Intent intent = new Intent(ctx, User_Home_Activity.class);
//
//                            if (notificationType.equals("followUpReminder")) {
//                                String title = query.getString(7);
//                                String message = query.getString(17);
//                                String lastCalled = query.getString(10);
//                                String image = query.getString(13);
//                                String leadMobile = query.getString(15);
//                                String leadSMS = query.getString(16);
//                                intent.putExtra("notificationFragment", "followUpReminder");
//                                intent.putExtra("title", title);
//                                intent.putExtra("message", message);
//                                intent.putExtra("lastCalled", lastCalled);
//                                intent.putExtra("leadMobile",leadMobile);
//                                intent.putExtra("leadSMS",leadSMS);
//                                intent.putExtra("image", image);
//                            }
//
//                            if (notificationType.equals("targetReminder")) {
//                                String totalTarget = query.getString(8);
//                                String targetAchieved = query.getString(9);
//                                intent.putExtra("notificationFragment", "targetReminder");
//                                intent.putExtra("totalTarget", totalTarget);
//                                intent.putExtra("targetAchieved", targetAchieved);
//                            }
//
//                            if (notificationType.equals("missedFollowups")) {
//                                String imageUrl = query.getString(3);
//                                String missedReminders = query.getString(5);
//                                intent.putExtra("notificationFragment", "missedFollowups");
//                                intent.putExtra("image", imageUrl);
//                                intent.putExtra("reminders", missedReminders);
//                            }
//
//                            if (notificationType.equals("saleClosureReminder")) {
//
//                                String path = query.getString(13);
//                                String message = query.getString(17);
//                                String title = query.getString(7);
//                                String extraMessage = query.getString(6);
//                                intent.putExtra("notificationFragment", "saleClosureReminder");
//                                intent.putExtra("image", path);
//                                intent.putExtra("message", message);
//                                intent.putExtra("title", title);
//                                intent.putExtra("extra", extraMessage);
//                            }
//                            File file = new File(query.getString(13));
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                            ShowNotifications(query.getString(3), query.getString(7), query.getString(17), alarmSound, pendingIntent, file);
//
//                        }
//
//                        if (userRole.equals("manager")) {
//                            Log.e("In manager","Async");
//                            Intent intent = new Intent(ctx, Manager_Home_Activity.class);
//
//                            if (notificationType.equals("saleClosureManagerReminder")) {
//                                String userName = query.getString(11);
//
//                                intent.putExtra("managerNotificationFragment", "saleClosureManagerReminder");
//                                intent.putExtra("userName", userName);
//                            }
//
//                            if (notificationType.equals("managerTargetReminder")) {
//                                intent.putExtra("managerNotificationFragment", "targetManagerReminder");
//                                String totalTarget = query.getString(8);
//                                String targetAchieved = query.getString(9);
//                                intent.putExtra("totalTarget", totalTarget);
//                                intent.putExtra("targetAchieved", targetAchieved);
//                            }
//                            if (notificationType.equals("managerMissedFollowups")) {
//                                String userName = query.getString(11);
//                                String imageUrl = query.getString(3);
//                                String missedReminders = query.getString(5);
//                                intent.putExtra("managerNotificationFragment", "managerMissedFollowups");
//                                intent.putExtra("image", imageUrl);
//                                intent.putExtra("reminders", missedReminders);
//                                intent.putExtra("userName",userName);
//                            }
//
//                            File file = new File(query.getString(13));
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                            ShowNotifications(query.getString(3), query.getString(7), query.getString(17), alarmSound, pendingIntent, file);
//
//                        }
//                        if (userRole.equals("gm")) {
//                            Log.e("In gm","Async");
//                            Intent intent = new Intent(ctx, GM_Home_Activity.class);
//                            if (notificationType.equals("gmSaleClosureReminder")) {
//                                String userName = query.getString(11);
//
//                                intent.putExtra("gmNotificationFragment", "gmSaleClosureReminder");
//                                intent.putExtra("userName", userName);
//                            }
//
//                            if (notificationType.equals("gmTargetReminder")) {
//                                intent.putExtra("gmNotificationFragment", "gmTargetReminder");
//                                String totalTarget = query.getString(8);
//                                String targetAchieved = query.getString(9);
//                                intent.putExtra("totalTarget", totalTarget);
//                                intent.putExtra("targetAchieved", targetAchieved);
//                            }
//                            File file = new File(query.getString(13));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                            ShowNotifications(query.getString(3), query.getString(7), query.getString(17), alarmSound, pendingIntent, file);
//
//                        }
//                        if (userRole.equals("agm")) {
//                            Log.e("In agm","Async");
//                            Intent intent = new Intent(ctx, AGM_Home_Activity.class);
//                            if (notificationType.equals("agmSaleClosureReminder")) {
//                                String userName = query.getString(11);
//
//                                intent.putExtra("agmNotificationFragment", "agmSaleClosureReminder");
//                                intent.putExtra("userName", userName);
//                            }
//
//                            if (notificationType.equals("agmTargetReminder")) {
//                                intent.putExtra("agmNotificationFragment", "agmTargetReminder");
//                                String totalTarget = query.getString(8);
//                                String targetAchieved = query.getString(9);
//                                intent.putExtra("totalTarget", totalTarget);
//                                intent.putExtra("targetAchieved", targetAchieved);
//                            }
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                            File file = new File(query.getString(13));
//
//                            ShowNotifications(query.getString(3), query.getString(7), query.getString(17), alarmSound, pendingIntent, file);
//
//                        }
//                        if (userRole.equals("asm")) {
//                            Log.e("In asm","Async");
//                            Intent intent = new Intent(ctx, ASM_Home_Activity.class);
//                            if (notificationType.equals("asmSaleClosureReminder")) {
//                                String userName = query.getString(11);
//
//                                intent.putExtra("asmNotificationFragment", "asmSaleClosureReminder");
//                                intent.putExtra("userName", userName);
//                            }
//
//                            if (notificationType.equals("gmTargetReminder")) {
//                                intent.putExtra("asmNotificationFragment", "asmTargetReminder");
//                                String totalTarget = query.getString(8);
//                                String targetAchieved = query.getString(9);
//                                intent.putExtra("totalTarget", totalTarget);
//                                intent.putExtra("targetAchieved", targetAchieved);
//                            }
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//                            File file = new File(query.getString(13));
//
//                            ShowNotifications(query.getString(3), query.getString(7), query.getString(17), alarmSound, pendingIntent, file);
//
//                        }
//
//
//                    }}

            }
        }
        finally{
            query.close();
        }
        //group the rows
        return null;
    }

    private void sendNotification(String message, String extraMessage) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.ic_launcher_circle);
        builder.setContentTitle(message);
        builder.setContentText(extraMessage);

        Random rand = new Random();
        int  n = rand.nextInt(10000) + 1;
//        if(objSaveData.getBoolean("vibrateSound")) {
//            builder.setSound(alarmSound);
//
//
//            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//        }
//        else
//        {
//            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//
//        }

        AudioManager am = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.e("Ringer Mode","Silent");
                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.e("Ringer Mode","Vibrate");
                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                break;
            case AudioManager.RINGER_MODE_NORMAL:
               // if (objSaveData.getBooleanReminder("vibrateSound")) {
                    builder.setSound(getAlarmUri());
                    builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//
//                } else {
//                    Log.e("Playing sound","Yes4");
//
//                    builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//                }
                break;
        }
        Intent notificationIntent = new Intent(ctx, User_Home_Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(n, builder.build());
    }

    private Uri getAlarmUri() {

        Uri alert = null;
        Log.e("notificationSound",objSaveData.getString("notificationSound"));

//        objSaveData.save("notificationSound", mUri.toString())
        if(objSaveData.getString("notificationSound").equals("notificationSound"))
        {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Log.e("Alarm sound Uri 2",alert.toString());
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_ALARM);
                Log.e("Alarm sound Uri 3",alert.toString());
                if (alert == null) {
                    alert = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Log.e("Alarm sound Uri 4",alert.toString());
                }
            }

        }
        else {
            alert = Uri.parse(objSaveData.getString("notificationSound"));
            Log.e("Alarm sound Uri 1",Uri.parse(objSaveData.getString("notificationSound")).toString());
        }
        return alert;
    }


    private void ShowNotifications(String imageUrl,String title, String message,Uri alarmSound, PendingIntent pendingIntent,File path)
    {


        SaveData objSaveData = new SaveData(ctx);
        Random rand = new Random();
        int  n = rand.nextInt(10000) + 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);
        if(objSaveData.getBoolean("vibrateSound")) {
            builder.setSound(alarmSound);
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        }
        else
        {
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        }
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.saleslinelogo);


        if(imageUrl!=null) {


            Log.e("DB Image Path", "" + path.getAbsolutePath());
            Bitmap bitmap = getBitmapFromURL(imageUrl, path);

            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        }
        else
        {
            builder.setStyle(new NotificationCompat.InboxStyle().addLine(message));
        }
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(n,builder.build());
    }
    public Bitmap getBitmapFromURL(String strURL, File path) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            storeImage(myBitmap,path);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void storeImage(Bitmap image, File path) {
        Log.e("Saving Image","");

        File pictureFile = path;
        Log.e("FilePath",pictureFile.getAbsolutePath());


        if (pictureFile == null) {
            Log.d("",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("", "Error accessing file: " + e.getMessage());
        }
    }



}
