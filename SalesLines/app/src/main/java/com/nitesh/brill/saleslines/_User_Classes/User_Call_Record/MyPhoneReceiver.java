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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;

public class MyPhoneReceiver extends BroadcastReceiver {

	private String phoneNumber;
    Context context;
    private static final String YES_ACTION = "YES_ACTION";
    private static final String NO_ACTION = "NO_ACTION";
     private  SaveData objSaveData;

	@Override
	public void onReceive(final Context context, Intent intent) {
        this.context = context;
        //Notification();

        objSaveData = new SaveData(context);

        phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		String extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


	//	if (updateExternalStorageState() == Constants.MEDIA_MOUNTED) {
			try {
				SharedPreferences settings = context.getSharedPreferences(
						Constants.LISTEN_ENABLED, 0);
				//boolean silent = settings.getBoolean("silentMode", true);
				if (extraState != null) {
					if (extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

						Log.e("State","Offhook");

						Intent myIntent = new Intent(context,
								RecordService.class);
						myIntent.putExtra("commandType",
								Constants.STATE_CALL_START);
						context.startService(myIntent);
					} else if (extraState
							.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

						Log.e("State","Idle");

						NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
						Intent myIntent = new Intent(context,
								RecordService.class);
						myIntent.putExtra("commandType",
								Constants.STATE_CALL_END);
						context.startService(myIntent);
					} else if (extraState
							.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
						if (phoneNumber == null)
							phoneNumber = intent
									.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


						Log.e("State","Ringing");

                        objSaveData.save("phoneNumber",phoneNumber);
						Intent myIntent = new Intent(context,
								RecordService.class);
						myIntent.putExtra("commandType",
								Constants.STATE_INCOMING_NUMBER);

						myIntent.putExtra("phoneNumber", phoneNumber);


						///myIntent.putExtra("silentMode", silent);



						context.startService(myIntent);
					}
				} else if (phoneNumber != null) {
                    Log.e("Outgoing call",""+phoneNumber);

                    objSaveData.save("phoneNumber",phoneNumber);

					Intent myIntent = new Intent(context, RecordService.class);
					myIntent.putExtra("commandType",
							Constants.STATE_INCOMING_NUMBER);
					myIntent.putExtra("phoneNumber", phoneNumber);
					//myIntent.putExtra("silentMode", silent);
					context.startService(myIntent);
				}
			} catch (Exception e) {
				Log.e(Constants.TAG, "Exception");
				e.printStackTrace();
			}
	//	}
	}

    public void Notification() {
        // Set Notification Title
        String strtitle = "Do you want to record Call";
        // Set Notification Text
        String strtext = "For Salesline ?";

        // Open NotificationView Class on Notification Click

        // Send data to NotificationView Class

        // Open NotificationView.java Activity
        Intent yesIntent = new Intent(context, RecordService.class);
        yesIntent.setAction(YES_ACTION);

        PendingIntent yesPendingIntent = PendingIntent.getService(context, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent noIntent = new Intent(context, RecordService.class);
        noIntent.setAction(NO_ACTION);
        PendingIntent noPendingIntent = PendingIntent.getService(context, 0, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                // Set Ticker Message
                .setTicker("abc")
                // Set Title
                .setContentTitle(strtitle)
                // Set Text
                .setContentText(strtext)
                // Add an Action Button below Notification
                .addAction(R.drawable.arrow_spinner, "Yes", yesPendingIntent)
                .addAction(R.drawable.arrow_spinner, "No", noPendingIntent)
                // Set PendingIntent into Notification
                .setContentIntent(yesPendingIntent)
                // Dismiss Notification
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(100, builder.build());

    }
	public static int updateExternalStorageState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return Constants.MEDIA_MOUNTED;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return Constants.MEDIA_MOUNTED_READ_ONLY;
		} else {
			return Constants.NO_MEDIA;
		}
	}

}
