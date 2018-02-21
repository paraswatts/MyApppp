package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.TodayFollowUpActivity;
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.TodayFollowUp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReceiver extends WakefulBroadcastReceiver
{
    private ArrayList<TodayFollowUp> dataList = new ArrayList<>();

    TodayFollowUp todayFollowUp;
	SaveData objSaveData;
	@Override
	 public void onReceive(Context context, Intent intent)
	{
		objSaveData =  new SaveData(context);
		if(intent.getStringExtra("fetchApiData")!=null)
		{


			if(intent.getStringExtra("fetchApiData").equals("yes")) {
                Log.e("Api Data", "fetch api data");
                Boolean loginState = objSaveData.getBoolean("loginState");

//
//            Intent intent1 = new Intent(this, User_Home_Activity.class);
//            startActivity(intent1);
                if (loginState && objSaveData.getString("userRole").equals("user")) {
//            Intent intent1 = new Intent(this, User_Home_Activity.class);
//            startActivity(intent1);
                    Log.e("Api Data", "getting data");

                    getData(context);

					//Intent intent1 = new Intent(context, TodayFollowUpActivity.class);

                   // intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  //  context.startActivity(intent1);
                }
                else{
                    setAlarm(context);
                }
            }
		}
	 }

	public void setAlarm(Context context) {

	    Log.e("Setting alarm again","for target reminder");
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
//		boolean alarmRunning= (PendingIntent.getBroadcast(context, 280, myIntent, PendingIntent.FLAG_NO_CREATE)!=null) ;
//
//		if (alarmRunning == false) {
//
			pendingIntent = PendingIntent.getBroadcast(
					context, n, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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

        if (Build.VERSION.SDK_INT <19) {
            alarmManager.set(AlarmManager.RTC,
                    System.currentTimeMillis()+1000*60*2, pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT <= 22) {
            alarmManager.setExact(AlarmManager.RTC,
                    System.currentTimeMillis()+1000*60*2, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,
                    System.currentTimeMillis()+1000*60*2, pendingIntent);
        }

//		else {
//			Log.e("Check Alarm", "Alarm Already running");
//		}
	}


	private void getData(final Context context) {

		//final ProgressDialog progressDialog = new ProgressDialog(context);
		//progressDialog.setMessage("Loading Data....");
		//progressDialog.setCancelable(false);
		//progressDialog.show();





		ApiEndpointInterface apiEndpointInterface = APIClient.getClient().create(ApiEndpointInterface.class);

		Call<JsonElement> call = apiEndpointInterface.getFollowupsToday(objSaveData.getString("user_id"), Integer.parseInt(objSaveData.getString("client_id")));

		call.enqueue(new Callback<JsonElement>() {
			@Override
			public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
				//progressDialog.dismiss();
				Log.d("URL", "=====" + response.raw().request().url());
				Log.e("getting our vicar", ""+response.body().toString());

				if(response.isSuccessful() && response.body()!=null) {
					try {
						//dbHelper.deleteTable(DbContract.DbEntry.TABLE_OUR_BISHOPS);

						JSONArray jsonArray =  new JSONArray(response.body().toString());
						Log.e("Array Length",""+jsonArray.length());

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject1 = jsonArray.getJSONObject(i);

							//     Log.e("Name ",""+jsonObject1.getString("name"));
							todayFollowUp = new TodayFollowUp(jsonObject1.getString("LeadName")
									, jsonObject1.getString("NxtInteractDate"));

							//);

							dataList.add(todayFollowUp);
							//dbHelper.addOurBishops(bishopModel);


						}


                        Intent intent =  new Intent(context,TodayFollowUpActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("arrayList",dataList);

                        context.startActivity(intent);
						//todayFollowupAdapter.notifyDataSetChanged();


					} catch (Exception e) {
						e.printStackTrace();
						// Log.e("Exception bishop",e.getMessage());
					}
				}

			}

			@Override
			public void onFailure(Call<JsonElement> call, Throwable t) {

			}
		});

	}
}
