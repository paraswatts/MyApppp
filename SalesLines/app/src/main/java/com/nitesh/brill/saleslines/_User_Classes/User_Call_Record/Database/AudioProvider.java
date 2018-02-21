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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract.AudioEntry;

/**
 * {@link ContentProvider} for Pets app.
 */
public class AudioProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = AudioProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */


    private static final int REMINDER = 300;

    private static final int REMINDER_ID = 301;

    private static final int AUDIO = 200;



    private static final int AUDIO_ID = 201;

    private static final int NOTIFICATION = 100;


    private static final int NOTIFICATION_ID = 101;






    /** URI matcher code for the content URI for a single pet in the pets table */


    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #USERS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.



        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_AUDIO, AUDIO);

        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_NOTIFICATIONS, NOTIFICATION);


        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_REMINDERS, REMINDER);



        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #USER_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.



        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_AUDIO + "/#", AUDIO_ID);

        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_REMINDERS + "/#", REMINDER_ID);

        sUriMatcher.addURI(AudioContract.CONTENT_AUTHORITY, AudioContract.PATH_NOTIFICATIONS + "/#", NOTIFICATION_ID);




    }

    /** Database helper object */
    private AudioDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AudioDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case AUDIO:
                cursor = database.query(AudioEntry.TABLE_AUDIO, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;




            case AUDIO_ID:
                // For the USER_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AudioEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AudioEntry.TABLE_AUDIO, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;



            case REMINDER:
                Log.e("DB ","Query");
                cursor = database.query(AudioEntry.TABLE_REMINDERS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case REMINDER_ID:
                // For the USER_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AudioEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AudioEntry.TABLE_REMINDERS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case NOTIFICATION:
                Log.e("DB ","Query");
                cursor = database.query(AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;


            case NOTIFICATION_ID:
                // For the USER_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AudioEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;


            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AUDIO:
                //return insertUser(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    /*
    private Uri insertUser(Uri uri, ContentValues values) {
        // Check that the name is not null

        if (values.containsKey(AudioEntry.COLUMN_USER_NAME)) {
            String name = values.getAsString(AudioEntry.COLUMN_USER_NAME);
            if (name == null) {
                Toast.makeText(getContext(),"Please Enter a Name",Toast.LENGTH_SHORT);
            }
        }

        // If the {@link AudioEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(AudioEntry.COLUMN_USER_EMAIL)) {
            String email = values.getAsString(AudioEntry.COLUMN_USER_EMAIL);
            if(!email.contains("@") && !email.contains((".")))
            {
                Toast.makeText(getContext(),"Please Enter a valid Email-id",Toast.LENGTH_SHORT);

            }
            if(email == null)
            {
                Toast.makeText(getContext(),"Please Enter a Email-id",Toast.LENGTH_SHORT);

            }
        }

        // If the {@link AudioEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(AudioEntry.COLUMN_USER_MOBILE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer mobile = values.getAsInteger(AudioEntry.COLUMN_USER_MOBILE);
            if(mobile.toString() == null)
            {
                Toast.makeText(getContext(),"Please Enter a Mobile Number",Toast.LENGTH_SHORT);

            }
        }


        if (values.containsKey(AudioEntry.COLUMN_USER_PASSWORD)) {
            String password = values.getAsString(AudioEntry.COLUMN_USER_PASSWORD);
            if (password == null) {
                Toast.makeText(getContext(),"Please enter a password",Toast.LENGTH_SHORT);
            }
        }

        if (values.containsKey(AudioEntry.COLUMN_USER_REPASSWORD)) {
            String repassword = values.getAsString(AudioEntry.COLUMN_USER_REPASSWORD);
            if (repassword == null) {
                Toast.makeText(getContext(),"Please re-enter a password",Toast.LENGTH_SHORT);
            }
        }
        String password = values.getAsString(AudioEntry.COLUMN_USER_PASSWORD);
        String repassword = values.getAsString(AudioEntry.COLUMN_USER_REPASSWORD);
        if(!password.equals(repassword))
        {
            Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_SHORT);
        }
        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(AudioEntry.TABLE_USERS, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
*/

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
           // case PERSON:
         //       return updateUser(uri, contentValues, selection, selectionArgs);
         //   case PERSON_ID:
                // For the USER_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
             //   selection = AudioContract.AudioEntry._ID + "=?";
            //    selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
               // return updateUser(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /*
    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    /*
    private int updateUser(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link AudioEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.

            // Check that the name is not null

            if (values.containsKey(AudioEntry.COLUMN_USER_NAME)) {
                String name = values.getAsString(AudioEntry.COLUMN_USER_NAME);
                if (name == null) {
                    Toast.makeText(getContext(),"Please Enter a Name",Toast.LENGTH_SHORT);
                }
            }

            // If the {@link AudioEntry#COLUMN_PET_GENDER} key is present,
            // check that the gender value is valid.
            if (values.containsKey(AudioEntry.COLUMN_USER_EMAIL)) {
                String email = values.getAsString(AudioEntry.COLUMN_USER_EMAIL);
                if(email == null)
                {
                    Toast.makeText(getContext(),"Please Enter a Email-id",Toast.LENGTH_SHORT);

                }
            }

            // If the {@link AudioEntry#COLUMN_PET_WEIGHT} key is present,
            // check that the weight value is valid.
            if (values.containsKey(AudioEntry.COLUMN_USER_MOBILE)) {
                // Check that the weight is greater than or equal to 0 kg
                Integer mobile = values.getAsInteger(AudioEntry.COLUMN_USER_MOBILE);
                if(mobile.toString() == null)
                {
                    Toast.makeText(getContext(),"Please Enter a Mobile Number",Toast.LENGTH_SHORT);

                }
            }


            if (values.containsKey(AudioEntry.COLUMN_USER_PASSWORD)) {
                String password = values.getAsString(AudioEntry.COLUMN_USER_PASSWORD);
                if (password == null) {
                    Toast.makeText(getContext(),"Please enter a password",Toast.LENGTH_SHORT);
                }
            }

            if (values.containsKey(AudioEntry.COLUMN_USER_REPASSWORD)) {
                String repassword = values.getAsString(AudioEntry.COLUMN_USER_REPASSWORD);
                if (repassword == null) {
                    Toast.makeText(getContext(),"Please re-enter a password",Toast.LENGTH_SHORT);
                }
            }
            String password = values.getAsString(AudioEntry.COLUMN_USER_PASSWORD);
            String repassword = values.getAsString(AudioEntry.COLUMN_USER_REPASSWORD);
            if(!password.equals(repassword))
            {
                Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_SHORT);
            }


        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AudioContract.AudioEntry.TABLE_USERS, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }*/

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AUDIO:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AudioEntry.TABLE_AUDIO, selection, selectionArgs);
                break;
            case AUDIO_ID:
                // Delete a single row given by the ID in the URI
                selection = AudioEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AudioEntry.TABLE_AUDIO, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }



    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case AUDIO:
                return AudioEntry.CONTENT_LIST_TYPE_AUDIO;
            case AUDIO_ID:
                return AudioEntry.CONTENT_ITEM_TYPE_AUDIO;


            case REMINDER:
                return AudioEntry.CONTENT_LIST_TYPE_REMINDERS;
            case REMINDER_ID:
                return AudioEntry.CONTENT_ITEM_TYPE_REMINDERS;


            case NOTIFICATION:
                return AudioEntry.CONTENT_LIST_TYPE_NOTIFICATIONS;
            case NOTIFICATION_ID:
                return AudioEntry.CONTENT_ITEM_TYPE_NOTIFICATIONS;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
