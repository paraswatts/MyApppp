package com.nitesh.brill.saleslines._AGM_Classes.AGM_Fragment

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
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
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity

import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.GM_Change_Notification_Sound_Fragment
import kotlinx.android.synthetic.main.fragment_change_notification_sound.*
import kotlinx.android.synthetic.main.fragment_user_home.*
import org.jetbrains.anko.support.v4.toast
import java.io.IOException


class AGM_Change_Notification_Sound_Fragment : BaseFragment() {
    private var mMediaPlayer: MediaPlayer? = null

    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    internal lateinit var packageName: String


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

        val myIntent = Intent(activity, ASM_Home_Activity::class.java)
        //Initialize PendingIntent
        pendingIntent = PendingIntent.getActivity(activity, 0, myIntent, 0)
        //Initialize NotificationManager using Context.NOTIFICATION_SERVICE
        notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //==============================\\
        tv_ChangeReminderSound.setOnClickListener {

            callRingTonePicker()
        }
        rl_switch.visibility = View.GONE

        //======================================\\


    }

    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
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
            } finally {
                mMediaPlayer!!.stop()
            }

            //set the currently selected uri, to mark that ringtone as checked by default. (Optional)
            //                Uri uri;
            //                ringtonePickerBuilder.setCurrentRingtoneUri(mCurrentSelectedUri);

            //Display the dialog.
            ringtonePickerBuilder.show()


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): GM_Change_Notification_Sound_Fragment {
            val fragment = GM_Change_Notification_Sound_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}



