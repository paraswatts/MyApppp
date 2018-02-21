package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.MyAlarmService;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by brill on 5/12/17.
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    int abc;
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.e("Booting","Completed");
            setAlarm(context);

            new NotificationAsyncTask(context).execute();
            new ReminderAsyncTask(context).execute();
            SaveData objSaveData = new SaveData(context);
            if(objSaveData.getString("locationSharing").equals("yes")){
                setAlarmLocation(context);
            }


            /* Setting the alarm here */
//            Intent alarmIntent = new Intent(context, MyAlarmService.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//
//            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            int interval = 8000;
//            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//
//            Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();

//            Intent intent1 = new Intent(context, AlarmGIFActivity.class);
//
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            context.startActivity(intent1);

//            PendingIntent pendingIntent;
//            AudioDbHelper userDbHelper = new AudioDbHelper(context);
//            SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();
//            Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_REMINDERS, //Table to query
//                    null, //columns to return
//                    null, //columns for the WHERE clause
//                    null, //filter by row groups
//                    null, null, null);
//            try
//            {
//                while(query.moveToNext()) {
//
//                    int alarmId = query.getInt(1);
//                    int hours = query.getInt(2);
//                    int minutes = query.getInt(3);
//                    String ampm = query.getString(4);
//                    String alarm_type = query.getString(5);
//                    String reminder_message = query.getString(6);
//                    String reminder_extra_message = query.getString(7);
//                    String gif_url = query.getString(8);
//                    setAlarm(context,alarmId,hours,minutes,ampm,alarm_type,reminder_message,reminder_extra_message,gif_url);
//
//
//                }
//
//
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//            }

        }
    }

    public void setAlarm(Context context) {

        Random rand = new Random();
        int n = rand.nextInt(10000) + 1;

//                       Toast.makeText(getApplicationContext(), "Alarm On",
        //   Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        // set selected time from timepicker to calendar
        calendar.setTimeInMillis(System.currentTimeMillis());

        // if it's after or equal 9 am schedule for next day
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 9) {
            Log.e("", "Alarm will schedule for next day!");
            calendar.add(Calendar.DAY_OF_YEAR, 1); // add, not set!
        }
        else{
            Log.e("", "Alarm will schedule for today!");
        }
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
            //calendar.set(Calendar.AM_PM, Calendar.AM);

        Intent myIntent = new Intent(context,
                MyReceiver.class);
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        myIntent.putExtra("fetchApiData", "yes");
        PendingIntent pendingIntent;

        // A PendingIntent specifies an action to take in the
        // future
        boolean alarmRunning= (PendingIntent.getBroadcast(context, 280, myIntent, PendingIntent.FLAG_NO_CREATE)!=null) ;

        if (alarmRunning == false) {

            pendingIntent = PendingIntent.getBroadcast(
                    context, 280, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //==========save alarm details in database=========//

//        reminders.setmReminderId(n);
//        reminders.setmHours(hours);
//        reminders.setmMinutes(minutes);
//        reminders.setmAMPM(AMPM);
//        reminders.setmAlarmType(alarmType);
//        reminders.setmMessage(message);
//        reminders.setmExtraMessage(extraMessage);
//        reminders.setmGifUrl(gifUrl);
//        dbHelper.addReminders(reminders);

        //=================================================//

        // set alarm time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
        else {
            Log.e("Check Alarm", "Alarm Already running");
        }
    }


    public void setAlarmLocation(Context context) {

        Log.e("Setting","alarm");
        Intent myIntent = new Intent(context,LocationSharingReceiver.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager alarmManager;
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);


        PendingIntent pendingIntent;

                // A PendingIntent specifies an action to take in the
                // future

                pendingIntent = PendingIntent.getBroadcast(
                context, 300, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000*60*1, pendingIntent);

    }


}