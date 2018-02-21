package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.intentfilter.androidpermissions.PermissionManager
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.UploadService
import kotlinx.android.synthetic.main.fragment_user_add_enquiry.*
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class User_Add_Enquiry_Fragment : BaseFragment() {
    val PHOTOCAM = 10
    val PHOTOGAL = 11
    val VC1CAM = 12
    val VC1GAL = 13
    val VC2CAM = 14
    val VC2GAL = 15
    private var GALLERY = 0
    private var CAMERA = 0

    private var mParam1: String? = null
    private var mParam2: String? = null
    var myUri = Uri.parse("http://stackoverflow.com")

    private var userfile: File? = null
    /**
     *   IF previous details fetch this flag is true if newly added its false by default
     */


    var PreviosLead = false
    var LeadId = ""
    var EnquiryNum = ""

    /**
     * Rating Data
     */

    var H: Boolean? = false
    var W: Boolean? = false
    var C: Boolean? = false
    var Rating: Float? = 0.0f


    /**
     *   Image  Name
     */
    var ImageName_PC: String? = null
    var ImageName_VC2: String? = null
    var ImageName_VC1: String? = null


    var ContentType_PC: String? = "jpg"
    var ContentType_VC1: String? = "jpg"
    var ContentType_VC2: String? = "jpg"


    var Img_PC: String = ""
    var Img_VC1: String = ""
    var Img_VC2: String = ""

    var check: Int = 0

    var stateId: String = ""

    var userId: Int = 0
    var leadAddedOrNot: String? = "no"


    var nextIntractionDate = false


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
        val view = inflater!!.inflate(R.layout.fragment_user_add_enquiry, container, false)
        view.isFocusableInTouchMode = true
        view.requestFocus()
// -- Select --

        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_UP) {
                    val file = File(Environment.getExternalStorageDirectory().toString() + "/SalesLineCallRecordings")

                    val files = file.listFiles()

                    if (files != null) {
                        for (f in file.listFiles()!!) {
                            if (f.name.startsWith(objSaveData.getString("MobileNumber"))) {
                                Log.e("File delete karan laga", f.absolutePath + "")
                                f.delete()
                            }
                        }
                    }
                    objSaveData.remove("MobileNumber")
                    Log.e(tag, "onKey Back listener is working!!!")
                    val fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                    val fragmentManager = fragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.content_frame, fragment)
                    fragmentTransaction.commit()

                    return true
                }
                return false
            }

        })





        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionManager = PermissionManager.getInstance(activity)
        val bundle = this.arguments
        //Log.e("Phone number in" ,"add Lead"+objSaveData.getString("MobileNumber"))
        if (isNetworkConnected) {


            //=================================\\
            getGetEnquiryNum()
        }


        //=================================\\
        img_Photo.setOnClickListener {


            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {

                    alert("Select Image Source", "Alert") {
                        positiveButton("Camera") {
                            mCameraCall(PHOTOCAM)

                        }
                        negativeButton("Gallery") {
                            mGalleryCall(PHOTOGAL)

                        }
                    }.show()

                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })


        }

        //=================================\\
        img_VC1.setOnClickListener {


            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {

                    alert("Select Image Source", "Alert") {
                        positiveButton("Camera") {
                            mCameraCall(VC1CAM)

                        }
                        negativeButton("Gallery") {
                            mGalleryCall(VC1GAL)

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

                    alert("Select Image Source", "Alert") {
                        positiveButton("Camera") {
                            mCameraCall(VC2CAM)

                        }
                        negativeButton("Gallery") {
                            mGalleryCall(VC2GAL)

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
        et_DemoDate.setOnClickListener {
            et_DemoDate.setBackgroundResource((R.drawable.text_view_border))
            et_DemoDate.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            objUsefullData.getDemoDatePicker(et_DemoDate)


        }

        //=================================\\
        et_NextInteractionDate.setOnClickListener {
            et_NextInteractionDate.setBackgroundResource((R.drawable.text_view_border))
            et_NextInteractionDate.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            objUsefullData.getNextInteractionDatePickerOn(et_NextInteractionDate)

        }

        //===================================\\

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingBar.setBackgroundResource((R.drawable.white_border))
            Rating = rating


        }

        //==================cb_W===============\\
        cb_H.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if (isChecked) {
                    H = true

                    cb_C.isChecked = false
                    cb_W.isChecked = false


                } else {
                    H = false

                }

                ll_LeadStatus.setBackgroundResource((R.drawable.white_border))
            }
        })

        //==================cb_H ===============\\
        cb_W.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if (isChecked) {
                    W = true

                    cb_C.isChecked = false
                    cb_H.isChecked = false

                } else {
                    W = false

                }
                ll_LeadStatus.setBackgroundResource((R.drawable.white_border))
            }
        })

        //==================cb_C ===============\\
        cb_C.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if (isChecked) {
                    C = true

                    cb_W.isChecked = false
                    cb_H.isChecked = false

                } else {
                    C = false


                }
                ll_LeadStatus.setBackgroundResource((R.drawable.white_border))
            }
        })


        //=======================Mobile no. check duplicate=====================\\

        et_Phone.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) {
                if (isNetworkConnected)
                    checkDuplicateNumber("" + et_Phone.text.toString())
            }

        }


        //======================= Lead Source=====================\\
        sp_Spinner_Lead_Source.setOnClickListener {

            //=================================\\
            if (isNetworkConnected)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Spinner_Lead_Source.background = ResourcesCompat.getDrawable(resources, R.drawable.text_view_border, null)
                } else {
                    sp_Spinner_Lead_Source.background = activity.resources.getDrawable(R.drawable.text_view_border)
                }

            sp_Spinner_Lead_Source.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            getBindSources(sp_Spinner_Lead_Source, "Lead Source")


        }


        //======================= Lead Stage=====================\\
        sp_Spinner_Lead_Stage.setOnClickListener {

            //=================================\\
            if (isNetworkConnected)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Spinner_Lead_Stage.background = ResourcesCompat.getDrawable(resources, R.drawable.text_view_border, null)
                } else {
                    sp_Spinner_Lead_Stage.background = activity.resources.getDrawable(R.drawable.text_view_border)
                }
            sp_Spinner_Lead_Stage.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            getBindStages(sp_Spinner_Lead_Stage, "Lead Stage")

        }

        //======================= Intrested Pproject =====================\\
        sp_Spinner_Interested_Product.setOnClickListener {

            //============================\\
            if (isNetworkConnected)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Spinner_Interested_Product.background = ResourcesCompat.getDrawable(resources, R.drawable.text_view_border, null)
                } else {
                    sp_Spinner_Interested_Product.background = activity.resources.getDrawable(R.drawable.text_view_border)
                }
            sp_Spinner_Interested_Product.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            getAllProduct(sp_Spinner_Interested_Product, "Interested Product")

        }


        //=======================Assign_Employee=====================\\
        sp_Assign_Employee.setText(objSaveData.getString(ConstantValue.NAME))


        //=======================Next_Itrection=====================\\
        sp_Next_Itrection_By.setOnClickListener {

            nextIntractionDate = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sp_Next_Itrection_By.background = ResourcesCompat.getDrawable(resources, R.drawable.text_view_border, null)
            } else {
                sp_Next_Itrection_By.background = activity.resources.getDrawable(R.drawable.text_view_border)
            }

            sp_Next_Itrection_By.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            val Animals = activity.resources.getStringArray(R.array.next_interaction)
            mShowAlertDialog(sp_Next_Itrection_By, Animals, "Next Interaction")

        }

        //=======================Response_Of_Iteraction=====================\\
        sp_Response_Of_Iteraction.setOnClickListener {
            if (isNetworkConnected)
                if (isNetworkConnected)


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sp_Response_Of_Iteraction.background = ResourcesCompat.getDrawable(resources, R.drawable.text_view_border, null)
                    } else {
                        sp_Response_Of_Iteraction.background = activity.resources.getDrawable(R.drawable.text_view_border)
                    }

            sp_Response_Of_Iteraction.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            getBindResponse(sp_Response_Of_Iteraction, "Response")
        }

        //=======================State=====================\\
        et_State.setOnClickListener {

            selector("State", mArrayStateName) { dialogInterface, i ->

                et_State.setBackgroundResource((R.drawable.text_view_border))

                stateId = mArrayStateId.get(i)

                et_State!!.setText(mArrayStateName.get(i).toString())
                et_State.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))
            }

        }


        //=================================\\
        btn_Save.setOnClickListener {
            if (isNetworkConnected)
                if (!PreviosLead) {

                    /**
                     *  If lead is new it will call
                     */
                    if (checkValidation())
                        callDataToServer()
                }
        }


    }

    private fun checkValidation(): Boolean {


        if (objValidation.checkEmpty(et_Name, "Name ")) {

            et_Name.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(et_Phone, "Phone ")) {

            et_Phone.requestFocus()
            return false
        }
        if (objValidation.checkForLength(et_Phone, "Phone ")) {

            et_Phone.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(et_Email, "Email ")) {

            et_Email.requestFocus()
            return false
        }
        if (!objValidation.checkForEmail(et_Email, "Email ")) {
            et_Email.requestFocus()
            return false
        }
//        if (objValidation.checkEmpty(et_Company, "Company ")) {
//            et_Company.requestFocus()
//            return false
//        }
//
//
//
//        if (objValidation.checkEmpty(et_Address, "Address ")) {
//
//            et_Address.requestFocus()
//            return false
//        }
//
//
//
//        if (objValidation.checkEmpty(et_City, "City ")) {
//
//            et_City.requestFocus()
//            return false
//        }
//
//        if (et_State.text.toString().equals("State")) {
//            objUsefullData.showMsgOnUI("Please select State")
//            et_State.setBackgroundResource((R.drawable.red_border))
//            et_State.requestFocus()
//            return false
//        }
//
//        if (objValidation.checkEmpty(et_PinCode, "PinCode ")) {
//
//            et_PinCode.requestFocus()
//            return false
//        }
//
//
//        if (objValidation.checkEmpty(et_Mob, "Mobile ")) {
//
//            et_Mob.requestFocus()
//            return false
//        }
//
//
//
//        if (objValidation.checkEmpty(et_Company, "Company ")) {
//
//            et_Company.requestFocus()
//            return false
//        }
//        if (objValidation.checkEmpty(et_jobTitle, "JobTitle ")) {
//
//            et_jobTitle.requestFocus()
//            return false
//        }
//        if (objValidation.checkEmpty(et_Department, "Department ")) {
//
//            et_Department.requestFocus()
//            return false
//        }

        if (!et_SecondaryEmail.text.toString().trim().equals("")) {

            if (!objValidation.checkForEmail(et_SecondaryEmail, "SecondaryEmail ")) {

                et_SecondaryEmail.requestFocus()
                return false
            }
        }
//        if (objValidation.checkEmpty(et_Skype, "Skype ")) {
//
//            et_Skype.requestFocus()
//            return false
//        }
//        if (objValidation.checkEmpty(et_DealName, "DealName ")) {
//
//            et_DealName.requestFocus()
//            return false
//        }
//
//        if (objValidation.checkEmpty(et_Fax, "Fax ")) {
//
//            et_Fax.requestFocus()
//            return false
//        }
//        if (objValidation.checkEmpty(et_Website, "Website ")) {
//
//            et_Website.requestFocus()
//            return false
//        }

        if (objValidation.checkEmpty(et_ExpectedCloseDate, "ExpectedCloseDate ")) {
            objUsefullData.showMsgOnUI("Expected close date should not be empty")
            et_ExpectedCloseDate.requestFocus()
            return false
        }



        if (Rating!!.equals(0.0f)) {

            objUsefullData.showMsgOnUI("Rating should not be empty")
            ratingBar.setBackgroundResource((R.drawable.red_border))
            ratingBar.requestFocus()
            return false
        }
        if (!H!! && !W!! && !C!!) {

            objUsefullData.showMsgOnUI("Please select lead status")
            ll_LeadStatus.setBackgroundResource((R.drawable.red_border))
            ll_LeadStatus.requestFocus()
            return false

        }


//        if (objValidation.checkEmpty(et_DemoDate, "DemoDate ")) {
//
//            objUsefullData.showMsgOnUI("Please select DemoDate")
//
//            et_DemoDate.requestFocus()
//            return false
//        }
        if (objValidation.checkEmpty(sp_Spinner_Lead_Source, "Lead Source ")) {
            sp_Spinner_Lead_Source.setError(null)
            objUsefullData.showMsgOnUI("Please select Lead Source")
            sp_Spinner_Lead_Source.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Lead_Source.requestFocus()
            return false
        }
        if (objValidation.checkEmpty(sp_Spinner_Lead_Stage, "Lead Stage")) {
            sp_Spinner_Lead_Stage.setError(null)
            objUsefullData.showMsgOnUI("Please select Lead Stage")
            sp_Spinner_Lead_Stage.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Lead_Stage.requestFocus()
            return false
        }
        if (objValidation.checkEmpty(sp_Spinner_Interested_Product, "Interested Product")) {
            sp_Spinner_Interested_Product.setError(null)
            objUsefullData.showMsgOnUI("Please select Interested Product")
            sp_Spinner_Interested_Product.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Interested_Product.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(sp_Assign_Employee, " Assign Employee")) {
            sp_Assign_Employee.setError(null)
            objUsefullData.showMsgOnUI("Please select Emplyee for assign lead")
            sp_Assign_Employee.setBackgroundResource((R.drawable.red_border))
            sp_Assign_Employee.requestFocus()
            sp_Assign_Employee.isFocusableInTouchMode = true
            return false
        }

        if (!nextIntractionDate) {
            objUsefullData.showMsgOnUI("Please select Next Interaction")
            sp_Next_Itrection_By.setBackgroundResource((R.drawable.red_border))
            sp_Next_Itrection_By.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(et_NextInteractionDate, "NextInteractionDate On ")) {

            objUsefullData.showMsgOnUI("Please select NextInteractionDate On")
            et_NextInteractionDate.setBackgroundResource((R.drawable.red_border))
            et_NextInteractionDate.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(sp_Response_Of_Iteraction, " Response Of Iteraction")) {
            sp_Response_Of_Iteraction.setError(null)
            objUsefullData.showMsgOnUI("Please select Response of Interaction")
            sp_Response_Of_Iteraction.setBackgroundResource((R.drawable.red_border))
            sp_Response_Of_Iteraction.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(et_Comments, "Comment ")) {

            et_Comments.requestFocus()
            return false
        }


        return true
    }


    //=======================================\\

    private fun getGetEnquiryNum() {


        val mCall = apiEndpointInterface!!.mGetEnquiryNum(objSaveData.getString(ConstantValue.CLIENT_ID))
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
                    if (response.isSuccessful) {
                        UsefullData.Log("User_Response   EnquiryNum  " + response.body().toString())

                        EnquiryNum = response.body().toString().substring(1, response.body().toString().length - 1)

                    } else {

                        UsefullData.Log("========" + response.code())
                        objUsefullData.showMsgOnUI("Enquiry Number not found ")

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })
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
                    if (response.isSuccessful) {

                        var mLeadId = response.body().toString()

                        if (mLeadId.equals("0")) {
                            //Do Nothing
                            UsefullData.Log("User_Response    " + response.body().toString())
                        } else {


                            alert("This Lead already exist ", "Duplicate phone number ?")

                            {


                                positiveButton("Ok")
                                {


                                    et_Phone.setText("")
                                    et_Name.setText("")
                                    /*
                                 check true when update previous lead
                                 */
                                    PreviosLead = true


                                }
                            }.show()

                        }
                    } else {

                        UsefullData.Log("========" + response.code())
                        // objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        })


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

    private fun getBindResponse(sp_Response_Of_Iteraction: EditText, s: String) {

        if (mArrayBindResponse.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.response_error))
        } else {
            mShowAlertDialogWithListview(sp_Response_Of_Iteraction, mArrayBindResponse as ArrayList<String>, s)
        }


    }

    private fun getBindStages(sp_Spinner_Lead_Stage: EditText, s: String) {
        if (mArrayBindStages.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.stage_error))
        } else {
            mShowAlertDialogWithListview(sp_Spinner_Lead_Stage, mArrayBindStages as ArrayList<String>, s)
        }


    }

    private fun getBindSources(sp_Spinner_Lead_Source: EditText, s: String) {


        if (mArrayBindSources.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.source_error))
        } else {
            mShowAlertDialogWithListview(sp_Spinner_Lead_Source, mArrayBindSources as ArrayList<String>, s)
        }


    }

    private fun getAllProduct(sp_Spinner_Interested_Product: EditText, s: String) {

        if (mArrayBindProduct.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.product_error))
        } else {
            mShowAlertDialogWithListview(sp_Spinner_Interested_Product, mArrayBindProduct as ArrayList<String>, s)
        }

    }


    private fun mShowAlertDialogWithListview(sp_Spinner_Lead_Source: EditText?, animals: ArrayList<String>, s: String) {


        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)

        val cs = animals.toArray(arrayOfNulls<CharSequence>(animals.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->

            sp_Spinner_Lead_Source!!.setText(animals[item].toString())
            userId = item
            // mUserId = "" + mArrayUserId.get(item)


        })


        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()

//
//
//        var mAdapter = Simple_Adapter(activity, animals)
//        val inflater = layoutInflater
//
//        // create view for add item in dialog
//
//        val convertView = inflater.inflate(R.layout.alert_dialogview, null) as View
//        dialogBuilder.setView(convertView)
//
//        val lv = convertView.findViewById(R.id.rc_RecyclerView) as RecyclerView
//
//        val mLayoutManager = GridLayoutManager(activity, 1)
//        lv!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
//        lv!!.adapter = mAdapter
//        mAdapter!!.notifyDataSetChanged()


//        dialogBuilder.setAdapter(adapter, DialogInterface.OnClickListener { dialog, item ->
//
//            UsefullData.Log("==onClick==")
//            sp_Spinner_Lead_Source!!.setText(animals[item].toString())
//            userId = item
//
//        })
//        var adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_single_choice, animals);


//        dialogBuilder.setAdapter(adapter, object : AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface?, item: Int) {
//
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                UsefullData.Log("==onNothingSelected==")
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                UsefullData.Log("==onNothingSelected==")
//
//            }
//
//        })


//


        /*


          val builder = AlertDialog.Builder(activity)
        builder.setTitle("Show dialog")

        val choiceList = arrayOf<CharSequence>("Coke", "Pepsi", "Sprite", "Seven Up")

        val selected = -1 // does not select anything


        builder.setSingleChoiceItems(
                choiceList,
                selected
        ) { dialog, which ->
            Toast.makeText(
                    activity,
                    "Select " + choiceList[which],
                    Toast.LENGTH_SHORT
            )
                    .show()
        }
        val alert = builder.create()
        alert.show()
         */

    }


    private fun callDataToServer() {


        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("Address1", et_Address.text.toString())
        paramObject.put("Address2", et_Address.text.toString())
        paramObject.put("Address3", et_Address.text.toString())
        paramObject.put("State", stateId)
        paramObject.put("City", et_City.text.toString())
        paramObject.put("PinCode", et_PinCode.text.toString())
        paramObject.put("Phone", et_Phone.text.toString())
        paramObject.put("Mobile", et_Mob.text.toString())
        paramObject.put("Email", et_Email.text.toString())
        paramObject.put("Alt_Mobile", et_AlternativeMobile.text.toString())
        paramObject.put("LeadName", et_Name.text.toString())


        paramObject.put("Company", et_Company.text.toString())
        paramObject.put("JobTitle", et_jobTitle.text.toString())
        paramObject.put("Department", et_Department.text.toString())
        paramObject.put("SecondaryEmail", et_SecondaryEmail.text.toString())
        paramObject.put("Skype", et_Skype.text.toString())
        paramObject.put("DealName", et_DealName.text.toString())
        paramObject.put("DealValue", et_DealValue.text.toString())
        paramObject.put("ExpectedCloseDate", et_ExpectedCloseDate.text.toString())
        paramObject.put("Fax", et_Fax.text.toString())
        paramObject.put("Website", et_Website.text.toString())

        paramObject.put("UserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
        paramObject.put("RoleId", Integer.parseInt(objSaveData.getString(ConstantValue.ROLE_ID)))
        paramObject.put("ManagerId", Integer.parseInt(objSaveData.getString(ConstantValue.MANAGER_ID)))
        paramObject.put("ClientId", Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        paramObject.put("EmployeeCode", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
        paramObject.put("H_Checked", H!!)
        paramObject.put("W_Checked", W!!)
        paramObject.put("C_Checked", C!!)
        paramObject.put("Rating", Rating!!)

        paramObject.put("ImageName_PC", ImageName_PC)
        paramObject.put("ContentType_PC", ContentType_PC!!)

        paramObject.put("Data_PC", Img_PC)

        paramObject.put("ImageName_VC1", ImageName_VC1)
        paramObject.put("ContentType_VC1", ContentType_VC1)
        paramObject.put("Data_VC1", Img_VC1)

        paramObject.put("ImageName_VC2", ImageName_VC2)
        paramObject.put("ContentType_VC2", ContentType_VC2)
        paramObject.put("Data_VC2", Img_VC2)



        if(TextUtils.isEmpty(et_DemoDate.text.toString())) {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val d = sdf.parse("01/01/1990")
            Log.e("Paras","Demo date null"+sdf.format(d))
            paramObject.put("DemoDate", sdf.format(d))

        }
        else
        {
            Log.e("Paras","Demo date is not null"+et_DemoDate.text.toString())
            paramObject.put("DemoDate", et_DemoDate.text.toString())

        }

        paramObject.put("LeadSource", sp_Spinner_Lead_Source.text.toString())
        paramObject.put("LeadStage", sp_Spinner_Lead_Stage.text.toString())
        paramObject.put("InteractProduct", sp_Spinner_Interested_Product.text.toString())
        paramObject.put("Interaction_By", sp_Next_Itrection_By.text.toString())
        paramObject.put("Interaction_On", et_NextInteractionDate.text.toString())
        paramObject.put("Comments", et_Comments.text.toString())
        paramObject.put("ResponseOfInteraction", sp_Response_Of_Iteraction.text.toString())
        paramObject.put("EnquiryNum", EnquiryNum)
        paramObject.put("Status", "Active")




        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())


        val mCall = apiEndpointInterface!!.addLead(body)

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
                    if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())


                        var array = JSONArray(response.body().toString())

                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }

                        if (success.equals("1")) {

                            leadAddedOrNot = "yes"

                            val bundle = arguments
                            val uploadRecordings = bundle.getString("uploadRecordings")
                            if (uploadRecordings != null) {
                                if (uploadRecordings.equals("yes")) {
                                    Log.e("Phone number in", "add Lead while Uploading" + bundle.getString("phoneNumber"))
                                    val syncState = objSaveData.getString("mSyncState")
                                    if (syncState == "0") {
                                        if (isConnectedWifi(context)) {

                                            val intent = Intent(context, UploadService::class.java)
                                            //intent.putExtra("phoneNumber",bundle.getString("phoneNumber"))

                                            context.startService(intent)
                                        } else {
                                            //Log.e("FilePath", "" + fileName)
                                            //saveToDatabaseActivity()

                                        }
                                    } else if (syncState == "2") {
                                        if (isConnectedMobile(context)) {

                                            val intent = Intent(context, UploadService::class.java)
                                            //intent.putExtra("phoneNumber",bundle.getString("phoneNumber"))
                                            context.startService(intent)

                                        } else {
                                            //Log.e("FilePath", "" + fileName)
                                            //saveToDatabaseActivity()

                                        }
                                    } else {
                                        if (isConnected(context)) {

                                            val intent = Intent(context, UploadService::class.java)
                                            //intent.putExtra("phoneNumber",bundle.getString("phoneNumber"))
                                            context.startService(intent)

                                        } else {
                                            //Log.e("FilePath", "" + fileName)
                                            //saveToDatabaseActivity()
                                        }

                                    }

                                }
                            }
                            objUsefullData.showMsgOnUI("Lead Added Sucessfully")



                            objSaveData.save(ConstantValue.CLOSELEAD, "0")
                            val fragment = User_Search_Fragment.newInstance("1", objSaveData.getString(ConstantValue.USER_ID))
                            val fragmentManager = fragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                            fragmentTransaction.replace(R.id.content_frame, fragment)
                            fragmentTransaction.commit()
                            clearAllValue()

                        } else {
                            objUsefullData.showMsgOnUI("Lead added failed")
                        }

                    } else {

                        UsefullData.Log("========" + response.code())

                        objUsefullData.getError("" + response.code())

                    }
                }
                catch (e: Exception) {

                    objUsefullData.getException(e)



                }

            }

        })


    }

    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    fun getNetworkInfo(context: Context): NetworkInfo {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    private fun clearAllValue() {

        et_Address.setText("")
        et_City.setText("")
        et_PinCode.setText("")
        et_Phone.setText("")
        et_Mob.setText("")
        et_Email.setText("")
        et_AlternativeMobile.setText("")
        et_Name.setText("")
        et_Company.setText("")



        ImageName_PC = ""
        ContentType_PC = ""

        Img_PC = ""

        ImageName_VC1 = ""
        ContentType_VC1 = ""
        Img_VC1 = ""

        ImageName_VC2 = ""
        ContentType_VC2 = ""
        Img_VC2 = ""


    }


    //==============================================================\\
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        UsefullData.Log("====resultCode====" + resultCode)
        UsefullData.Log("" + data + "====Request====" + requestCode)

        if (requestCode == PHOTOCAM && resultCode == Activity.RESULT_OK) {


            setImage(img_Photo, myUri, 0)


        } else if (requestCode == PHOTOGAL && resultCode == Activity.RESULT_OK && null != data) {

            UsefullData.Log("====photo====" + data.data)

            setImage(img_Photo, data.data, 0)


        } else if (requestCode == VC1CAM && resultCode == Activity.RESULT_OK) {
            setImage(img_VC1, myUri, 1)


        } else if (requestCode == VC1GAL && resultCode == Activity.RESULT_OK && null != data) {
            setImage(img_VC1, data.data, 1)
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



            reCreateFile(imgBmp, photo)

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
            reCreateFile(bitmap, photo)


            CAMERA = 0


        }


    }

    // ==========================================//
    fun rotate(src: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun createFileFromPicturePath(picturePath: String, photo: Int, imgBmp: Bitmap?) {

        var path = userfile!!.path


        if (photo == 0) {
            ImageName_PC = path.substring(path.lastIndexOf("/") + 1)
            Img_PC = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)


        } else if (photo == 1) {

            ImageName_VC1 = path.substring(path.lastIndexOf("/") + 1)
            Img_VC1 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)

        } else if (photo == 2) {
            ImageName_VC2 = path.substring(path.lastIndexOf("/") + 1)
            Img_VC2 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)
        }
    }

    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {


        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
        return baos.toByteArray()
    }

    // ==========================================//
    fun getPath(uri: Uri): String {
        val selectedImage = uri
        val filePathColumn = arrayOf<String>(MediaStore.MediaColumns.DATA)
        val cursor = activity.contentResolver.query(selectedImage,
                filePathColumn, null, null, null)
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        return picturePath
    }


    //===============================================================\\
    private fun reCreateFile(_bitmapScaled: Bitmap?, photo: Int) {
        var path = userfile!!.path
        var baos = ByteArrayOutputStream()
        val imgBmp = Bitmap.createScaledBitmap(_bitmapScaled, 400, 400,
                true)



        imgBmp.compress(Bitmap.CompressFormat.PNG, 90, baos)
        UsefullData.Log("User File :: " + userfile!!.path)

        if (photo == 0) {
            ImageName_PC = path.substring(path.lastIndexOf("/") + 1)
            Img_PC = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)


        } else if (photo == 1) {

            ImageName_VC1 = path.substring(path.lastIndexOf("/") + 1)
            Img_VC1 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)

        } else if (photo == 2) {
            ImageName_VC2 = path.substring(path.lastIndexOf("/") + 1)
            Img_VC2 = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                    Base64.NO_WRAP)
        }
    }


    private fun mGalleryCall(code: Int) {

        GALLERY = 11


        userfile = objUsefullData
                .createFile("userfile.png")
        val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code)


    }


    private fun mCameraCall(code: Int) {


        UsefullData.Log("==========mCameraCall===========")
        CAMERA = 22
        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        userfile = objUsefullData.createFile("userfile.png")

        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(userfile))
        startActivityForResult(photoCaptureIntent, code)

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


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
        fun newInstance(param1: String, param2: String): User_Add_Enquiry_Fragment {
            val fragment = User_Add_Enquiry_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

