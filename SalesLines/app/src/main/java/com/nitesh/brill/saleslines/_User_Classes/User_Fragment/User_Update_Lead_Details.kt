package com.nitesh.brill.saleslines._User_Classes.User_Fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.intentfilter.androidpermissions.PermissionManager
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.CustomDialogImageView
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Adapter.User_Adapter_Lead_Demo
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Constants
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.RecordService
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.LeadDemo
import kotlinx.android.synthetic.main.fragment_update_lead.*
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class User_Update_Lead_Details : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mEnqId: String? = null
    private var mLeadId: String? = null
    private var mDemoId: String? = null
    private var mRating: String? = null
    private var mManagerId: String? = null
    private var mUserId: String? = null
    private var dupMobile: String? = null
    private var dupMobileExist: Boolean = false

    lateinit var progressDialog:ProgressDialog;

    internal lateinit var packageName: String

    val PHOTOCAM = 10
    val PHOTOGAL = 11
    val VC1CAM = 12
    val VC1GAL = 13
    val VC2CAM = 14
    val VC2GAL = 15
    var myUri = Uri.parse("http://stackoverflow.com")
    private var GALLERY = 0
    private var CAMERA = 0
    private var userfile: File? = null
    /**
     *   Image  Name
     */
    var ImageName_PC: String? = ""
    var ImageName_VC2: String? = ""
    var ImageName_VC1: String? = ""


    var ContentType_PC: String? = "jpg"
    var ContentType_VC1: String? = "jpg"
    var ContentType_VC2: String? = "jpg"

    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    private var editField = true

    var Img_PC: String = ""
    var Img_VC1: String = ""
    var Img_VC2: String = ""

    var stateId: String = ""

    private val dataList: MutableList<LeadDemo> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mEnqId = arguments.getString(ARG_PARAM1)
            mLeadId = arguments.getString(ARG_PARAM2)

            mRating = arguments.getString(ARG_PARAM4)
            mManagerId = arguments.getString(ARG_PARAM5)
            mUserId = arguments.getString(ARG_PARAM6)

            //======Used In Upload Service Class====\\


            objSaveData.save("enqID", "" + mEnqId)
            objSaveData.save("leadID", "" + mLeadId)
            objSaveData.save("managerID", "" + mManagerId)
            objSaveData.save("userID", "" + mUserId)
            objSaveData.save("clientID", "" + objSaveData.getString(ConstantValue.CLIENT_ID))


        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_update_lead, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val permissionManager = PermissionManager.getInstance(activity)
//Recordings

        if (objSaveData.getString(ConstantValue.CLOSELEAD).equals("1")) {
            btn_Update.visibility = View.INVISIBLE
        } else {
            btn_Update.visibility = View.VISIBLE
        }
        packageName = "com.nitesh.brill.saleslines";

        permissionStatus = getActivity().getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)


        /**
         * Recordings
         *
        val progressDialog = ProgressDialog(activity)
         */
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Loading Data....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Recordings.visibility = GONE
        val settings = getActivity().getSharedPreferences(
                Constants.LISTEN_ENABLED, 0)
        val editor = settings.edit()
        editor.putBoolean("silentMode", false)
        editor.commit()
        setSharedPreferences(true)
        tv_Name.setEnabled(false);
        tv_Phone.setEnabled(false);
        tv_Email.setEnabled(false);
        tv_Company.setEnabled(false);
        tv_Address.setEnabled(false);
        tv_Mobile.setEnabled(false);
        tv_City.setEnabled(false);
        tv_State.setEnabled(false);
        tv_AltMobile.setEnabled(false);
        tv_PinCode.setEnabled(false);
        et_jobTitle.setEnabled(false);
        et_Department.setEnabled(false);
        et_SecondaryEmail.setEnabled(false);
        et_Skype.setEnabled(false);
        et_DealName.setEnabled(false);
        et_DealValue.setEnabled(false);
        et_ExpectedCloseDate.setEnabled(false);
        et_Fax.setEnabled(false);
        et_Website.setEnabled(false);
        et_NextItreaction.setEnabled(false);
        et_Next_Interaction_On.setEnabled(false);

        //==================================\\

        getDataFromServer();


        //=================================\\
        img_PC.setOnClickListener {
            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    alert("Choose Action to perform") {
                        positiveButton("View Image") {
                            val bitmap = (img_PC.getDrawable() as BitmapDrawable).bitmap


                            if (bitmap == null) {

                                objUsefullData.showMsgOnUI("No image found")

                            } else {
                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                val byteArray = stream.toByteArray()


                                val intent = Intent(activity, CustomDialogImageView::class.java)
                                intent.putExtra("byteArray", byteArray)
                                startActivity(intent)
                            }
                        }
                        negativeButton("Update Image") {
                            alert("Select Image Source", "Alert") {
                                positiveButton("Camera") {
                                    mCameraCall(PHOTOCAM)

                                }
                                negativeButton("Gallery") {
                                    mGalleryCall(PHOTOGAL)

                                }
                            }.show()

                        }
                    }.show()


                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })


        }

        //=================================\\
        img_VC.setOnClickListener {
            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    alert("Choose Action to perform") {
                        positiveButton("View Image") {
                            val bitmap = (img_VC.getDrawable() as BitmapDrawable).bitmap


                            if (bitmap == null) {

                                objUsefullData.showMsgOnUI("No image found")

                            } else {


                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                val byteArray = stream.toByteArray()


                                val intent = Intent(activity, CustomDialogImageView::class.java)
                                intent.putExtra("byteArray", byteArray)
                                startActivity(intent)
                            }
                        }
                        negativeButton("Update Image") {
                            alert("Select Image Source", "Alert") {
                                positiveButton("Camera") {
                                    mCameraCall(VC1CAM)

                                }
                                negativeButton("Gallery") {
                                    mGalleryCall(VC1GAL)

                                }
                            }.show()

                        }
                    }.show()


                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })


        }

        //=================================\\
        img_VC2.setOnClickListener {


            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    alert("Choose Action to perform") {
                        positiveButton("View Image") {
                            val bitmap = (img_VC2.getDrawable() as BitmapDrawable).bitmap


                            if (bitmap == null) {

                                objUsefullData.showMsgOnUI("No image found")

                            } else {


                                val stream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                val byteArray = stream.toByteArray()


                                val intent = Intent(activity, CustomDialogImageView::class.java)
                                intent.putExtra("byteArray", byteArray)
                                startActivity(intent)
                            }
                        }
                        negativeButton("Update Image") {
                            alert("Select Image Source", "Alert") {
                                positiveButton("Camera") {
                                    mCameraCall(VC2CAM)

                                }
                                negativeButton("Gallery") {
                                    mGalleryCall(VC2GAL)

                                }
                            }.show()
                        }
                    }.show()

                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })


        }


        et_ExpectedCloseDate.setOnClickListener {

            objUsefullData.getDemoDatePicker(et_ExpectedCloseDate)

        }


        //=======================================\\

        btn_Update.setOnClickListener {

            val fragment = User_Update_Enquiry_Fragment.newInstance("" + mEnqId, mLeadId!!, "" + mDemoId, "" + mRating)
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, fragment)
            fragmentTransaction.commit()

        }


        //=======================State=====================\\
//        tv_State.setOnClickListener {
//
//            selector("State", mArrayStateName) { dialogInterface, i ->
//                stateId = mArrayStateId.get(i)
//                tv_State!!.setText(mArrayStateName.get(i).toString())
//                tv_State.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
//            }
//
//        }


        //=======================================\\

        btn_Edit.setOnClickListener {


            if (isNetworkConnected)


                if (editField) {


                    tv_Name.setEnabled(true);
                    tv_Email.setEnabled(true);
                    tv_Phone.setEnabled(true);
                    tv_Company.setEnabled(true);
                    tv_Address.setEnabled(true);
                    tv_Mobile.setEnabled(true);
                    tv_City.setEnabled(true);
                    tv_State.setEnabled(true);
                    tv_AltMobile.setEnabled(true);
                    tv_PinCode.setEnabled(true);
                    et_jobTitle.setEnabled(true);
                    et_Department.setEnabled(true);
                    et_SecondaryEmail.setEnabled(true);
                    et_Skype.setEnabled(true);
                    et_DealName.setEnabled(true);
                    et_DealValue.setEnabled(true);
                    et_ExpectedCloseDate.setEnabled(true);
                    et_Fax.setEnabled(true);
                    et_Website.setEnabled(true);
                    editField = false
                    btn_Edit.setText("Save")

                } else if (!editField) {

                    if (checkValidation())

                        if(!dupMobile!!.equals(tv_Phone.text.toString())) {
                            Log.e("Checking dup","Checking dup")
                            checkDuplicateNumberOnSave("" + tv_Phone.text.toString())
                        }
                    else
                        {
                            saveLeadDetails()
                        }



                }


        }


        //================ Send Sms=================\\

        btn_SMS.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(tv_Phone.text.toString())))
            sendIntent.putExtra("sms_body", "")

            startActivity(sendIntent)


        }

        btn_Call.setOnClickListener {

            permissionManager.checkPermissions(permissionsRequired, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + tv_Phone.text.toString())
                    callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(callIntent)


                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })

        }

        //=======================Next_Itrection=====================\\
//        et_NextItreaction.setOnClickListener {
//
//
//            val Animals = activity.resources.getStringArray(R.array.next_interaction)
//            mShowAlertDialog(et_NextItreaction, Animals, "Next Interection")
//
//        }


        //=======================Mobile no. check duplicate=====================\\

        tv_Phone.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) {
                if (isNetworkConnected)
                        if(!dupMobile!!.equals(tv_Phone.text.toString())) {
                            Log.e("Checking dup","Checking dup")
                        checkDuplicateNumber("" + tv_Phone.text.toString())
                    }
            }

        }

    }

    private fun getDataFromServer() {

        if (isNetworkConnected) {


            objUsefullData.showProgress("Loading data..", "")


            getDemoidFromServer()


            getLeadDetails()

            //==================================\\

            getEnqDetails()

            //=======================================\\

            getLeadDemos()


        }


    }

    private fun checkValidation(): Boolean {

        if (objValidation.checkEmpty(tv_Name, "Name ")) {

            tv_Name.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(tv_Phone, "Phone ")) {

            tv_Phone.requestFocus()
            return false
        }
        if (objValidation.checkForLength(tv_Phone, "Phone ")) {

            tv_Phone.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(tv_Email, "Email ")) {

            tv_Email.requestFocus()
            return false
        }
        if (!objValidation.checkForEmail(tv_Email, "Email ")) {
            tv_Email.requestFocus()
            return false
        }
//
//        if (objValidation.checkEmpty(tv_Name, "Name ")) {
//
//            tv_Name.requestFocus()
//            return false
//        }
//
//        if (objValidation.checkEmpty(tv_Phone, "Phone ")) {
//
//            tv_Phone.requestFocus()
//            return false
//        }
//
//
//        if (objValidation.checkEmpty(tv_Email, "Email ")) {
//
//            tv_Email.requestFocus()
//            return false
//        }
//        if (!objValidation.checkForEmail(tv_Email, "Email ")) {
//            tv_Email.requestFocus()
//            return false
//        }
//        if (objValidation.checkEmpty(tv_Company, "Company ")) {
//            tv_Company.requestFocus()
//            return false
//        }
//
//
//
//        if (objValidation.checkEmpty(tv_Address, "Address ")) {
//
//            tv_Address.requestFocus()
//            return false
//        }
//
//
//
//        if (objValidation.checkEmpty(tv_City, "City ")) {
//
//            tv_City.requestFocus()
//            return false
//        }
//
//        if (objValidation.checkEmpty(tv_State, "State ")) {
//
//            tv_State.requestFocus()
//            return false
//        }
//
//        if (objValidation.checkEmpty(tv_PinCode, "PinCode ")) {
//
//            tv_PinCode.requestFocus()
//            return false
//        }
//
//
//        if (objValidation.checkEmpty(tv_Mobile, "Mobile ")) {
//
//            tv_Mobile.requestFocus()
//            return false
//        }

        return true
    }

    private fun checkDuplicateNumber(s: String) {


        val mCall = apiEndpointInterface!!.mCheckDuplicatePhone("" + s, objSaveData.getString(ConstantValue.CLIENT_ID), objSaveData.getString(ConstantValue.USER_ID))
        mCall.enqueue(object : Callback<JsonElement> {


            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {

                        var mLeadId = response.body().toString()

                        if (mLeadId.equals("0")) {
                            //Do Nothing
                            UsefullData.Log("User_Response    " + response.body().toString())
                        } else {
                            dupMobileExist= true
                            tv_Phone.requestFocus()
                            Log.e("Checking dup1","Checking dup")
                            tv_Phone.setText("")
                            tv_Phone.setError("Number already Exists")

//                            alert("This phone number is already available ! please choose another number ", "Duplicate phone number ?") {
//                                positiveButton("Ok") {
//
//                                    tv_Phone.setText("")
//                                    Log.e("Checking dup2","Checking dup")
//                                    toast("Please enter diffrent mobile number ")
//
//
//                                }
//
//                            }
                        }

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }
        })


    }

    private fun checkDuplicateNumberOnSave(s: String) {


        val mCall = apiEndpointInterface!!.mCheckDuplicatePhone("" + s, objSaveData.getString(ConstantValue.CLIENT_ID), objSaveData.getString(ConstantValue.USER_ID))
        mCall.enqueue(object : Callback<JsonElement> {


            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                    if (response!!.isSuccessful) {

                        var mLeadId = response.body().toString()

                        if (mLeadId.equals("0")) {
                            //Do Nothing
                            saveLeadDetails()

                            UsefullData.Log("User_Response Save button   " + response.body().toString())
                        } else {
                            tv_Phone.requestFocus()
                            Log.e("Checking dup1","Checking dup")
                            tv_Phone.setText("")
                            tv_Phone.setError("Number already Exists")

//                            alert("This phone number is already available ! please choose another number ", "Duplicate phone number ?") {
//                                positiveButton("Ok") {
//
//                                    tv_Phone.setText("")
//                                    Log.e("Checking dup2","Checking dup")
//                                    toast("Please enter diffrent mobile number ")
//
//
//                                }
//
//                            }
                        }

                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }
        })


    }



    private fun getDemoidFromServer() {


        val mCall = apiEndpointInterface!!.mGetDemoId("" + mEnqId)
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                 objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                //objUsefullData.dismissProgress()

                //testing Demo id
                //  mDemoId = "83"
                Log.e("URL", "=mGetDemoId==" + response!!.raw().request().url())

                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response  mGetDemoId    " + response.body().toString())
                        mDemoId = response.body().toString()




                        UsefullData.Log(mLeadId + "====getDemoidFromServer===" + mRating)
                        UsefullData.Log(mDemoId + "===getDemoidFromServer====" + mEnqId)


                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

        })


    }

    private fun setSharedPreferences(silentMode: Boolean) {

        objSaveData.saveBoolean("silentMode", silentMode)

        val myIntent = Intent(context, RecordService::class.java)
        myIntent.putExtra("commandType", Constants.RECORDING_DISABLED)
        myIntent.putExtra("silentMode", silentMode)

        UsefullData.Log("" + silentMode)
        context.startService(myIntent)
    }


    private fun mShowAlertDialog(sp_Next_Itrection_By: EditText?, animals: Array<out String>?, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)


        dialogBuilder.setItems(animals, DialogInterface.OnClickListener { dialog, item ->

            sp_Next_Itrection_By!!.setText(animals!![item].toString())


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }


    private fun saveAssignEmployee() {


        objUsefullData.showProgress("Please Wait..", "")
        val paramObject = JSONObject()
        paramObject.put("ManagerId", mManagerId)
        paramObject.put("EmployeeCode", mUserId)
        paramObject.put("LeadId", mLeadId)


        UsefullData.Log("==================" + paramObject.toString())

        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.mUpdateEnquiryLead2(mLeadId, body)


        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("=============" + response!!.body())
                        var success = ""

                        var item = JSONArray(response!!.body().toString())

                        for (i in 0..(item.length() - 1)) {
                            val item_ = item.getJSONObject(0)

                            UsefullData.Log("===" + item_)
                            success = item_.optString("Success")

                        }



                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Updated")

                        } else {
                            objUsefullData.showMsgOnUI("Save failed")
                        }

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                objUsefullData.dismissProgress()
                UsefullData.Log("=============" + t)
                objUsefullData.showMsgOnUI("Update failed  " + t)

            }

        })


    }


    private fun getLeadDemos() {
        dataList.clear()
        val mCall = apiEndpointInterface!!.mGetLeadDemos((mEnqId!!))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                // objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            var data = LeadDemo(item.getString("DemoDate"), item.getString("Interaction_By"), item.getString("CreatedDate"), item.getString("H_Checked"), item.getString("Comments"))

                            dataList.add(data)


                        }
                        var adapterSearch = User_Adapter_Lead_Demo(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()

                    progressDialog.dismiss()

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }

            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                // objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)

            }

        })

    }

    private fun getEnqDetails() {


        //    objUsefullData.showProgress("getting lead data ... ", "")
        val mCall = apiEndpointInterface!!.mGetEnquiryDetails((mEnqId!!))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                //  objUsefullData.dismissProgress()
                Log.e("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response =============   " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            try {

                                if (!item.getString("EnquiryNum").isEmpty()) {
                                    et_EnqNo.setText(item.getString("EnquiryNum").toString())
                                }
                                if (!item.getString("NextInteractionBy").isEmpty()) {
                                    et_NextItreaction.setText(item.getString("NextInteractionBy").toString())
                                }
                                if (!item.getString("NextInteractionDate").isEmpty()) {
                                    et_Next_Interaction_On.setText(item.getString("NextInteractionDate").toString())
                                }

                            } catch (e: Exception) {

                            }
                        }

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }

            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                   objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)

            }

        })

    }

    private fun saveLeadDetails() {


        val paramObject = JSONObject()
        objUsefullData.showProgress("Please Wait...", "")
        paramObject.put("LeadName", tv_Name.text.toString())
        paramObject.put("Phone", tv_Phone.text.toString())
        paramObject.put("Company", tv_Company.text.toString())
        paramObject.put("Status", "Active")

        paramObject.put("UserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
        paramObject.put("RoleId", Integer.parseInt(objSaveData.getString(ConstantValue.ROLE_ID)))
        paramObject.put("ManagerId", Integer.parseInt(objSaveData.getString(ConstantValue.MANAGER_ID)))

        paramObject.put("ModifiedUserId", tv_Name.text.toString())
        paramObject.put("Email", tv_Email.text.toString())
        paramObject.put("EmployeeCode", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))

        paramObject.put("Address1", tv_Address.text.toString())
        paramObject.put("Address2", tv_Address.text.toString())
        paramObject.put("Address3", tv_Address.text.toString())

        paramObject.put("State", tv_State.text.toString())
        paramObject.put("City", tv_City.text.toString())
        paramObject.put("PinCode", tv_PinCode.text.toString())
        paramObject.put("Company", tv_Company.text.toString())
        paramObject.put("JobTitle", et_jobTitle.text.toString())
        paramObject.put("Department", et_Department.text.toString())
        paramObject.put("SecondaryEmail", et_SecondaryEmail.text.toString())
        paramObject.put("Skype", et_Skype.text.toString())
        paramObject.put("DealName", et_DealName.text.toString())
        paramObject.put("DealValue", et_DealValue.text.toString())
        paramObject.put("ExpectedCloseDate", et_ExpectedCloseDate.text.toString())
        paramObject.put("Fax", et_Fax.text.toString())
        paramObject.put("Website", et_Website.text.toString())
        paramObject.put("Mobile", tv_Mobile.text.toString())
        paramObject.put("Alt_Mobile", tv_AltMobile.text.toString())


        paramObject.put("ImageName_PC", ImageName_PC)
        paramObject.put("ContentType_PC", ContentType_PC!!)
        paramObject.put("Data_PC", Img_PC)

        paramObject.put("ImageName_VC1", ImageName_VC1)
        paramObject.put("ContentType_VC1", ContentType_VC1)
        paramObject.put("Data_VC1", Img_VC1)

        paramObject.put("ImageName_VC2", ImageName_VC2)
        paramObject.put("ContentType_VC2", ContentType_VC2)
        paramObject.put("Data_VC2", Img_VC2)
        paramObject.put("LeadId", mLeadId)

        UsefullData.Log("==================" + paramObject.toString())

        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.mUpdateDetailsLead(mLeadId, body)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.e("URL", "===" + response!!.raw().request().url())

                try {
                if (response.isSuccessful) {


                        UsefullData.Log("User_Response =============   " + response.body().toString())
                        var success = ""

                        var array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }

                        if (success.equals("1")) {
                            // objUsefullData.showMsgOnUI("Successfully Saved")
                            saveAssignEmployee()

                        } else {
                            objUsefullData.showMsgOnUI("Save failed")
                        }

                    tv_Name.setEnabled(false);
                    tv_Email.setEnabled(false);
                    tv_Phone.setEnabled(false);
                    tv_Company.setEnabled(false);
                    tv_Address.setEnabled(false);
                    tv_Mobile.setEnabled(false);
                    tv_City.setEnabled(false);
                    tv_State.setEnabled(false);
                    tv_AltMobile.setEnabled(false);
                    tv_PinCode.setEnabled(false);
                    et_jobTitle.setEnabled(false);
                    et_Department.setEnabled(false);
                    et_SecondaryEmail.setEnabled(false);
                    et_Skype.setEnabled(false);
                    et_DealName.setEnabled(false);
                    et_DealValue.setEnabled(false);
                    et_ExpectedCloseDate.setEnabled(false);
                    et_Fax.setEnabled(false);
                    et_Website.setEnabled(false);
                    editField = true
                    btn_Edit.setText("Edit")

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                objUsefullData.dismissProgress()
                UsefullData.Log("=============" + t)
                objUsefullData. getThrowableException(t)

            }

        })


    }

    private fun getLeadDetails() {

        objUsefullData.showProgress("Loading Lead Info.. ", "")
        val mCall = apiEndpointInterface!!.getLeadDetails((mLeadId!!))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                // objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            try {
                                if (!item.getString("LeadName").equals(null) && !item.getString("LeadName").equals("null") && !item.getString("LeadName").isEmpty()) {

                                    tv_Name.setText(item.getString("LeadName"))

                                }

                                if (!item.getString("Phone").equals(null) && !item.getString("Phone").equals("null") && !item.getString("Phone").isEmpty()) {
                                    dupMobile = item.getString("Phone");

                                    tv_Phone.setText(item.getString("Phone"))
                                }

                                if (!item.getString("City").equals(null) && !item.getString("City").equals("null") && !item.getString("City").isEmpty()) {
                                    tv_City.setText(item.getString("City"))
                                }
                                if (!item.getString("Mobile").equals(null) && !item.getString("Mobile").equals("null") && !item.getString("Mobile").isEmpty()) {
                                    tv_Mobile.setText(item.getString("Mobile"))
                                }
                                if (!item.getString("PinCode").equals(null) && !item.getString("PinCode").equals("null") && !item.getString("PinCode").isEmpty()) {
                                    tv_PinCode.setText(item.getString("PinCode"))
                                }
//
//                                if (!item.getString("StateId").equals(null) && !item.getString("StateId").equals("null") && !item.getString("StateId").isEmpty()) {
//                                    stateId = (item.getString("StateId"))
//                                }

                                if (!item.getString("StateName").equals(null) && !item.getString("StateName").equals("null") && !item.getString("StateName").isEmpty()) {
                                    tv_State.setText(item.getString("StateName"))
                                }
                                if (!item.getString("Alt_Mobile").equals(null) && !item.getString("Alt_Mobile").equals("null") && !item.getString("Alt_Mobile").isEmpty()) {
                                    tv_AltMobile.setText(item.getString("Alt_Mobile"))
                                }
                                if (!item.getString("Name").equals(null) && !item.getString("Name").equals("null") && !item.getString("Name").isEmpty()) {
                                    et_Assign_Employee.setText(item.getString("Name"))
                                }
                                if (!item.getString("Address1").equals(null) && !item.getString("Address1").equals("null") && !item.getString("Address1").isEmpty()) {
                                    tv_Address.setText(item.getString("Address1"))
                                }
                                if (!item.getString("Company").equals(null) && !item.getString("Company").equals("null") && !item.getString("Company").isEmpty()) {
                                    tv_Company.setText(item.getString("Company"))
                                }
                                if (!item.getString("Email").equals(null) && !item.getString("Email").equals("null") && !item.getString("Email").isEmpty()) {
                                    tv_Email.setText(item.getString("Email"))
                                }


                                if (!item.getString("JobTitle").equals(null) && !item.getString("JobTitle").equals("null") && !item.getString("JobTitle").isEmpty()) {
                                    et_jobTitle.setText(item.getString("JobTitle"))
                                }

                                if (!item.getString("Department").equals(null) && !item.getString("Department").equals("null") && !item.getString("Department").isEmpty()) {
                                    et_Department.setText(item.getString("Department"))
                                }

                                if (!item.getString("SecondaryEmail").equals(null) && !item.getString("SecondaryEmail").equals("null") && !item.getString("SecondaryEmail").isEmpty()) {
                                    et_SecondaryEmail.setText(item.getString("SecondaryEmail"))
                                }
                                if (!item.getString("Fax").equals(null) && !item.getString("Fax").equals("null") && !item.getString("Fax").isEmpty()) {
                                    et_Fax.setText(item.getString("Fax"))
                                }
                                if (!item.getString("Website").equals(null) && !item.getString("Website").equals("null") && !item.getString("Website").isEmpty()) {
                                    et_Website.setText(item.getString("Website"))
                                }
                                if (!item.getString("Skype").equals(null) && !item.getString("Skype").equals("null") && !item.getString("Skype").isEmpty()) {
                                    et_Skype.setText(item.getString("Skype"))
                                }
                                if (!item.getString("DealName").equals(null) && !item.getString("DealName").equals("null") && !item.getString("DealName").isEmpty()) {
                                    et_DealName.setText(item.getString("DealName"))
                                }
                                if (!item.getString("DealValue").equals(null) && !item.getString("DealValue").equals("null") && !item.getString("DealValue").isEmpty()) {
                                    et_DealValue.setText(item.getString("DealValue"))
                                }
                                if (!item.getString("ExpectedCloseDate").equals(null) && !item.getString("ExpectedCloseDate").equals("null") && !item.getString("ExpectedCloseDate").isEmpty()) {
                                    et_ExpectedCloseDate.setText(item.getString("ExpectedCloseDate"))
                                }
                                /**
                                 * Image Getting
                                 */

                                if ( !item.getString("Data_PC").equals(null) && !item.getString("Data_PC").equals("null") && !item.getString("Data_PC").isEmpty() && !item.getString("Data_PC").equals("MHgxMjM=")) {
                                    //
                                    img_PC.setBackgroundResource(0)
                                    Img_PC = item.getString("Data_PC")

                                    val decodedString = Base64.decode(item.getString("Data_PC"), Base64.DEFAULT)
                                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                    img_PC.setImageBitmap(decodedByte)

                                }

                                if (!item.getString("Data_VC1").equals(null) && !item.getString("Data_VC1").equals("null") && !item.getString("Data_VC1").isEmpty() && !item.getString("Data_PC").equals("MHgxMjM=")) {
                                    img_VC.setBackgroundResource(0)
                                    Img_VC1 = item.getString("Data_VC1")

                                    val decodedString = Base64.decode(item.getString("Data_VC1"), Base64.DEFAULT)
                                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                                    img_VC!!.background = null
                                    img_VC.setImageBitmap(decodedByte)
                                }

                                if (!item.getString("Data_VC2").equals(null) && !item.getString("Data_VC2").equals("null") && !item.getString("Data_VC2").isEmpty() && !item.getString("Data_PC").equals("MHgxMjM=")) {
                                    img_VC2.setBackgroundResource(0)
                                    Img_VC2 = item.getString("Data_VC2")
                                    val decodedString = Base64.decode(item.getString("Data_VC2"), Base64.DEFAULT)
                                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                                    img_VC2!!.background = null
                                    img_VC2.setImageBitmap(decodedByte)
                                }

                                objUsefullData.dismissProgress()

                            } catch (e: Exception) {

                            }
                        }


                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)

            }

        })


    }


    //======================================\\
    private fun mGalleryCall(code: Int) {

        GALLERY = 11


        userfile = objUsefullData
                .createFile("userfile.png")
        val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code)


    }

    //======================================\\
    private fun mCameraCall(code: Int) {


        UsefullData.Log("==========mCameraCall===========")
        CAMERA = 22
        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        userfile = objUsefullData.createFile("userfile.png")

        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(userfile))
        startActivityForResult(photoCaptureIntent, code)


    }


    //==============================================================\\
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // When an Image is picked
        if (requestCode == PHOTOCAM && resultCode == Activity.RESULT_OK) {


            setImage(img_PC, myUri, 0)


        } else if (requestCode == PHOTOGAL && resultCode == Activity.RESULT_OK && null != data) {

            UsefullData.Log("====photo====" + data.data)

            setImage(img_PC, data.data, 0)


        } else if (requestCode == VC1CAM && resultCode == Activity.RESULT_OK) {
            setImage(img_VC, myUri, 1)


        } else if (requestCode == VC1GAL && resultCode == Activity.RESULT_OK && null != data) {
            setImage(img_VC, data.data, 1)
            UsefullData.Log("====VC1====" + data.data)


        } else if (requestCode == VC2CAM && resultCode == Activity.RESULT_OK) {
            setImage(img_VC2, myUri, 2)

        } else if (requestCode == VC2GAL && resultCode == Activity.RESULT_OK && null != data) {
            setImage(img_VC2, data.data, 2)
            UsefullData.Log("====VC2====" + data.data)
        } else {
            GALLERY = 0
            CAMERA = 0
            Toast.makeText(activity, "You haven't picked Image/Video", Toast.LENGTH_LONG).show()
        }


    }

    //===================================================\\
    private fun setImage(img_Photo: ImageView?, data: Uri, photo: Int) {

        UsefullData.Log("" + GALLERY + "------- gallery--------" + CAMERA)
        if (GALLERY == 11) {


            UsefullData.Log("------- gallery         " + photo)

            val picturePath = getPath(data)
            userfile = File(picturePath)

            val imgBmp = BitmapFactory.decodeFile(picturePath)
            img_Photo!!.background = null
            img_Photo!!.setImageBitmap(imgBmp)
            reCreateFile(photo, imgBmp)
            GALLERY = 0


        } else if (CAMERA == 22) {

            UsefullData.Log("------- CAMERA    " + photo)


            var bitmap = BitmapFactory.decodeFile(
                    userfile!!.path,
                    BitmapFactory.Options())

            var orientation = 0
            try {
                val ei = ExifInterface(userfile!!.path)
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            } catch (e: Exception) {
                e.printStackTrace()
            }


            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotate(bitmap!!, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotate(bitmap!!, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> bitmap = rotate(bitmap!!, 270f)
                else -> {
                    println("orientation*****case****default****" + orientation)
                    bitmap = rotate(bitmap!!, orientation.toFloat())
                }
            }

            img_Photo!!.background = null
            img_Photo!!.setImageBitmap(bitmap)
            reCreateFile(photo, bitmap)


            CAMERA = 0


        }


    }


    // ==========================================//
    fun getPath(uri: Uri): String {
        val selectedImage = uri
        val filePathColumn = arrayOf<String>(MediaStore.MediaColumns.DATA)
        val cursor = activity.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null)
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        return picturePath
    }


    // ==========================================//
    fun rotate(src: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }


    private fun reCreateFile(photo: Int, _bitmapScaled: Bitmap?) {

        var path = userfile!!.getPath()
        var baos = ByteArrayOutputStream()
        val imgBmp = Bitmap.createScaledBitmap(_bitmapScaled, 400, 400,
                true)



        imgBmp.compress(Bitmap.CompressFormat.PNG, 90, baos)
        UsefullData.Log("User File :: " + userfile!!.getPath())

        if (photo == 0) {
            ImageName_PC = path.substring(path.lastIndexOf("/") + 1);
            Img_PC = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP);


        } else if (photo == 1) {

            ImageName_VC1 = path.substring(path.lastIndexOf("/") + 1)
            Img_VC1 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP);

        } else if (photo == 2) {
            ImageName_VC2 = path.substring(path.lastIndexOf("/") + 1);
            Img_VC2 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP);
        }
    }

    //========================\\
    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {


        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
        return baos.toByteArray();
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"
        private val ARG_PARAM4 = "param4"
        private val ARG_PARAM5 = "param5"
        private val ARG_PARAM6 = "param6"

        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, param3: String, param4: String, managerId: String, userId: String): User_Update_Lead_Details {
            val fragment = User_Update_Lead_Details()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            args.putString(ARG_PARAM4, param4)
            args.putString(ARG_PARAM5, managerId)
            args.putString(ARG_PARAM6, userId)

            fragment.arguments = args
            return fragment
        }
    }
}

