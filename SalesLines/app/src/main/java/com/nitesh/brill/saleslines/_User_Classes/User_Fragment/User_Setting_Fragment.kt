package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.firebase.jobdispatcher.*
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.LocationJobService
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.LocationSharingReceiver
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.ScheduledJobService
import com.nitesh.brill.saleslines._User_Classes.User_Location.GPSTracker_DUP

import kotlinx.android.synthetic.main.fragment_user_setting.*
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startService
import java.util.*


class User_Setting_Fragment : BaseFragment() {

    private var mFragment: Fragment? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    internal lateinit var packageName: String

    internal var __permissionsRequired = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_user_setting, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)

        packageName = "com.nitesh.brill.saleslines";

        if (!objSaveData.getString("mSyncStateName").equals("mSyncStateName")) {

            tv_SyncNetwork.setText("Sync over :- " + objSaveData.getString("mSyncStateName"))
        }

        //==================\\
        tv_Notification.setOnClickListener {
            mFragment = User_Change_Notification_Sound_Fragment.newInstance("", "")
            callFragment(mFragment)
        }
        //==================\\
        tv_Change.setOnClickListener {
            mFragment = User_Change_Background_Fragment.newInstance("", "")
            callFragment(mFragment)
        }
        //==================\\
        tv_About.setOnClickListener {
            mFragment = User_About_Fragment.newInstance("", "")
            callFragment(mFragment)
        }


        //====================\\
        tv_SyncNetwork.setOnClickListener {

            var mArray : MutableList<String> = ArrayList()
            mArray.add("Wifi")
            mArray.add("Wifi/Mobile data")
            mArray.add("Mobile data")
            selector("Sync Options", mArray) { dialogInterface, i ->


                objSaveData.save("mSyncState", "" + i)
                objSaveData.save("mSyncStateName", "" + mArray.get(i).toString())
                tv_SyncNetwork.setText("Sync over :- " + objSaveData.getString("mSyncStateName"))
            }


        }

        Log.e("Location sharing",""+objSaveData.getString("locationSharing"))


        if (objSaveData.getString("locationSharing").equals("yes")) {
            bt_location.text = "Stop Location Sharing"
            //setAlarm()


        } else {
            bt_location.text = "Start Location Sharing"
            //cancelAlarm()

        }
        bt_location.setOnClickListener{
            if (objSaveData.getString("locationSharing").equals("yes")) {
                bt_location.text = "Start Location Sharing"
                objSaveData.save("locationSharing", "no")
                cancelAlarm()
            }
            else {
              getPermissions()


            }
        }







//        if (isConnectedWifi(context)) {
//            // objUsefullData.showMsgOnUI("Wifi Connected")
//        }
//        if (isConnectedMobile(context)) {
//            // objUsefullData.showMsgOnUI("Mobile Data Connected")
//        }

    }


    private fun getPermissions() {


        if (checkSelfPermission(getActivity(), __permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(getActivity(), __permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                ) {
            if (shouldShowRequestPermissionRationale( __permissionsRequired[0])
                    || shouldShowRequestPermissionRationale( __permissionsRequired[1])
                ) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    requestPermissions( __permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel()

                    }
                builder.show()
            } else if (permissionStatus!!.getBoolean(__permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->

                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)

                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()

                    builder.show()
                }
            } else {
                //just request the permission
                requestPermissions(__permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }


            val editor = permissionStatus!!
                    .edit()
            editor.putBoolean(__permissionsRequired[0], true)
            editor.commit()


        } else {

            proceedAfterPermission();


        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {

                    proceedAfterPermission()


            } else if (shouldShowRequestPermissionRationale(__permissionsRequired[0])
                    || shouldShowRequestPermissionRationale(__permissionsRequired[1])
                   ) {

                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs GPS permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->

                    dialog.cancel()
                    requestPermissions( __permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                builder.show()
            } else {

                Toast.makeText(getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission()
            }
            else
            {

            }
        }

    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission()
            }
            else
            {

            }
        }
    }


    fun proceedAfterPermission()
    {
        Log.e("proceeding","After permission")
        bt_location.text = "Stop Location Sharing"
        objSaveData.save("locationSharing", "yes")
        val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(context));

        val myJob = dispatcher.newJobBuilder()
                .setService(LocationJobService::class.java) // the JobService that will be called
                .setTag("location")
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(0, 120))
//                .setConstraints(
//                        // only run on an unmetered network
//                        Constraint.ON_ANY_NETWORK
//                        // only run when the device is charging
//                )
                .build()
        dispatcher.mustSchedule(myJob);


        //setAlarm()

        //val locationIntent=Intent(context,GPSTracker_DUP::class.java)
        //context.startService(locationIntent)

    }
    fun cancelAlarm() {

//        val locationIntent=Intent(context,GPSTracker_DUP::class.java)
//        context.stopService(locationIntent)
//        Log.e("Cancelling ","alarm")
//        val myIntent = Intent(context,
//                LocationSharingReceiver::class.java)
//        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//
//        val alarmManager: AlarmManager
//        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//
//        val pendingIntent: PendingIntent
//
//        // A PendingIntent specifies an action to take in the
//        // future
//
//            pendingIntent = PendingIntent.getBroadcast(
//                    context, 300, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            alarmManager.cancel(pendingIntent)
        val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(context));
//
////        val myJob = dispatcher.newJobBuilder()
////                .setService(LocationJobService::class.java) // the JobService that will be called
////                .setTag("location")
////                .setReplaceCurrent(false)
////                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
////                .setLifetime(Lifetime.FOREVER)
////                .setRecurring(false)
////                .setTrigger(Trigger.executionWindow(0, 300))
//////                .setConstraints(
//////                        // only run on an unmetered network
//////                        Constraint.ON_ANY_NETWORK
//////                        // only run when the device is charging
//////                )
////                .build()
        dispatcher.cancel("location")
           }



    fun setAlarm() {

        Log.e("Setting","alarm")
        val myIntent = Intent(context,
                LocationSharingReceiver::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val alarmManager: AlarmManager
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val pendingIntent: PendingIntent

        // A PendingIntent specifies an action to take in the
        // future

            pendingIntent = PendingIntent.getBroadcast(
                    context, 300, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)


            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    1000*60*2, pendingIntent)

    }


    fun getNetworkInfo(context: Context): NetworkInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info!!.isConnected() && info!!.getType() == ConnectivityManager.TYPE_WIFI
    }

    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info!!.isConnected() && info!!.getType() == ConnectivityManager.TYPE_MOBILE
    }

    private fun callFragment(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, mFragment)
            fragmentTransaction.commit()

        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_Setting_Fragment {
            val fragment = User_Setting_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor