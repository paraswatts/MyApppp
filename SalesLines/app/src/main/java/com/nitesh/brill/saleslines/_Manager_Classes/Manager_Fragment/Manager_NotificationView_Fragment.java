package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.brill.nitesh.punjabpool.Common.BaseFragment;
import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.GM_NotificationView_Fragment;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.NotificationCursorAdapter;
import com.nitesh.brill.saleslines.Common_Files.updateNotIcon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Manager_NotificationView_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manager_NotificationView_Fragment extends BaseFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> ,SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    NotificationCursorAdapter notificationCursorAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    public Manager_NotificationView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    public static Manager_NotificationView_Fragment newInstance(String param1, String param2) {
        Manager_NotificationView_Fragment fragment = new Manager_NotificationView_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        notificationCursorAdapter = new NotificationCursorAdapter(getActivity(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("I am in Notification ", "Fragment");


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_notification, container, false);
        TextView tv_empty_notifications = (TextView) view.findViewById(R.id.tv_empty_notifications);
        listView = (ListView) view.findViewById(R.id.lv_Notification_List);
        listView.setEmptyView(tv_empty_notifications);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.blueColorPrimary));

        listView.setAdapter(notificationCursorAdapter);
        ImageButton bt_delete_notification = (ImageButton) view.findViewById(R.id.bt_delete_notification);

        bt_delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (notificationCursorAdapter.getCount() > 0) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                    }
                    builder.setTitle("Delete notifications")
                            .setMessage("Are you sure you want to delete all notifications?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    AudioDbHelper audioDbHelper = new AudioDbHelper(getContext());

                                    audioDbHelper.deleteNotifications();
                                    notificationCursorAdapter.swapCursor(null);
                                    notificationCursorAdapter.notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    Log.e("I am in Notification ", "List is empty");
                }
            }
        });

        changeReadState();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public void changeReadState(){

        SaveData objSaveData = new SaveData(getContext());
        String email = objSaveData.getString("LoginId");
        String[] selectionArgs = {email};
        PendingIntent pendingIntent;
        String selection = "email=?";

        AudioDbHelper userDbHelper = new AudioDbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = userDbHelper.getWritableDatabase();
        Uri alarmSound = Uri.parse(objSaveData.getString("notificationSound"));

        String columns[] = {AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FIRED, AudioContract.AudioEntry._ID};
        Cursor query = sqLiteDatabase.query(AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS, //Table to query
                null, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //filter by row groups
                null, null, null);//The values for the WHERE clause


        Log.e("Email",""+email);

        try
        {
            // Log.e("I am here","async while loop");

            while(query.moveToNext()) {
                //  Log.e("I am here","async while loop");
                if (email.equals(query.getString(query.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EMAIL))) ) {


                    //Log.e("Email in async is", query.getString(1) +"yes or no"+query.getString(12));

                    if (query.getString(query.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_READ_STATE)).equals("no")) {


                        ContentValues content_Values = new ContentValues();
                        content_Values.put(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_READ_STATE, "yes");
                        sqLiteDatabase.update(AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS,
                                content_Values,
                                AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EMAIL + "= ?",
                                new String[]{email});




                    }
                }
            }
        }
        finally{

            Log.e("Calling interface","Method");
            if(userDbHelper.getNotCount(objSaveData.getString("LoginId"),"no")<=0)
            {
                updateNotIcon updateNotIcon = (com.nitesh.brill.saleslines.Common_Files.updateNotIcon)getActivity();
                updateNotIcon.updateNotIcon();

            }
            query.close();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SaveData objSaveData = new SaveData(getContext());

        Log.e("I am in Create Loader", "");
        String userRole = objSaveData.getString("LoginId");
        Log.e("userRole", userRole);


        String[] selectionArgs = {userRole};

        String selection = "email=?";


        return new android.support.v4.content.CursorLoader(getActivity(),
                AudioContract.AudioEntry.CONTENT_URI_NOTIFICATION, null, selection, selectionArgs, "lead_receive_date DESC"
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notificationCursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notificationCursorAdapter.swapCursor(null);
    }


    @Override
    public void onRefresh() {
        changeReadState();
        getLoaderManager().restartLoader(0, null, this);
        swipeRefreshLayout.setRefreshing(false);


    }

}
