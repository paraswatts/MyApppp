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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class AudioContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private AudioContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.nitesh.brill.saleslines";



    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_AUDIO = "Audio";

    public static final String PATH_NOTIFICATIONS = "Notifications";


    public static final String PATH_REMINDERS = "Reminders";


    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */


    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class AudioEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */






        public static final Uri CONTENT_URI_REMINDERS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REMINDERS);

        public static final Uri CONTENT_URI_AUDIO = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AUDIO);

        public static final Uri CONTENT_URI_NOTIFICATION = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFICATIONS);







        /**
         * The MIME type of the {@link #CONTENT_URI_AUDIO} for a list of pets.

*/
        public static final String CONTENT_LIST_TYPE_AUDIO = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUDIO;

        public static final String CONTENT_LIST_TYPE_REMINDERS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMINDERS;

        public static final String CONTENT_LIST_TYPE_NOTIFICATIONS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;








        public static final String CONTENT_ITEM_TYPE_REMINDERS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REMINDERS;

        public static final String CONTENT_ITEM_TYPE_AUDIO =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUDIO;

        public static final String CONTENT_ITEM_TYPE_NOTIFICATIONS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFICATIONS;





        /** Name of database table for pets */
        public final static String TABLE_AUDIO = "audio_recordings";

        public final static String TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS = "notifications_missed_followups";

        public final static String TABLE_REMINDERS = "reminders";

        public final static String TABLE_USER_COORDINATES = "user_coordinates";



        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */

        public final static String COLUMN_USER_LATITUDE ="latitude";

        public final static String COLUMN_USER_LONGITUDE ="longitude";

        public final static String COLUMN_USER_EMAIL ="user_email";

        public final static String COLUMN_USER_COORDINATES_UPLOADED ="coordinates_uploaded";

        public final static String COLUMN_USER_COORDINATES_TIME ="coordinates_time";

        public final static String COLUMN_USER_SAMPLE_COLUMN ="sample_column";

        public final static String COLUMN_AUDIO_FILE ="filename";

        public final static String COLUMN_AUDIO_ENQ_ID ="audio_enqid";

        public final static String COLUMN_AUDIO_MOBILE_NUMBER ="audio_mobile";

        public final static String COLUMN_NOTIFICATION_USER_ROLE ="user_role";

        public final static String COLUMN_NOTIFICATION_MESSAGE ="message";

        public final static String COLUMN_NOTIFICATION_DATE_TIME ="date";

        public final static String COLUMN_NOTIFICATION_IMAGE ="image";

        public final static String COLUMN_NOTIFICATIONS_TYPE = "notification_type";

        public final static String COLUMN_NOTIFICATIONS_MISSED_REMINDERS = "missed_reminders";

        public final static String COLUMN_NOTIFICATIONS_EXTRA_MESSAGE = "extra_message";

        public final static String COLUMN_NOTIFICATIONS_TITLE = "title";

        public final static String COLUMN_NOTIFICATIONS_TOTAL_TARGET = "total_target";

        public final static String COLUMN_NOTIFICATIONS_TARGET_ACHIEVED = "target_achieved";

        public final static String COLUMN_NOTIFICATIONS_LAST_CALLED = "last_called";

        public final static String COLUMN_NOTIFICATIONS_USER_NAME = "user_name";


        public final static String COLUMN_NOTIFICATIONS_FIRED = "fired";

        public final static String COLUMN_NOTIFICATIONS_FILE_PATH = "file_path";

        public final static String COLUMN_NOTIFICATIONS_EMAIL = "email";

        public final static String COLUMN_NOTIFICATIONS_LEAD_MOBILE = "mobile_number";

        public final static String COLUMN_NOTIFICATIONS_LEAD_SMS = "lead_sms";
        public final static String COLUMN_NOTIFICATIONS_LEAD_STATUS = "lead_status";
        public final static String COLUMN_NOTIFICATIONS_LEAD_GET_TIME = "lead_receive_time";

        public final static String COLUMN_NOTIFICATIONS_LEAD_GET_DATE = "lead_receive_date";


        public final static String COLUMN_NOTIFICATIONS_READ_STATE = "read_state";

        public final static String COLUMN_REMINDER_ID = "reminder_id";

        public final static String COLUMN_REMINDER_TOTAL_TARGET = "total_target";

        public final static String COLUMN_REMINDER_TARGET_ACHIEVED = "target_achieved";

        public final static String COLUMN_REMINDER_TIME = "follow_up_time";

        public final static String COLUMN_REMINDER_LEAD_NUMBER = "lead_number";

        public final static String COLUMN_REMINDER_USER_NAME = "user_name";


        public final static String COLUMN_REMINDER_ALARM_TYPE = "alarm_type";

        public final static String COLUMN_REMINDER_MESSAGE = "reminder_message";

        public final static String COLUMN_REMINDER_EXTRA_MESSAGE = "reminder_extra_messsage";

        public final static String COLUMN_REMINDER_GIF_URL = "gif_url";

        public final static String COLUMN_REMINDER_SET = "reminder_set";

        public final static String COLUMN_REMINDER_ROLE_TYPE = "role_type";









    }

}

