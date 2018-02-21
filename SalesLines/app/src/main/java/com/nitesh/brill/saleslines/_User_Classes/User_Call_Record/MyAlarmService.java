package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;


import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import java.util.Random;


public class MyAlarmService extends IntentService
{
     private NotificationManager mManager;
    String TAG ="My_Alarm_Service";
    SaveData objSaveData;
    private Reminders reminders;
    AudioDbHelper dbHelper;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyAlarmService(String name) {
        super(name);
    }
    public MyAlarmService(){
        super(null);// That was the lacking constructor
    }

    @Override
     public IBinder onBind(Intent arg0)
     {
       // TODO Auto-generated method stub
        return null;
     }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        objSaveData =  new SaveData(this);
        reminders = new Reminders();
        dbHelper =  new AudioDbHelper(this);
        super.onStartCommand(intent,flags,startId);
        Log.e("Alarmfired","");
//        Log.e("Alarm Id in service",""+intent.getStringExtra("alarmId"));

        if(intent.getStringExtra("fetchApiData")!=null)
        {
            if(intent.getStringExtra("fetchApiData").equals("yes")) {
                Toast.makeText(this, "Alarm fired", Toast.LENGTH_SHORT).show();

                Log.e("Api Data", "fetch api data");
//
//            Intent intent1 = new Intent(this, User_Home_Activity.class);
//            startActivity(intent1);

                Intent intent1 = new Intent(MyAlarmService.this, AlarmGIFActivity.class);

                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        }
        else {
            Boolean loginState = objSaveData.getBoolean("loginState");

//
//            Intent intent1 = new Intent(this, User_Home_Activity.class);
//            startActivity(intent1);
            if (loginState && objSaveData.getString("userRole").equals("user")) {
                Intent intent1 = new Intent(MyAlarmService.this, AlarmGIFActivity.class);
                try {
                    Log.e("Alarm Id in service", "" + intent.getIntExtra("alarmId", 0));
                    intent1.putExtra("alarmId", intent.getIntExtra("alarmId", 0));
                    intent1.putExtra("alarmType", intent.getStringExtra("alarmType"));
                    intent1.putExtra("gifMessage", intent.getStringExtra("gifMessage"));
                    intent1.putExtra("extraMessage", intent.getStringExtra("extraMessage"));
                    intent1.putExtra("gifUrl", intent.getStringExtra("gifUrl"));
                    intent1.putExtra("message", "1");
                    intent1.putExtra("sendMessage", "no");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
            else
            {
                addReminderToDB(intent.getStringExtra("alarmType"),
                        intent.getStringExtra("gifMessage"),
                        intent.getStringExtra("extraMessage"),
                        intent.getStringExtra("gifUrl"),
                        "no",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "user",
                        intent.getStringExtra("email")
                        );
            }
        }
        return START_NOT_STICKY;
    }



    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }

    public void addReminderToDB(String alarmType, String message, String extraMessage, String gifUrl, String reminderSet, String totalTarget, String targetAchieved, String userName, String leadNumber, String followUpTime, String roleType, String email) {
        Random rand = new Random();
        Log.e("Saving","In Database");
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

//   @SuppressWarnings("static-access")
//   @Override
//   public void onStart(Intent intent, int startId)
//   {
//       super.onStart(intent, startId);
//       //Toast.makeText(this, "I'm running", Toast.LENGTH_SHORT).show();
//       Log.e("Alarmfired","");
//       Intent intent1 = new Intent(MyAlarmService.this,AlarmGIFActivity.class);
//       try {
//           intent1.putExtra("alarmType", intent.getStringExtra("alarmType"));
//           intent1.putExtra("gifMessage", intent.getStringExtra("gifMessage"));
//           intent1.putExtra("extraMessage", intent.getStringExtra("extraMessage"));
//       }
//       catch (Exception e)
//       {
//           e.printStackTrace();
//       }
//       intent1.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//       startActivity(intent1);
//
//
//    }

    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}