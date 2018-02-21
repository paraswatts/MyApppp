package com.nitesh.brill.saleslines.FirebaseService;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Common_Files.updateNotIcon;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity;
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.AlarmGIFActivity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.MyAlarmService;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Notification;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Reminders;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Paras-Android on 11-09-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String INTENT_FILTER = "INTENT_FILTER";
    public static final String INTENT_FILTER_MANAGER = "INTENT_FILTER_MANAGER";
    public static final String INTENT_FILTER_GM = "INTENT_FILTER_GM";
    public static final String INTENT_FILTER_AGM = "INTENT_FILTER_AGM";
    public static final String INTENT_FILTER_ASM = "INTENT_FILTER_ASM";

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    int min, beforeMinutes, afterMinutes;
    int hour, beforeHour, afterHour;
    String ampmBefore, ampmAfter;
    AudioDbHelper dbHelper = new AudioDbHelper(this);
    private SaveData objSaveData;
    private Notification notification;
    private Reminders reminders;
    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        objSaveData = new SaveData(this);
        notification = new Notification();
        reminders = new Reminders();

        try {
            if (remoteMessage.getData().size() > 0) {

                Log.e("Login State in ", "notifications" + objSaveData.getBoolean("loginState"));
                String message;
                String imageUrl;
                String title;
                String roleType;
                String noOfMissedReminders;
                String extraMessage;
                String totalTarget;
                String targetAchieved;
                String lastCalled;
                String userName;
                String email;
                String leadMobile;
                String leadSMS;
                String followUpTime;
                String missedFollowUpTime;
                String gifUrl;
                String alarmType = null;
                String leadNumber;


                //============Extract different payload fields from the notiication ============//
                Log.e("Data Payload of manager", remoteMessage.getData().toString());
                message = remoteMessage.getData().get("message");
                title = remoteMessage.getData().get("title");
                imageUrl = remoteMessage.getData().get("image");
                noOfMissedReminders = remoteMessage.getData().get("numberOfreminders");
                String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                roleType = remoteMessage.getData().get("roleType");
                extraMessage = remoteMessage.getData().get("extraMessage");
                totalTarget = remoteMessage.getData().get("totalTarget");
                targetAchieved = remoteMessage.getData().get("targetAchieved");
                lastCalled = remoteMessage.getData().get("lastCalled");
                Boolean loginState = objSaveData.getBoolean("loginState");
                email = remoteMessage.getData().get("loginId");
                leadMobile = remoteMessage.getData().get("leadMobile");
                leadSMS = remoteMessage.getData().get("sms");
                missedFollowUpTime = remoteMessage.getData().get("missedFollowUpTime");
                gifUrl = remoteMessage.getData().get("gifUrl");
                leadNumber = remoteMessage.getData().get("leadphone");
                userName = remoteMessage.getData().get("sm");
                followUpTime = remoteMessage.getData().get("followUpTime");
                // followUpTime = "07:17:PM";

                //Log.e("====", userName + "==Data==" + leadNumber);


                String notificationsType = remoteMessage.getData().get("notificationsType");


                if (notificationsType.equals("targetReminder") ||
                        notificationsType.equals("managerTargetReminder") ||
                        notificationsType.equals("gmTargetReminder") ||
                        notificationsType.equals("agmTargetReminder") ||
                        notificationsType.equals("asmTargetReminder")
                        ) {
                    alarmType = "target";
                } else if (
                        notificationsType.equals("saleClosureManagerReminder") ||
                                notificationsType.equals("gmSaleClosureReminder") ||
                                notificationsType.equals("asmSaleClosureReminder") ||
                                notificationsType.equals("agmSaleClosureReminder")
                        ) {
                    alarmType = "saleclouserManager";
                } else if (
                        notificationsType.equals("saleClosureReminder")
                        ) {
                    alarmType = "saleclouser";
                } else if (
                        notificationsType.equals("followUpReminder")
                        ) {
                    alarmType = "follow";
                } else if (
                        notificationsType.equals("missedFollowups")
                        ) {
                    alarmType = "missed";
                } else if (
                        notificationsType.equals("assignlead")
                        ) {
                    alarmType = "assignlead";
                } else if (
                        notificationsType.equals("smduplicityReminder")
                        ) {
                    alarmType = "smduplicityReminder";
                }



                if (roleType.equals("user") && objSaveData.getString("LoginId").equals(email) && loginState) {
                    Intent broadCast = new Intent(INTENT_FILTER);

                    sendBroadcast(broadCast);
                    Intent intent = new Intent(this, User_Home_Activity.class);
                    if (notificationsType.equals("followUpReminder")) {

                        alarmType = "follow";
                        intent.putExtra("notificationFragment", "followUpReminder");
                        intent.putExtra("extraMessage", extraMessage);
                        intent.putExtra("message", message);
                        intent.putExtra("lastCalled", lastCalled);
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("followUpTime", followUpTime);
                        intent.putExtra("leadNumber", leadNumber);
                        Log.e("username", "" + leadNumber);
                        intent.putExtra("userName", userName);

                        String finalMessage = message +  " " +extraMessage;
                                addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");


//=================used in followd up ===============\\

                        objSaveData.save("leadSMS", leadSMS);
                        try {
                            SimpleDateFormat df = new SimpleDateFormat("hh:mm:a");
                            Date d = df.parse(followUpTime);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            //cal.add(Calendar.MINUTE, 15);
                            String newTime = df.format(cal.getTime());
                            afterHour = cal.get(Calendar.HOUR);
                            afterMinutes = cal.get(Calendar.MINUTE);

                            if (cal.get(Calendar.AM_PM) == 0) {
                                ampmAfter = "AM";
                            } else {
                                ampmAfter = "PM";
                            }
                            Log.e("Change Tme ==== " + newTime, "============ " + afterHour + "  ====  " + afterMinutes + "  ===  " + ampmAfter);


                        } catch (Exception e) {

                            e.printStackTrace();

                        }


                        //  setAlarm(beforeHour, beforeMinutes, ampmBefore, "follow", message, extraMessage);
                        // setAlarm(hour, min, ampm, "follow", message, extraMessage);


                        setAlarm(afterHour, afterMinutes, ampmAfter, "follow", message, extraMessage, gifUrl, email);


                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("targetReminder")) {
                        alarmType = "target";
                        intent.putExtra("notificationFragment", "targetReminder");
                        intent.putExtra("totalTarget", totalTarget);
                        intent.putExtra("targetAchieved", targetAchieved);
                        String finalMessage = message +  " " +totalTarget+" and you have achieved" + " " + targetAchieved + " "+extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openTargetGifActivity(targetAchieved, message, extraMessage, totalTarget);

                    }

                    if (notificationsType.equals("missedFollowups")) {
                        alarmType = "missed";
                        intent.putExtra("notificationFragment", "missedFollowups");
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("reminders", noOfMissedReminders);
                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("saleClosureReminder")) {

                        alarmType = "saleclouser";
                        intent.putExtra("notificationFragment", "saleClosureReminder");
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("message", message);
                        intent.putExtra("title", title);
                        intent.putExtra("extra", extraMessage);

                        String finalMessage = message +  " "  +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("smduplicityReminder")) {

                        alarmType = "smduplicityReminder";
                        intent.putExtra("notificationFragment", "smduplicityReminder");
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("message", message);
                        intent.putExtra("title", title);
                        intent.putExtra("extra", extraMessage);

                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    //==== assign lead reminder ====\\


                    if (notificationsType.equals("assignlead")) {

                        alarmType = "assignlead";
                        intent.putExtra("notificationFragment", "assignlead");
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("message", message);
                        intent.putExtra("title", title);
                        intent.putExtra("extra", extraMessage);
                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        //=====================================\\
                        sendNotification(message, extraMessage);

                        //openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("duplicateLead")) {
                        alarmType = "duplicateLead";
                        intent.putExtra("notificationFragment", "duplicateLead");
                        intent.putExtra("image", gifUrl);
                        intent.putExtra("message", message);
                        intent.putExtra("title", title);
                        intent.putExtra("extra", extraMessage);
                        //=====================================\\
                        sendNotification(message, extraMessage);
                        //openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }
                } else if (roleType.equals("manager") && objSaveData.getString("LoginId").equals(email) && loginState) {
                    Intent broadCast = new Intent(INTENT_FILTER_MANAGER);

                    Intent intent = new Intent(this, Manager_Home_Activity.class);
                    if (notificationsType.equals("saleClosureManagerReminder")) {
                        alarmType = "saleclouserManager";
                        intent.putExtra("managerNotificationFragment", "saleClosureManagerReminder");
                        intent.putExtra("userName", userName);
                        intent.putExtra("image", gifUrl);
                        String finalMessage = message + " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("managerTargetReminder")) {
                        String finalMessage = message +  " " +totalTarget+" and you have achieved" + " " + targetAchieved + " "+extraMessage;                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        alarmType = "target";
                        intent.putExtra("managerNotificationFragment", "targetManagerReminder");
                        intent.putExtra("totalTarget", totalTarget);
                        intent.putExtra("targetAchieved", targetAchieved);
                        openTargetGifActivity(targetAchieved, message, extraMessage, totalTarget);
                    }
                } else if (roleType.equals("gm") && objSaveData.getString("LoginId").equals(email)) {
                    Intent broadCast = new Intent(INTENT_FILTER_GM);

                    Intent intent = new Intent(this, GM_Home_Activity.class);
                    if (notificationsType.equals("gmSaleClosureReminder")) {
                        Log.e("Notification", "for GM Sale Closure");
                        alarmType = "saleclouserManager";
                        intent.putExtra("gmNotificationFragment", "gmSaleClosureReminder");
                        intent.putExtra("userName", userName);
                        intent.putExtra("image", gifUrl);
                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("gmTargetReminder")) {
                        String finalMessage = message +  " " +totalTarget+" and you have achieved" + " " + targetAchieved + " "+extraMessage;                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        alarmType = "target";
                        intent.putExtra("gmNotificationFragment", "gmTargetReminder");
                        intent.putExtra("totalTarget", totalTarget);
                        intent.putExtra("targetAchieved", targetAchieved);
                        openTargetGifActivity(targetAchieved, message, extraMessage, totalTarget);
                    }
                } else if (roleType.equals("asm") && objSaveData.getString("LoginId").equals(email)) {
                    Intent broadCast = new Intent(INTENT_FILTER_ASM);

                    Intent intent = new Intent(this, ASM_Home_Activity.class);
                    if (notificationsType.equals("asmSaleClosureReminder")) {
                        Log.e("Notification", "for ASM Sale Closure");
                        alarmType = "saleclouserManager";
                        intent.putExtra("asmNotificationFragment", "asmSaleClosureReminder");
                        intent.putExtra("userName", userName);
                        intent.putExtra("image", gifUrl);
                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }

                    if (notificationsType.equals("asmTargetReminder")) {
                        alarmType = "target";
                        Log.e("Notification", "for ASM target");
                        intent.putExtra("asmNotificationFragment", "asmTargetReminder");
                        intent.putExtra("totalTarget", totalTarget);
                        intent.putExtra("targetAchieved", targetAchieved);
                        String finalMessage = message +  " " +totalTarget+" and you have achieved" + " " + targetAchieved + " "+extraMessage;                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openTargetGifActivity(targetAchieved, message, extraMessage, totalTarget);
                    }
                } else if (roleType.equals("agm") && objSaveData.getString("LoginId").equals(email)) {
                    Intent broadCast = new Intent(INTENT_FILTER_AGM);

                    Intent intent = new Intent(this, AGM_Home_Activity.class);
                    if (notificationsType.equals("agmSaleClosureReminder")) {
                        alarmType = "saleclouserManager";
                        intent.putExtra("agmNotificationFragment", "agmSaleClosureReminder");
                        intent.putExtra("userName", userName);
                        intent.putExtra("image", gifUrl);
                        String finalMessage = message +  " " +extraMessage;
                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openGifActivity(alarmType, message, extraMessage, gifUrl, leadNumber, followUpTime, userName);
                    }
                    if (notificationsType.equals("agmTargetReminder")) {
                        alarmType = "target";
                        intent.putExtra("agmNotificationFragment", "agmTargetReminder");
                        intent.putExtra("totalTarget", totalTarget);
                        intent.putExtra("targetAchieved", targetAchieved);
                        String finalMessage = message +  " " +totalTarget+" and you have achieved" + " " + targetAchieved + " "+extraMessage;                        addNotificationToDB(noOfMissedReminders, finalMessage, extraMessage, gifUrl, "yes", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");

                        openTargetGifActivity(targetAchieved, message, extraMessage, totalTarget);
                    }

                } else {
                    addNotificationToDB(noOfMissedReminders, message, extraMessage, gifUrl, "no", roleType, notificationsType, totalTarget, targetAchieved, email, userName, "no");
                    if (!notificationsType.equals("assignlead")) {
                        addReminderToDB(alarmType, message, extraMessage, gifUrl, "no", totalTarget, targetAchieved, userName, leadNumber, followUpTime, roleType, email);
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void sendNotification(String message, String extraMessage) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);
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

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
                //if (objSaveData.getBooleanReminder("vibrateSound")) {
                    builder.setSound(getAlarmUri());
                    builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

//                } else {
//                    Log.e("Playing sound","Yes4");
//
//                    builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//                }
                break;
        }
        Intent notificationIntent = new Intent(this, User_Home_Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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


    public void openGifActivity(String alarmType, String message, String extraMessage, String gifUrl, String leadNumber, String followUpTime, String userName) {

        Intent intentGIFActivity = new Intent(getApplicationContext(), AlarmGIFActivity.class);
        intentGIFActivity.putExtra("alarmType", alarmType);
        intentGIFActivity.putExtra("gifMessage", message);
        intentGIFActivity.putExtra("extraMessage", extraMessage);
        intentGIFActivity.putExtra("gifUrl", gifUrl);
        intentGIFActivity.putExtra("followUpTime", followUpTime);
        intentGIFActivity.putExtra("leadNumber", leadNumber);
        Log.e("leadNumber", "" + leadNumber);
        intentGIFActivity.putExtra("userName", userName);
        intentGIFActivity.putExtra("message", "0");
        intentGIFActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentGIFActivity);
    }

    public void openTargetGifActivity(String targetAchieved, String message, String extraMessage, String totalTarget) {
        Intent intentGIFActivity = new Intent(getApplicationContext(), AlarmGIFActivity.class);
        intentGIFActivity.putExtra("alarmType", "target");
        intentGIFActivity.putExtra("gifMessage", message);
        intentGIFActivity.putExtra("extraMessage", extraMessage);
        intentGIFActivity.putExtra("totalTarget", totalTarget);
        intentGIFActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentGIFActivity.putExtra("targetAchieved", targetAchieved);
        startActivity(intentGIFActivity);
    }

    public void addNotificationToDB(String noOfMissedReminders, String message, String extraMessage, String gifUrl, String reminderFired, String roleType, String notificationsType, String totalTarget, String targetAchieved, String email, String userName,String readState) {
        String dateTime = new SimpleDateFormat("dd MMM hh:mm:ss aa").format(Calendar.getInstance().getTime());
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm:ss aa");
        String time = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());
        try {
            Date mDate = sdf.parse(dateTime);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        updateNotIcon updateNotIcon = (com.nitesh.brill.saleslines.Common_Files.updateNotIcon)getBaseContext();
//        updateNotIcon.getNotCount1();
        notification.setmImage(gifUrl);
        notification.setmNotificationReminder(noOfMissedReminders);
        notification.setmNotificationMessage(message);
        notification.setmExtraMessage(extraMessage);
        notification.setmNotificationUserRole(roleType);
        notification.setmNotificationsType(notificationsType);
        notification.setmTarget(totalTarget);
        notification.setmTargetAchieved(targetAchieved);
        notification.setmFilePath(gifUrl);
        notification.setmEmail(email);
        notification.setmUserName(userName);
        notification.setmLeadStatus("0");
        notification.setmLeadTime(time);
        notification.setmLeadDate(String.valueOf(timeInMilliseconds));
        notification.setReadState(readState);
        notification.setmFired(reminderFired);

        //    notification.setmLeadMobile(leadMobile);
        //   notification.setmLeadSMS(leadSMS);
        dbHelper.addNotification(notification);

    }


    public void addReminderToDB(String alarmType, String message, String extraMessage, String gifUrl, String reminderSet, String totalTarget, String targetAchieved, String userName, String leadNumber, String followUpTime, String roleType, String email) {
        Random rand = new Random();
        int n = rand.nextInt(10000) + 1;
        reminders.setmReminderId(n);
        reminders.setmAlarmType(alarmType);
        reminders.setmMessage(message);
        reminders.setmExtraMessage(extraMessage);
        reminders.setmGifUrl(gifUrl);
        reminders.setmmReminderSet(reminderSet);
        reminders.setmTotalTarget(totalTarget);
        reminders.setmTargetAchieved(targetAchieved);
        reminders.setmUserName(userName);
        reminders.setmLeadNumber(leadNumber);
        reminders.setmfollowUpTime(followUpTime);
        reminders.setmRoleType(roleType);
        reminders.setmLoginEmail(email);

        dbHelper.addReminders(reminders);
    }

    public void setAlarm(int hours, int minutes, String AMPM, String alarmType, String message, String extraMessage,String gifUrl,String email) {

        Random rand = new Random();
        int n = rand.nextInt(10000) + 1;
        Log.e("Alarm set for ", hours + "   " + minutes + "   " + AMPM + " " + message + extraMessage);

       // addReminderToDB(alarmType, message, extraMessage, gifUrl, "no", totalTarget, targetAchieved, userName, leadNumber, followUpTime, roleType, email);

        Calendar calendar = Calendar.getInstance();
        // set selected time from timepicker to calendar
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 00);


        if (AMPM.equals("PM")) {
            Log.e("PM", "");
            calendar.set(Calendar.AM_PM, Calendar.PM);
        } else {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        }
        Intent myIntent = new Intent(this,
                MyAlarmService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        myIntent.putExtra("gifUrl", gifUrl);
        myIntent.putExtra("alarmType", alarmType);
        myIntent.putExtra("gifMessage", message);
        myIntent.putExtra("extraMessage", extraMessage);
        myIntent.putExtra("email",email);


        PendingIntent pendingIntent;

        // A PendingIntent specifies an action to take in the
        // future
        pendingIntent = PendingIntent.getService(
                this, n, myIntent, 0);

        // set alarm time
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 22) {
            alarmManager.setExact(AlarmManager.RTC,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,
                    calendar.getTimeInMillis(), pendingIntent);
        }

    }

}