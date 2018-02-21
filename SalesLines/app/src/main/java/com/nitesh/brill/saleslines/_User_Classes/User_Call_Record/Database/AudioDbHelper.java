/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Audio;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Notification;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Reminders;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.UserCoordinates;

import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_AUDIO_ENQ_ID;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_AUDIO_FILE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_AUDIO_MOBILE_NUMBER;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EMAIL;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EXTRA_MESSAGE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FILE_PATH;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FIRED;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LAST_CALLED;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_GET_DATE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_GET_TIME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_MOBILE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_SMS;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_STATUS;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_MISSED_REMINDERS;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_READ_STATE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TARGET_ACHIEVED;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TITLE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TOTAL_TARGET;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TYPE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_USER_NAME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATION_DATE_TIME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATION_IMAGE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATION_MESSAGE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_NOTIFICATION_USER_ROLE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_ALARM_TYPE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_EXTRA_MESSAGE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_GIF_URL;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_LEAD_NUMBER;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_ROLE_TYPE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_TIME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_TOTAL_TARGET;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_ID;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_MESSAGE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_TARGET_ACHIEVED;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_SET;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_REMINDER_USER_NAME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_COORDINATES_TIME;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_COORDINATES_UPLOADED;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_EMAIL;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_LATITUDE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_LONGITUDE;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.COLUMN_USER_SAMPLE_COLUMN;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.TABLE_AUDIO;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.TABLE_REMINDERS;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry.TABLE_USER_COORDINATES;
import static com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry._ID;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class AudioDbHelper extends SQLiteOpenHelper {
    private static String SQL_CREATE_AUDIO_TABLE;

    private static String SQL_CREATE_NOTIFICATIONS_MISSED_FOLLOWUPS;

    private static String SQL_CREATE_REMINDER_TABLE;

    private static String SQL_CREATE_COORDINATES_TABLE;

    Context context;

    public static final String LOG_TAG = AudioDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "SalesLine.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 5;

    /**
     * Constructs a new instance of {@link AudioDbHelper}.
     *
     * @param context of the app
     */
    public AudioDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        SQL_CREATE_AUDIO_TABLE = "CREATE TABLE " + AudioEntry.TABLE_AUDIO + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AUDIO_FILE + " TEXT NOT NULL " + ")";


        SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " + AudioEntry.TABLE_REMINDERS + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REMINDER_ID + " INTEGER,"
                + COLUMN_REMINDER_ALARM_TYPE + " TEXT,"
                + COLUMN_REMINDER_MESSAGE + " TEXT,"
                + COLUMN_REMINDER_EXTRA_MESSAGE + " TEXT,"
                + COLUMN_REMINDER_GIF_URL + " TEXT,"
                + COLUMN_REMINDER_SET + " TEXT,"
                + COLUMN_REMINDER_TOTAL_TARGET + " TEXT,"
                + COLUMN_REMINDER_TARGET_ACHIEVED + " TEXT,"
                + COLUMN_REMINDER_TIME + " TEXT,"
                + COLUMN_REMINDER_LEAD_NUMBER + " TEXT,"
                + COLUMN_REMINDER_USER_NAME + " TEXT,"
                + COLUMN_REMINDER_ROLE_TYPE + " TEXT,"
                + COLUMN_NOTIFICATIONS_EMAIL + " TEXT " + ")";


        SQL_CREATE_NOTIFICATIONS_MISSED_FOLLOWUPS = "CREATE TABLE " + AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTIFICATION_USER_ROLE + " TEXT,"
                + COLUMN_NOTIFICATION_DATE_TIME + " TEXT,"
                + COLUMN_NOTIFICATION_IMAGE + " TEXT,"
                + COLUMN_NOTIFICATIONS_TYPE + " TEXT,"
                + COLUMN_NOTIFICATIONS_MISSED_REMINDERS + " TEXT,"
                + COLUMN_NOTIFICATIONS_EXTRA_MESSAGE + " TEXT,"
                + COLUMN_NOTIFICATIONS_TITLE + " TEXT,"
                + COLUMN_NOTIFICATIONS_TOTAL_TARGET + " TEXT,"
                + COLUMN_NOTIFICATIONS_TARGET_ACHIEVED + " TEXT,"
                + COLUMN_NOTIFICATIONS_LAST_CALLED + " TEXT,"
                + COLUMN_NOTIFICATIONS_USER_NAME + " TEXT,"
                + COLUMN_NOTIFICATIONS_FIRED + " TEXT,"
                + COLUMN_NOTIFICATIONS_FILE_PATH + " TEXT,"
                + COLUMN_NOTIFICATIONS_EMAIL + " TEXT,"
                + COLUMN_NOTIFICATIONS_LEAD_MOBILE + " TEXT,"
                + COLUMN_NOTIFICATIONS_LEAD_SMS + " TEXT,"
                + COLUMN_NOTIFICATIONS_LEAD_STATUS + " TEXT,"
                + COLUMN_NOTIFICATION_MESSAGE + " TEXT,"
                + COLUMN_NOTIFICATIONS_LEAD_GET_TIME + " TEXT,"
                + COLUMN_NOTIFICATIONS_READ_STATE + " TEXT,"
                + COLUMN_NOTIFICATIONS_LEAD_GET_DATE + " TEXT " + ")";
        SQL_CREATE_COORDINATES_TABLE = "CREATE TABLE " + AudioEntry.TABLE_USER_COORDINATES + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_LATITUDE+ " TEXT,"
                + COLUMN_USER_LONGITUDE + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_COORDINATES_TIME + " TEXT,"
                + COLUMN_USER_COORDINATES_UPLOADED + " TEXT " + ")";


        db.execSQL(SQL_CREATE_COORDINATES_TABLE);


        db.execSQL(SQL_CREATE_REMINDER_TABLE);

        db.execSQL(SQL_CREATE_AUDIO_TABLE);

        db.execSQL(SQL_CREATE_NOTIFICATIONS_MISSED_FOLLOWUPS);


    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.

        db.execSQL("ALTER TABLE " + TABLE_AUDIO +" ADD COLUMN "+ COLUMN_AUDIO_MOBILE_NUMBER);

       // onCreate(db);
    }

    public void addCoordinates(UserCoordinates userCoordinates) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_LATITUDE, userCoordinates.getLatitude());
        values.put(COLUMN_USER_LONGITUDE, userCoordinates.getLongitude());
        values.put(COLUMN_USER_EMAIL, userCoordinates.getUserEmail());
        values.put(COLUMN_USER_COORDINATES_UPLOADED, userCoordinates.getUploaded());
        values.put(COLUMN_USER_COORDINATES_TIME, userCoordinates.getLocationTime());

        db.insert(AudioEntry.TABLE_USER_COORDINATES, null, values);
        db.close();

    }

    public void addReminders(Reminders reminders) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REMINDER_ID, reminders.getmReminderId());
        values.put(COLUMN_REMINDER_TOTAL_TARGET, reminders.getmTotalTarget());
        values.put(COLUMN_REMINDER_TARGET_ACHIEVED, reminders.getmTargetAchieved());
        values.put(COLUMN_REMINDER_ALARM_TYPE, reminders.getmAlarmType());
        values.put(COLUMN_REMINDER_MESSAGE, reminders.getmMessage());
        values.put(COLUMN_REMINDER_EXTRA_MESSAGE, reminders.getmExtraMessage());
        values.put(COLUMN_REMINDER_GIF_URL, reminders.getmGifUrl());
        values.put(COLUMN_REMINDER_SET, reminders.getmReminderSet());
        values.put(COLUMN_REMINDER_TIME, reminders.getmfollowUpTime());
        values.put(COLUMN_REMINDER_LEAD_NUMBER, reminders.getmLeadNumber());
        values.put(COLUMN_REMINDER_USER_NAME, reminders.getmUserName());
        values.put(COLUMN_REMINDER_ROLE_TYPE, reminders.getmRoleType());
        values.put(COLUMN_NOTIFICATIONS_EMAIL, reminders.getmLoginEmail());

        db.insert(AudioEntry.TABLE_REMINDERS, null, values);
        db.close();

    }


    public void addAudio(Audio audio) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AUDIO_FILE, audio.getAudioRecording());
        values.put(COLUMN_AUDIO_MOBILE_NUMBER, audio.getAudioMobile());

        db.insert(AudioEntry.TABLE_AUDIO, null, values);
        db.close();

    }

    public void addNotification(Notification notification) {
        //Log.e("I am in ", "Add notification");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTIFICATION_MESSAGE, notification.getmNotificationMessage());
        values.put(COLUMN_NOTIFICATION_USER_ROLE, notification.getmNotificationUserRole());
        values.put(COLUMN_NOTIFICATION_DATE_TIME, notification.getmDateTime());
        values.put(COLUMN_NOTIFICATION_IMAGE, notification.getmImage());
        values.put(COLUMN_NOTIFICATIONS_TYPE, notification.getmNotificationsType());
        values.put(COLUMN_NOTIFICATIONS_MISSED_REMINDERS, notification.getmNotificationReminder());
        values.put(COLUMN_NOTIFICATIONS_EXTRA_MESSAGE, notification.getmExtraMessage());
        values.put(COLUMN_NOTIFICATIONS_TITLE, notification.getmTitle());
        values.put(COLUMN_NOTIFICATIONS_TOTAL_TARGET, notification.getmTarget());
        values.put(COLUMN_NOTIFICATIONS_TARGET_ACHIEVED, notification.getmTargetAchieved());
        values.put(COLUMN_NOTIFICATIONS_LAST_CALLED, notification.getmLastCalled());
        values.put(COLUMN_NOTIFICATIONS_USER_NAME, notification.getmUserName());
        values.put(COLUMN_NOTIFICATIONS_FIRED, notification.getmFired());
        values.put(COLUMN_NOTIFICATIONS_FILE_PATH, notification.getmFilePath());
        values.put(COLUMN_NOTIFICATIONS_EMAIL, notification.getmEmail());
        values.put(COLUMN_NOTIFICATIONS_LEAD_MOBILE, notification.getmLeadMobile());
        values.put(COLUMN_NOTIFICATIONS_LEAD_SMS, notification.getmLeadSMS());
        values.put(COLUMN_NOTIFICATIONS_LEAD_STATUS, notification.getmLeadStatus());
        values.put(COLUMN_NOTIFICATIONS_LEAD_GET_TIME, notification.getmLeadTime());
        values.put(COLUMN_NOTIFICATIONS_READ_STATE, notification.getReadState());
        values.put(COLUMN_NOTIFICATIONS_LEAD_GET_DATE, notification.getmLeadDate());

        //values.put(COLUMN_USER_EMAIL, user.getEmail());
        // values.put(AudioEntry.COLUMN_USER_MOBILE,user.getMobile());
        //  values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, null, values);
        db.close();

    }

    public void deleteNotifications() {
        SaveData objSaveData = new SaveData(context);
        SQLiteDatabase db = this.getWritableDatabase();
        String email = objSaveData.getString("LoginId");

        String whereClause = "email=?";
        String[] whereArgs = new String[]{email};
        db.delete(AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, whereClause, whereArgs);
    }

    public void deleteCoordinates() {
        SaveData objSaveData = new SaveData(context);
        SQLiteDatabase db = this.getWritableDatabase();
        String email = objSaveData.getString("LoginId");

        String whereClause = "email=?";
        String[] whereArgs = new String[]{email};
        db.delete(AudioEntry.TABLE_USER_COORDINATES, whereClause, whereArgs);
    }
    public int getNotCount(String email,String readState) {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS +" WHERE email ="+"'"+email+"'" + " AND  read_state="+"'"+readState+"'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        //Log.e("Query count",""+countQuery+count);

        cursor.close();
        db.close();
// return count
        return count;
    }

    public int getNoCountUserLocation(String email,String uploadState) {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_USER_COORDINATES +" WHERE user_email ="+"'"+email+"'" + " AND  coordinates_uploaded="+"'"+uploadState+"'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        //Log.e("Query count",""+countQuery+count);

        cursor.close();
        db.close();
// return count
        return count;
    }


}
