package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.kevalpatel.ringtonepicker.RingtonePickerDialog
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity
import kotlinx.android.synthetic.main.fragment_change_notification_sound.*
import kotlinx.android.synthetic.main.fragment_user_home.*
import org.jetbrains.anko.support.v4.toast
import android.content.DialogInterface
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.ToneGenerator
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R.layout.dialog
import org.jetbrains.anko.db.INTEGER
import java.io.IOException


class User_Change_Notification_Sound_Fragment : BaseFragment() {
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    internal lateinit var packageName: String
    private var mMediaPlayer: MediaPlayer? = null

    internal var __permissionsRequired = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS)

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false
    private var pendingIntent: PendingIntent? = null
    private var notificationManager: NotificationManager? = null
    private var mUri: Uri? = null    // TODO: Rename and change types of parameters

    private var mParam1: String? = null
    private var mParam2: String? = null


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
        val view = inflater!!.inflate(R.layout.fragment_change_notification_sound, container, false)

        return view

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        packageName = "com.nitesh.brill.saleslines";
        //========================\\
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)

        val myIntent = Intent(activity, User_Home_Activity::class.java)
        //Initialize PendingIntent
        pendingIntent = PendingIntent.getActivity(activity, 0, myIntent, 0)
        //Initialize NotificationManager using Context.NOTIFICATION_SERVICE
        notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(objSaveData.getString("userRole").equals( "user")) {

            //==============================\\
            tv_ChangeReminderSound.setOnClickListener {
                getPermissions()

            }

            if (objSaveData.getBoolean("dialPadTone")) {
                sw_dialpad.isChecked = false


            } else {
                sw_dialpad.isChecked = true

            }


            val switchStateSound = sw_dialpad.isChecked()

            Log.e("Button State", "" + switchStateSound + "=====");


//        if (switchStateSound) {
//            objSaveData.saveBoolean("dialPadTone", true)
//
//
//        } else {
//            objSaveData.saveBoolean("dialPadTone", false)
//
//
//        }


            sw_dialpad.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.e("Is checked", "" + isChecked)
                if (isChecked) {
                    Log.e("Sound off ", objSaveData.getString("buttonState").toString());
                    sw_dialpad.isChecked = true
                    objSaveData.saveBoolean("dialPadTone", false)

                } else {
                    Log.e("Sound on ", objSaveData.getString("buttonState").toString());
                    sw_dialpad.isChecked = false
                    objSaveData.saveBoolean("dialPadTone", true)


                }

            }
        }
        else{
            rl_switch.visibility = View.GONE
        }





        //======================================\\


        tv_DialPadTone.setOnClickListener {


            val items = arrayOf<CharSequence>(" TONE_DTMF_0 ", " TONE_DTMF_1 ", " TONE_DTMF_2 ", " TONE_DTMF_3 ", " TONE_DTMF_4 ", " TONE_DTMF_5 ", " TONE_DTMF_6 ", " TONE_DTMF_7 ", " TONE_DTMF_8 ", " TONE_DTMF_9 ")


            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Dialpad Tone!")
                    .setSingleChoiceItems(items, 1) { dialogInterface, item ->


                    }.setPositiveButton("OK") { dialog, id ->

                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                objSaveData.save(ConstantValue.DialPadTone,""+selectedPosition)
                UsefullData.Log("=====Pos "+ selectedPosition)



            }.setNegativeButton("Cancel") { dialog, id ->
                //  Your code when user clicked on Cancel
            }.create()
            dialog.show()
//

        }
    }

    private fun getPermissions() {


        if (ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[4])) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Audio,Phone and Storage permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(getActivity(), __permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else if (permissionStatus!!.getBoolean(__permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Audio ,Phone and Storage permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    toast("Go to Permissions to Grant  Audio ,Phone and Storage")
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(getActivity(), __permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }


            val editor = permissionStatus!!
                    .edit()
            editor.putBoolean(__permissionsRequired[0], true)
            editor.commit()


        } else {


            callRingTonePicker()


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
                try {

                    callRingTonePicker()

                } catch (e: SecurityException) {
                    e.printStackTrace()
                }

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), __permissionsRequired[4])) {

                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Audio ,Phone fand Storage permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(getActivity(), __permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                Toast.makeText(getContext(), "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                callRingTonePicker()

            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + et_dialerPad.text.toString().trim())
                callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(callIntent)
            }
        }
    }

    private fun callRingTonePicker() {

        val ringtonePickerBuilder = RingtonePickerDialog.Builder(activity, fragmentManager)

        //Set title of the dialog.
        //If set null, no title will be displayed.
        ringtonePickerBuilder.setTitle("Select ringtone")

        //Add the desirable ringtone types.
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION)

        //set the text to display of the positive (ok) button. (Optional)
        ringtonePickerBuilder.setPositiveButtonText("SET RINGTONE")

        //set text to display as negative button. (Optional)
        ringtonePickerBuilder.setCancelButtonText("CANCEL")

        //Set flag true if you want to play the com.ringtonepicker.sample of the clicked tone.
        ringtonePickerBuilder.setPlaySampleWhileSelection(true)

        //Set the callback listener.
        ringtonePickerBuilder.setListener { ringtoneName, ringtoneUri ->
            //Do someting with Uri.
            mUri = ringtoneUri
            Log.e("mUri", "" + mUri)
            Log.e("mUriString", mUri.toString())
            objSaveData.save("notificationSound", mUri.toString())

            mMediaPlayer = MediaPlayer()
            try {
                mMediaPlayer!!.setDataSource(context, mUri)
                val audioManager = context
                        .getSystemService(Context.AUDIO_SERVICE) as AudioManager
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                    mMediaPlayer!!.prepare()
                    mMediaPlayer!!.start()
                }
            } catch (e: IOException) {
                println("OOPS")
            }
            finally {
                mMediaPlayer!!.stop()
            }

        }

        ringtonePickerBuilder.show()


    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): User_Change_Notification_Sound_Fragment {
            val fragment = User_Change_Notification_Sound_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
/*

notificationBuilder = NotificationCompat.Builder(activity)
                        .setContentTitle("Notification Demo").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                        .setContentText("You have got notification.")
                //add sound

                notificationBuilder!!.setSound(mUri)
                //vibrate
                val v = longArrayOf(500, 1000)
                notificationBuilder!!.setVibrate(v)
                notificationManager!!.notify(1, notificationBuilder!!.build())

 */
