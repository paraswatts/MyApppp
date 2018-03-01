package com.nitesh.brill.saleslines._User_Classes.User_Fragment;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nitesh.brill.saleslines.Common_Files.SaveData;
import com.nitesh.brill.saleslines.Common_Files.updateNotIcon;
import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioDbHelper;
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.NotificationCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link User_NotificationView_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_NotificationView_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,SwipeRefreshLayout.OnRefreshListener {
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
    public User_NotificationView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public static User_NotificationView_Fragment newInstance(String param1, String param2) {
        User_NotificationView_Fragment fragment = new User_NotificationView_Fragment();
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
        ImageView tv_empty_notifications = (ImageView) view.findViewById(R.id.tv_empty_notifications);
        tv_empty_notifications.setImageBitmap(getMarkerBitmapFromView());
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
                        builder = new AlertDialog.Builder(getContext(), R.style.DeleteDialogTheme);
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
                            .setIcon(R.drawable.ic_caution_sign)
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

    private Bitmap getMarkerBitmapFromView() {
        final View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.no_notifications, null);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
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
