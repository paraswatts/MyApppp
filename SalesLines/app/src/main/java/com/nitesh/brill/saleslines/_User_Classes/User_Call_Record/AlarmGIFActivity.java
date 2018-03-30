package com.nitesh.brill.saleslines._User_Classes.User_Call_Record;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmGIFActivity extends AppCompatActivity {

    //    objSaveData.save("leadSMS",leadSMS);
    ImageView gif;
    ImageView close;
    SaveData saveData;
    String gifMessage;
    TextView tv_achieved, tv_AchiveTarget, tv_target_achieved_alarm,tv_total_target_alarm,tv_TotalTarget;
    TextView tv_gif_message, tv_gif_message_extra;
    TextView tv_current_time;
    Button bt_sms;
    LinearLayout targetLayout, ll_OtherReminder;


    String TAG = "=======AlarmGIFActivity======";
    ProgressBar progressBar;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private Vibrator vibrator;
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        Log.e("Alarm", "Alarm Fired");
        setContentView(R.layout.testlayout);
        saveData = new SaveData(getApplicationContext());
        gif = (ImageView) findViewById(R.id.gif);
        close = (ImageView) findViewById(R.id.close);
        tv_gif_message = (TextView) findViewById(R.id.tv_gif_message);
        tv_gif_message_extra = (TextView) findViewById(R.id.tv_gif_message_extra);
        targetLayout = (LinearLayout) findViewById(R.id.targetLayout);
        ll_OtherReminder = (LinearLayout) findViewById(R.id.ll_OtherReminder);

        //   rll1 = (RelativeLayout) findViewById(R.id.rll1);
        tv_TotalTarget = (TextView) findViewById(R.id.tv_TotalTarget);
        tv_AchiveTarget = (TextView) findViewById(R.id.tv_AchiveTarget);



        tv_total_target_alarm = (TextView) findViewById(R.id.tv_total_target_alarm);
        tv_target_achieved_alarm = (TextView) findViewById(R.id.tv_target_achieved_alarm);

        tv_TotalTarget = (TextView) findViewById(R.id.tv_TotalTarget);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        bt_sms = (Button) findViewById(R.id.bt_sms);
        playSound(this, getAlarmUri());

        progressBar = (ProgressBar) findViewById(R.id.gif_progress);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        targetLayout.setVisibility(View.GONE);
        ll_OtherReminder.setVisibility(View.VISIBLE);


        bt_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("=========", "===========" + getIntent().getStringExtra("sendMessage"));

                if (getIntent().getStringExtra("sendMessage") == null) {


                    if (ActivityCompat.checkSelfPermission(AlarmGIFActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmGIFActivity.this, Manifest.permission.SEND_SMS)) {
                            //Show Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(AlarmGIFActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ActivityCompat.requestPermissions(AlarmGIFActivity.this, new String[]{Manifest.permission.SEND_SMS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else if (permissionStatus.getBoolean(Manifest.permission.SEND_SMS, false)) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(AlarmGIFActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sentToSettings = true;
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            //just request the permission
                            ActivityCompat.requestPermissions(AlarmGIFActivity.this, new String[]{Manifest.permission.SEND_SMS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }

                        SharedPreferences.Editor editor = permissionStatus.edit();
                        editor.putBoolean(Manifest.permission.SEND_SMS, true);
                        editor.commit();


                    } else {

                        proceedAfterPermission();

                        //You already have the permission, just go ahead.
                    }


                } else {
                    AlarmGIFActivity.this.finish();                }
            }

        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                }
                if (vibrator != null) {
                    vibrator.cancel();

                }
                AlarmGIFActivity.this.finish();
                //finish();
            }
        });
        String gifUrl = getIntent().getStringExtra("gifUrl");
        String currentDateTimeString = new SimpleDateFormat("dd-MMM-yyyy hh:mm:a").format(new Date());


        tv_current_time.setText(currentDateTimeString);

        Log.d("==activityopen===", "4th");
        try {
            if (getIntent().getStringExtra("alarmType") != null) {
                int alarmId = getIntent().getIntExtra("alarmId", 0);
                Log.e("Alarm ID", "" + alarmId);
                if (getIntent().getStringExtra("alarmType").equals("missed")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 300, 0, 0);

                    LinearLayout.LayoutParams paramsGif = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    paramsGif.setMargins(0, 350, 0, 0);

                    LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    paramsText.setMargins(0, 0, 0, -50);
                    bt_sms.setVisibility(View.GONE);
                    try {
                        gifMessage = getIntent().getStringExtra("gifMessage");
                        Log.e("", gifMessage);

                        showGIF(gifUrl);
                        tv_gif_message.setText(gifMessage);
                        tv_gif_message.setLayoutParams(paramsText);
                        // tv_gif_message.setLayoutParams(params);
                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_gif_message_extra.setText(gifExtraMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deleteReminder(alarmId);


                } else if (getIntent().getStringExtra("alarmType").equals("saleclouser")) {
                    try {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        bt_sms.setVisibility(View.GONE);
                        params.setMargins(0, 350, 0, 0);

                        gifMessage = getIntent().getStringExtra("gifMessage");

                        showGIF(gifUrl);
                        tv_gif_message.setText(gifMessage);
                        // tv_gif_message.setLayoutParams(params);

                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_gif_message_extra.setText(gifExtraMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deleteReminder(alarmId);


                } else if (getIntent().getStringExtra("alarmType").equals("saleclouserManager")) {

                    try {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        bt_sms.setVisibility(View.GONE);
                        params.setMargins(0, 350, 0, 0);

                        gifMessage = getIntent().getStringExtra("gifMessage");

                        showGIF(gifUrl);
                        tv_gif_message.setText(gifMessage);
                        // tv_gif_message.setLayoutParams(params);

                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_gif_message_extra.setText(gifExtraMessage);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deleteReminder(alarmId);

                } else if (getIntent().getStringExtra("alarmType").equals("follow")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(0, 350, 0, 0);
                    try {
                        gifMessage = getIntent().getStringExtra("gifMessage");
                        Log.e("", gifMessage);
                        tv_gif_message.setText(gifMessage);
                        // tv_gif_message.setLayoutParams(params);
                        tv_gif_message.setTextSize(50f);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

                        tv_gif_message.setTypeface(boldTypeface);
                        tv_gif_message.setTextColor(getResources().getColor(R.color.colorPrimary));
                        showGIF(gifUrl);

                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_gif_message_extra.setText(gifExtraMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deleteReminder(alarmId);
                } else if (getIntent().getStringExtra("alarmType").equals("target")) {
                    try {

                        targetLayout.setVisibility(View.VISIBLE);
                        ll_OtherReminder.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        bt_sms.setVisibility(View.GONE);
                        params.setMargins(0, 300, 0, 0);
                        // tv_gif_message.setLayoutParams(params);

                        gifMessage = getIntent().getStringExtra("gifMessage");
                        tv_TotalTarget.setText(gifMessage);
                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_AchiveTarget.setText(gifExtraMessage);
                        String gif_target_alarm = getIntent().getStringExtra("totalTarget");
                        tv_total_target_alarm.setText(gif_target_alarm);
                        String gif_target_alarm1 = getIntent().getStringExtra("targetAchieved");
                        tv_target_achieved_alarm.setText(gif_target_alarm1);
                        Log.e("====targetAchieved==", "====== targetAchieved=====" + getIntent().getStringExtra("targetAchieved"));
                        Log.e("====totalTarget==", "====== totalTarget=====" + getIntent().getStringExtra("totalTarget"));
                        deleteReminder(alarmId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (getIntent().getStringExtra("alarmType").equals("smduplicityReminder")) {
                    try {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        bt_sms.setVisibility(View.GONE);
                        params.setMargins(0, 350, 0, 0);
                        gifMessage = getIntent().getStringExtra("gifMessage");
                        showGIF(gifUrl);
                        tv_gif_message.setText(gifMessage);
                        // tv_gif_message.setLayoutParams(params);

                        String gifExtraMessage = getIntent().getStringExtra("extraMessage");
                        tv_gif_message_extra.setText(gifExtraMessage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    deleteReminder(alarmId);

                }
            } else {
                showGIF("https://media.giphy.com/media/3oEjHS0LAZXyNo0eXu/giphy.gif");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void proceedAfterPermission() {

        try {
            String msg = "";
            //We've got the permission, now we can proceed further
            SmsManager sm = SmsManager.getDefault();
            String number = getIntent().getStringExtra("leadNumber");
//            Log.e("Number ", number);

            if (saveData.getString("leadSMS").isEmpty()) {
                msg = getIntent().getStringExtra("userName") + " will call you at " + getIntent().getStringExtra("followUpTime");
                sm.sendTextMessage(number, null, msg, null, null);
            } else {

                sm.sendTextMessage(number, null, saveData.getString("leadSMS").toString(), null, null);
            }
        }catch (Exception e)
        {
           e.printStackTrace();
        }


        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();

        }

        Log.e("====No permission==", "====== No permission=====");
        AlarmGIFActivity.this.finish();
        //finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmGIFActivity.this, Manifest.permission.SEND_SMS)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(AlarmGIFActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(AlarmGIFActivity.this, new String[]{Manifest.permission.SEND_SMS}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(AlarmGIFActivity.this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(AlarmGIFActivity.this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void deleteReminder(int alarmId) {


        AudioDbHelper audioDbHelper = new AudioDbHelper(getApplicationContext());
        String query = "Delete from " + AudioContract.AudioEntry.TABLE_REMINDERS + " where reminder_id = " + alarmId;
        Log.e("Delete Query", "" + query);
        SQLiteDatabase sqLiteDatabase = audioDbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL(query);

        sqLiteDatabase.close();


    }

    private void showGIF(String gifUrl) {

        Glide
                .with(this)
                .asGif()
                .load(gifUrl)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return true;
                    }
//
                })
                .into(gif);

//        Glide
//                .with(this)
//                .asGif()
//                .load(gifUrl)
//                .into(new SimpleTarget<GifDrawable>() {
//                    @Override
//                    public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
//                        progressBar.setVisibility(View.GONE);
//                        gif.setImageDrawable(resource);
//                    }
//                });
    }

    private void playSound(Context context, Uri alert) {

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.e("Ringer Mode","Silent");
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{1000, 1000, 1000, 1000, 1000}, 0);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.e("Ringer Mode","Vibrate");
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{1000, 1000, 1000, 1000, 1000}, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.e("Ringer Mode","Normal"+alert +saveData.getBoolean("vibrateSound"));
                //if (saveData.getBooleanReminder("vibrateSound")) {
                    mMediaPlayer = new MediaPlayer();
                    try {
                        Log.e("Playing sound","Yes");
                        mMediaPlayer.setDataSource(context, alert);
                        final AudioManager audioManager = (AudioManager) context
                                .getSystemService(Context.AUDIO_SERVICE);
                        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                            Log.e("Playing sound","Yes 2");

                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                            mMediaPlayer.prepare();
                            mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();
                        }
                    } catch (IOException e) {
                        Log.e("OOPS","OOPS");
                    }
                    Log.e("Playing sound","Yes3");

                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{1000, 1000, 1000, 1000, 1000}, 0);

//                } else {
//                    Log.e("Playing sound","Yes4");
//
//                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibrator.vibrate(new long[]{1000, 1000, 1000, 1000, 1000}, 0);
//                }
                break;
        }


    }

    private Uri getAlarmUri() {

        Uri alert = null;
        Log.e("notificationSound",saveData.getString("notificationSound"));

//        objSaveData.save("notificationSound", mUri.toString())
        if(saveData.getString("notificationSound").equals("notificationSound"))
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
            alert = Uri.parse(saveData.getString("notificationSound"));
            Log.e("Alarm sound Uri 1",Uri.parse(saveData.getString("notificationSound")).toString());
        }
        return alert;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();

        }
    }
}
