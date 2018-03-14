package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Constants
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.ImageAdapter
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.RecordService
import kotlinx.android.synthetic.main.fragment_user_home.*
import org.jetbrains.anko.support.v4.toast


class User_Home_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    //=============================\\

    internal var PERMISSION_ALL = 1
    internal var PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)


    //==========================================\\

    internal lateinit var packageName: String

    internal var mContext: Context? = null

    internal lateinit var activity: Context

    var phoneNo = ""
    var cursorPos = 0
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101

    internal var __permissionsRequired = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS)

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false


    internal var builder: NotificationCompat.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    //=============================================\\

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return inflater!!.inflate(R.layout.fragment_user_home, container, false)

    }




    //================================================\\

    override fun onViewCreated(rootView: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        UsefullData.Log(objSaveData.getString(ConstantValue.USER_ID) + "=========")
        UsefullData.Log("=========" + objSaveData.getString(ConstantValue.CLIENT_ID))
        val gridView = rootView!!.findViewById(R.id.gv_Dialer) as GridView

        gridView.adapter = ImageAdapter(context)

        packageName = "com.nitesh.brill.saleslines";
        //========================\\
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)

        val settings = getActivity().getSharedPreferences(
                Constants.LISTEN_ENABLED, 0)
        val editor = settings.edit()
        editor.putBoolean("silentMode", false)
        editor.commit()
        setSharedPreferences(true)

        bt_correct.setOnClickListener {

            var cursorPosition = et_dialerPad.getSelectionStart()
            Log.e("Cursor position", "" + cursorPosition);
            if (et_dialerPad.text.length > 0) {


                val inputString = et_dialerPad.text.toString()
                 val selectionStart=et_dialerPad.getSelectionStart();
                val selectionEnd=et_dialerPad.getSelectionEnd();
                val selectedText = inputString.substring(selectionStart, selectionEnd)
                if(!selectedText.isEmpty())
                {
                    Log.e("Selected Text is",selectedText)
                    val selectionDeletedString = inputString.replace(selectedText, "")
                    et_dialerPad.setText(selectionDeletedString)
                    et_dialerPad.setSelection(selectionStart)
                }
                else
                {

                    if (cursorPosition > 0) {

                        et_dialerPad.text.delete(cursorPosition - 1, cursorPosition)
                    }
                }


            }




        }

        bt_correct.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {

                et_dialerPad.setText("")
                return true;
            }

        })


        //===================================//
        rl_Search_Layout.setOnClickListener {

            //=========================\\

            if (isNetworkConnected) {

                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("1", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }

        }

        //==========Dial Number==========//


        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            // Sending image id to FullScreenActivity
            //et_dialerPad.setSelection(cursorPos)
            phoneNo = et_dialerPad.getText().toString()

            Log.e("Dialpad tone on/off",""+objSaveData.getBoolean("dialPadTone"));

          if(!objSaveData.getBoolean("dialPadTone")) {
                try {
                    val am = getActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    val volume_level = am.getStreamVolume(AudioManager.STREAM_RING) // Highest Ring volume level is 7, lowest is 0
                    val mToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, volume_level * 14) // Raising volume to 100% (For eg. 7 * 14 ~ 100)
                    mToneGenerator.stopTone()


                    if (objSaveData.getString(ConstantValue.DialPadTone).equals("_dial_pad_tone")) {
                        mToneGenerator.startTone(0, 100)
                    } else {
                        mToneGenerator.startTone(Integer.parseInt(objSaveData.getString(ConstantValue.DialPadTone)), 100)
                    }
                } catch (e: Exception) {

                    e.printStackTrace()

                }
        }

            var textToReplace:String = ""


            if (position == 0) {
               // appendText("1")
                textToReplace = "1"
            } else if (position == 1) {
               // appendText("2")
                textToReplace = "2"
            } else if (position == 2) {
               // appendText("3")
                textToReplace = "3"
            } else if (position == 3) {
                //appendText("4")
                textToReplace = "4"
            } else if (position == 4) {
                //appendText("5")
                textToReplace = "5"
            } else if (position == 5) {
                //appendText("6")
                textToReplace = "6"
            } else if (position == 6) {
                textToReplace = "7"
                //appendText("7")
            } else if (position == 7) {
                textToReplace = "8"
                //appendText("8")
            } else if (position == 8) {
                textToReplace = "9"
                //appendText("9")
            } else if (position == 9) {
                textToReplace = "*"
                //appendText("*")
            } else if (position == 10) {
                textToReplace = "0"
                //appendText("0")
            } else if (position == 11) {
                textToReplace = "#"
               // appendText("#")
            }
            val inputString = et_dialerPad.text.toString()
            val selectionStart=et_dialerPad.getSelectionStart();
            val selectionEnd=et_dialerPad.getSelectionEnd();
            val selectedText = inputString.substring(selectionStart, selectionEnd)
            if(!selectedText.isEmpty())
            {

                Log.e("Selected Text is",selectedText)
                val selectionDeletedString = inputString.replace(selectedText, textToReplace)
                et_dialerPad.setText(selectionDeletedString)
                et_dialerPad.setSelection(selectionStart+1)
            }

            else
            {
                if (position == 0) {
                    appendText("1")
                    textToReplace = "1"
                } else if (position == 1) {
                    appendText("2")
                    textToReplace = "2"
                } else if (position == 2) {
                    appendText("3")
                    textToReplace = "3"
                } else if (position == 3) {
                    appendText("4")
                    textToReplace = "4"
                } else if (position == 4) {
                    appendText("5")
                    textToReplace = "5"
                } else if (position == 5) {
                    appendText("6")
                    textToReplace = "6"
                } else if (position == 6) {
                    textToReplace = "7"
                    appendText("7")
                } else if (position == 7) {
                    textToReplace = "8"
                    appendText("8")
                } else if (position == 8) {
                    textToReplace = "9"
                    appendText("9")
                } else if (position == 9) {
                    textToReplace = "*"
                    appendText("*")
                } else if (position == 10) {
                    textToReplace = "0"
                    appendText("0")
                } else if (position == 11) {
                    textToReplace = "#"
                    appendText("#")
                }

            }

        }


        //======================//
        //====================== Add lead==========================\\

        iv_AddLead.setOnClickListener {

            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Add_Enquiry_Fragment.newInstance("", "")
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }

        }


        //====================== All leads ==========================\\

        iv_AllLeads.setOnClickListener {

            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("1", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }

        }

        //====================== All  CloseLead ==========================\\

        iv_CloseLead.setOnClickListener {
            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "1")
                val fragment = User_Search_Fragment.newInstance("2", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }
        }

        //====================== All TodayFollowup ==========================\\

        iv_TodayFollowup.setOnClickListener {
            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("3", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }

        }
        //====================== All   MissedFollowup ==========================\\

        iv_MissedFollowup.setOnClickListener {
            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("4", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }

        }

        //====================== All FollowupLeads ==========================\\

        iv_AddedToday.setOnClickListener {
            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("5", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }
        }

        //====================== All FollowupLeads ==========================\\

        iv_FollowupLeads.setOnClickListener {
            if (isNetworkConnected) {
                objSaveData.save(ConstantValue.CLOSELEAD, "0")
                val fragment = User_Search_Fragment.newInstance("6", mParam2!!)
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, fragment)
                fragmentTransaction.commit()
            }
        }

        iv_Call.setOnClickListener {
            //            objSaveData.save(ConstantValue.CLOSELEAD, "0")
//            val fragment = User_Search_Fragment.newInstance("6", mParam2!!)
//            val fragmentManager = fragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
//            fragmentTransaction.replace(R.id.content_frame, fragment)
//            fragmentTransaction.commit()
            if (objValidation.checkEmpty(et_dialerPad, "Phone ")) {

                et_dialerPad.requestFocus()

            }
            else if (objValidation.checkForLength(et_dialerPad, "Phone ")) {

                et_dialerPad.requestFocus()

            }


            else {

                getPermissions()
            }


        }


    }

    fun appendText(str: String) {

       val cursorPosition = et_dialerPad.getSelectionStart()
//
//        val start = Math.max(et_dialerPad.getSelectionStart(), 0);
//        val end = Math.max(et_dialerPad.getSelectionEnd(), 0);
//        et_dialerPad.getText().replace(Math.min(start, end), Math.max(start, end),
//        str, 0, str.length);
        //et_dialerPad.append(str)
        et_dialerPad.text.insert(cursorPosition,str)


        //phoneNo += str;

//        val mBuilder = StringBuilder(phoneNo)
//        mBuilder.insert(et_dialerPad.getSelectionStart(), str)
//        et_dialerPad.setText("" + mBuilder);
//        cursorPos=et_dialerPad.selectionStart




       // Log.e("=====", cursorPos+"====" + mBuilder)
    }

    private fun setSharedPreferences(silentMode: Boolean) {

        objSaveData.saveBoolean("silentMode", false)

        val myIntent = Intent(context, RecordService::class.java)
        myIntent.putExtra("commandType",
                if (silentMode)
                    Constants.RECORDING_DISABLED
                else
                    Constants.RECORDING_ENABLED)
        myIntent.putExtra("silentMode", false)
        context.startService(myIntent)
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


            val callIntent = Intent(Intent.ACTION_CALL)
            var dialing_number = et_dialerPad.text;

            callIntent.data = Uri.parse("tel:" + Uri.encode(et_dialerPad.text.toString().trim()))
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(callIntent)

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

                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + Uri.parse(et_dialerPad.text.toString().trim()))
                    callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(callIntent)
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
                builder.setMessage("This app needs Audio ,Phone and Storage permissions.")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + Uri.parse(et_dialerPad.text.toString().trim()))
                callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(callIntent)
            }
        }

    }


    override fun onResume() {
        super.onResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), __permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + Uri.parse(et_dialerPad.text.toString().trim()))
                callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(callIntent)
            }
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        //============================\\

        fun newInstance(param1: String, param2: String): User_Home_Fragment {
            val fragment = User_Home_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
