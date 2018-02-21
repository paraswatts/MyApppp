package com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitesh.brill.saleslines.R;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Fragment.AGM_SaleClosureReminder_Fragment;
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Fragment.AGM_TargetReminder_Fragment;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Fragment.ASM_SaleClosureReminder_Fragment;
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Fragment.ASM_TargetReminder_Fragment;
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_MissedFollowupReminder_Fragment;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_SaleSaleClosureReminder_Fragment;
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_TargetReminder_Fragment;
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity;
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_FollowUpReminder_Fragment;
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_MissedFollowupReminder_Fragment;
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_SaleCloserReminder_Fragment;
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_TargetReminder_Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by paras on 13-05-2017.
 */

public class NotificationCursorAdapter extends CursorAdapter {
    private SparseBooleanArray mSelectedItemsIds;

    public NotificationCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_person, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Log.e("I am in cursor adapter", "");
        int notificationColumnMessageIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATION_MESSAGE);
        int notificationUserNameIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_USER_NAME);
        int notificationRoleTypeIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATION_USER_ROLE);
        int notificationDateTimeColumnIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATION_DATE_TIME);
        int notificationGetTimeColumnIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_GET_TIME);
        int notificationGetDateColumnIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_GET_DATE);

        int notificationImageUrlIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FILE_PATH);
        int notificationTypeIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TYPE);
        int notificationNumberOfReminders = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_MISSED_REMINDERS);
        int notificationTitleIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TITLE);
        int notificationTotalTargetIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TOTAL_TARGET);
        int notificationTargetAchievedIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_TARGET_ACHIEVED);
        final int notificationExtraMessageIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_EXTRA_MESSAGE);
        final int notificationLastCalledIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LAST_CALLED);
        final int notificationFiredIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_FIRED);
        final int notificationLeadMobileIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_MOBILE);
        final int notificationLeadSMSIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_SMS);
        final int notificationLeadStatusIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATIONS_LEAD_STATUS);

        TextView textViewNotificationMessage = (TextView) view.findViewById(R.id.tv_Notification_List);
        TextView textViewNotificationTime = (TextView) view.findViewById(R.id.ml);

        //TextView tv_Notification_Date_Time = (TextView) view.findViewById(R.id.tv_Notification_Date_Time);
        final TextView ml = (TextView) view.findViewById(R.id.ml);

        //=========== Extract Strings from columns of database ============\\

        final String notificationsUserName = cursor.getString(notificationUserNameIndex);
        String notificationDateTime = cursor.getString(notificationDateTimeColumnIndex);
        String notificationUserRole = cursor.getString(notificationRoleTypeIndex);
        final String missedReminders = cursor.getString(notificationNumberOfReminders);
        final String notificationMessage = cursor.getString(notificationColumnMessageIndex);
        final String notificationTitle = cursor.getString(notificationTitleIndex);
        final String notificationType = cursor.getString(notificationTypeIndex);
        final String imageUrl = cursor.getString(notificationImageUrlIndex);
        final String extraMessage = cursor.getString(notificationExtraMessageIndex);
        final String totalTarget = cursor.getString(notificationTotalTargetIndex);
        final String targetAchieved = cursor.getString(notificationTargetAchievedIndex);
        final String lastCalled = cursor.getString(notificationLastCalledIndex);
        final String fired = cursor.getString(notificationFiredIndex);
        final String leadMobile = cursor.getString(notificationLeadMobileIndex);
        final String leadSMS = cursor.getString(notificationLeadSMSIndex);
        final String leadStatus = cursor.getString(notificationLeadStatusIndex);
        final String leadTime = cursor.getString(notificationGetTimeColumnIndex);
        final String leadDate = cursor.getString(notificationGetDateColumnIndex);


        Log.e("notificationDateTime=", leadTime +leadDate);
        //Log.e("USername in cursor ",notificationsUserName);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_notification_item);


        //tv_Notification_Date_Time.setText(notificationDateTime);

        textViewNotificationMessage.setText(notificationMessage);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(leadDate));
            textViewNotificationTime.setText(formatter.format(calendar.getTime()));
        }catch (NumberFormatException  e)
        {
            e.printStackTrace();
        }
        //textViewNotificationTime.setText(leadDate + " "+leadTime);

///
//        if (notificationType.equalsIgnoreCase("assignlead") || notificationType.equalsIgnoreCase("duplicateLead")) {
//
//            ml.setText("NS");
//            ml.setBackgroundColor(context.getResources().getColor(R.color.red));
//
//        }
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (notificationType.equalsIgnoreCase("assignlead") || notificationType.equalsIgnoreCase("duplicateLead")) {
//
//                    ml.setText("SL");
//                    ml.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//
//                }
//
//
//
//            }
//        });
//           rl.setOnClickListener( new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(notificationType.equals("missedFollowups")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("image",imageUrl);
//                        bundle.putString("reminders",missedReminders);
//                        Fragment fragment = new User_MissedFollowupReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((User_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("saleClosureReminder")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("image",imageUrl);
//                        bundle.putString("message",notificationMessage);
//                        bundle.putString("title" , notificationTitle);
//                        bundle.putString("extra" , extraMessage);
//                        Fragment fragment = new User_SaleCloserReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((User_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("targetReminder")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("totalTarget",totalTarget);
//                        bundle.putString("targetAchieved",targetAchieved);
//
//
//                        Fragment fragment = new User_TargetReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((User_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("followUpReminder")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("leadMobile",leadMobile);
//                        bundle.putString("leadSMS",leadSMS);
//                        bundle.putString("title",notificationTitle);
//                        bundle.putString("message",notificationMessage);
//                        bundle.putString("lastCalled",lastCalled);
//                        bundle.putString("image",imageUrl);
//
//
//                        Fragment fragment = new User_FollowUpReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((User_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("saleClosureManagerReminder")) {
//                        Bundle bundle = new Bundle();
//
//                        bundle.putString("userName",notificationsUserName);
//                        Fragment fragment = new Manager_SaleSaleClosureReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((Manager_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("managerMissedFollowups")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("image",imageUrl);
//                        bundle.putString("reminders",missedReminders);
//                        bundle.putString("userName", notificationsUserName);
//                        Fragment fragment = new Manager_MissedFollowupReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((Manager_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//                    if(notificationType.equals("managerTargetReminder")) {
//                        Bundle bundle = new Bundle();
//
//
//                        bundle.putString("totalTarget",totalTarget);
//                        bundle.putString("targetAchieved",targetAchieved);
//
//                        Fragment fragment = new Manager_TargetReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((Manager_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("gmSaleClosureReminder")) {
//                        Bundle bundle = new Bundle();
//
//                        bundle.putString("userName",notificationsUserName);
//                        Fragment fragment = new ASM_SaleClosureReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((GM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("gmTargetReminder")) {
//                        Bundle bundle = new Bundle();
//
//
//                        bundle.putString("totalTarget",totalTarget);
//                        bundle.putString("targetAchieved",targetAchieved);
//
//                        Fragment fragment = new ASM_TargetReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((GM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("asmSaleClosureReminder")) {
//                        Bundle bundle = new Bundle();
//                        Log.e("asm sale close",notificationType);
//
//                        bundle.putString("userName",notificationsUserName);
//                        Fragment fragment = new ASM_SaleClosureReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((ASM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("asmTargetReminder")) {
//                        Bundle bundle = new Bundle();
//
//
//                        bundle.putString("totalTarget",totalTarget);
//                        bundle.putString("targetAchieved",targetAchieved);
//
//                        Fragment fragment = new ASM_TargetReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((ASM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("agmSaleClosureReminder")) {
//                        Bundle bundle = new Bundle();
//
//                        bundle.putString("userName",notificationsUserName);
//                        Fragment fragment = new AGM_SaleClosureReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((AGM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                    if(notificationType.equals("agmTargetReminder")) {
//                        Bundle bundle = new Bundle();
//
//
//                        bundle.putString("totalTarget",totalTarget);
//                        bundle.putString("targetAchieved",targetAchieved);
//
//                        Fragment fragment = new AGM_TargetReminder_Fragment();
//                        fragment.setArguments(bundle);
//                        if (fragment != null) {
//                            FragmentTransaction ft = ((AGM_Home_Activity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null);
//                            ft.replace(R.id.content_frame, fragment);
//                            ft.commit();
//                        }
//                    }
//
//                }
//            });


    }


    // get List after update or delete


    public void toggleSelection(int position) {

        selectView(position, !mSelectedItemsIds.get(position));

    }


    // Remove selection after unchecked

    public void removeSelection() {

        mSelectedItemsIds = new SparseBooleanArray();

        notifyDataSetChanged();

    }


    // Item checked on selection

    public void selectView(int position, boolean value) {

        if (value)

            mSelectedItemsIds.put(position, value);

        else

            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();

    }


    // Get number of selected item

    public int getSelectedCount() {

        return mSelectedItemsIds.size();

    }


    public SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;

    }
}
