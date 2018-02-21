package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

/**
 * Created by Paras-Android on 28-09-2017.
 */

public class ReminderAsyncTask extends AsyncTask<Void, Void, Void> {
    private SaveData objSaveData;
    String roleType;
    Context ctx;
    public ReminderAsyncTask(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.e("I am here","In reminder Async Task");
        PendingIntent pendingIntent;
        AudioDbHelper userDbHelper = new AudioDbHelper(ctx);
        SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();

        objSaveData = new SaveData(ctx);
        String roleId= objSaveData.getString("role_id");
        if(roleId.equals("5"))
        {
            roleType = "user";
        }
        else if(roleId.equals("4"))
        {
            roleType = "manager";
        }
        else if(roleId.equals("3"))
        {
            roleType = "asm";
        }
        else if(roleId.equals("2"))
        {
            roleType = "agm";
        }
        else if(roleId.equals("1"))
        {
            roleType = "gm";
        }
        String selection = "email=?";

        String email = objSaveData.getString("LoginId");
        String[] selectionArgs = {email};

        Log.e("String selection",""+selection+" "+email);

        Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_REMINDERS, //Table to query
                null, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //filter by row groups
                null, null, null);

        Log.e("Count =",""+query.getCount()+"query "+query.toString());
        try
        {
            while(query.moveToNext()) {


                String alarmId = query.getString(1);
                Log.e("Alarm id in async",alarmId);
                String alarm_type = query.getString(2);
                String reminder_message = query.getString(3);
                String reminder_extra_message = query.getString(4);
                String gif_url = query.getString(5);
                String reminder_set = query.getString(6);
                String totalTarget = query.getString(7);
                String targetAchieved = query.getString(8);
                String followUpTime = query.getString(9);
                String leadNumber = query.getString(10);
                String userName = query.getString(11);

                if (reminder_set.equals("no")) {
                    if(alarm_type.equals("target")) {

                        Intent intentGIFActivity = new Intent(ctx,AlarmGIFActivity.class);
                        intentGIFActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentGIFActivity.putExtra("alarmType", "target");
                        intentGIFActivity.putExtra("gifMessage", reminder_message);
                        intentGIFActivity.putExtra("extraMessage", reminder_extra_message);
                        intentGIFActivity.putExtra("totalTarget", totalTarget);
                        intentGIFActivity.putExtra("targetAchieved", targetAchieved);
                        intentGIFActivity.putExtra("alarmId", Integer.parseInt(alarmId));

                        ctx.startActivity(intentGIFActivity);

                    }
                    else
                    {
                        Intent intentGIFActivity = new Intent(ctx, AlarmGIFActivity.class);
                        intentGIFActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentGIFActivity.putExtra("alarmType", alarm_type);
                        intentGIFActivity.putExtra("gifMessage", reminder_message);
                        intentGIFActivity.putExtra("extraMessage", reminder_extra_message);
                        intentGIFActivity.putExtra("gifUrl", gif_url);
                        intentGIFActivity.putExtra("alarmId", Integer.parseInt(alarmId));
                        intentGIFActivity.putExtra("followUpTime",followUpTime);
                        intentGIFActivity.putExtra("leadNumber",leadNumber);
                        intentGIFActivity.putExtra("userName",userName);
                        ctx.startActivity(intentGIFActivity);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }






}
