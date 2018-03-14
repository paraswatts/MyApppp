package com.nitesh.brill.saleslines._User_Classes.User_Fragment


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_user_update_enquiry.*
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.alert
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class User_Update_Enquiry_Fragment : BaseFragment() {


    // TODO: Rename and change types of parameters
    private var mEnqId: String? = null
    private var mLeadId: String? = null
    private var mDemoId: String? = null
    private var mRating: String? = null

    private var mEnqNumber: String? = ""

    var H: Boolean? = false
    var W: Boolean? = false
    var C: Boolean? = false
    var Rating: Float? = 0.0f

    var pos: Int = 0

    private var LeadStage: String? = ""
    private var InteractProduct: String? = ""
    private var Interaction_By: String? = ""
    private var Interaction_On: String? = ""
    private var ResponseOfInteraction: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mEnqId = arguments.getString(ARG_PARAM1)
            mLeadId = arguments.getString(ARG_PARAM2)
            mDemoId = arguments.getString(ARG_PARAM3)
            mRating = arguments.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_update_enquiry, container, false)

    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Rating = mRating!!.toFloat()


        //===========================\\
        if (isNetworkConnected) {
            getLeadDetailsFromServer()


            //===========================\\

            getLeadPreviousDetails()


            //=======================================\\

            getLeadDemos()
        }
        et_DemoDate.setOnClickListener {

            objUsefullData.getDemoDatePicker(et_DemoDate)
            et_DemoDate.setBackgroundResource((R.drawable.text_view_border))
            et_DemoDate.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))


        }

        //=================================\\
        et_NextInteractionDate.setOnClickListener {

            et_NextInteractionDate.setBackgroundResource((R.drawable.text_view_border))
            et_NextInteractionDate.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))


            objUsefullData.getNextInteractionDatePickerOn(et_NextInteractionDate)

        }

        //==================cb_W===============\\
        cb_H.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if (isChecked) {
                    H = true

                    cb_C.setChecked(false)
                    cb_W.setChecked(false)


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

                    cb_C.setChecked(false)
                    cb_H.setChecked(false)

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

                    cb_W.setChecked(false)
                    cb_H.setChecked(false)

                } else {
                    C = false


                }
                ll_LeadStatus.setBackgroundResource((R.drawable.white_border))
            }
        })


        //======================= Lead Source=====================\\
        sp_Spinner_Lead_Stage.setOnClickListener {

            //=================================\\

            //=================================\\
            if (isNetworkConnected)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Spinner_Lead_Stage.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.text_view_border, null));
                } else {
                    sp_Spinner_Lead_Stage.setBackground(activity.resources.getDrawable(R.drawable.text_view_border))
                }
            sp_Spinner_Lead_Stage.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))


            getBindStages(sp_Spinner_Lead_Stage, "Lead Stage")


        }


        //======================= Intrested Pproject =====================\\
        sp_Spinner_Interested_Product.setOnClickListener {

            //============================\\
            if (isNetworkConnected)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Spinner_Interested_Product.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.text_view_border, null));
                } else {
                    sp_Spinner_Interested_Product.setBackground(activity.resources.getDrawable(R.drawable.text_view_border))
                }
            sp_Spinner_Interested_Product.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))

            getAllProduct(sp_Spinner_Interested_Product, "Interested Product")

        }


        //=======================Next_Itrection=====================\\
        sp_Next_Intrection_By.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sp_Next_Intrection_By.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.text_view_border, null));
            } else {
                sp_Next_Intrection_By.setBackground(activity.resources.getDrawable(R.drawable.text_view_border))
            }
            sp_Next_Intrection_By.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))

            val Animals = activity.resources.getStringArray(R.array.next_interaction)
            mShowAlertDialog(sp_Next_Intrection_By, Animals, "Next Interaction")

        }

        //=======================Response_Of_Iteraction=====================\\
        sp_Response_Of_Iteraction.setOnClickListener {
            if (isNetworkConnected)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sp_Response_Of_Iteraction.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.text_view_border, null));
                } else {
                    sp_Response_Of_Iteraction.setBackground(activity.resources.getDrawable(R.drawable.text_view_border))
                }
            sp_Response_Of_Iteraction.setPadding(objUsefullData.dpToPx(paddingLeft), objUsefullData.dpToPx(paddingTop), objUsefullData.dpToPx(paddingRight), objUsefullData.dpToPx(paddingBottom))


            getBindResponse(sp_Response_Of_Iteraction, "Response")
        }


        //=================================\\

        btn_Update.setOnClickListener {


            if (checkValidation()) {


                //=================================\\
                if (isNetworkConnected)
                    UsefullData.Log("=========== Interaction_By========" + Interaction_By)
                sendDataToServerForUpdate()


            }

        }

        //===================================\\

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            Rating = rating

        }


        btn_SaleClouser.setOnClickListener {


            if (checkValidationSale()) {

                alert(title = "Sale Close", message = "Do you really want to close this sale.") {
                    positiveButton("Yes") {

                        //=================================\\
                        if (isNetworkConnected)
                        {
                            var mFragment = User_Sale_Clouser_Fragment.newInstance("" + mLeadId,
                                    "" + mEnqId,
                                    "" + mDemoId,
                                    "" + mEnqNumber,
                                    "" + InteractProduct,
                                    ""+et_DemoDate.text.toString(),
                                        ""+LeadStage,
                                    ""+InteractProduct,
                                    ""+et_Comments.text.toString(),
                                    ""+ResponseOfInteraction,
                                    ""+Rating!!,
                                    ""+H!!,
                                    ""+W!!,
                                    ""+C!!

                                    )
                            val fragmentManager = fragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                            fragmentTransaction.replace(R.id.content_frame, mFragment)
                            fragmentTransaction.commit()
                        }
                            //sendSaleCloseLeadDemoDetails()
                    }
                    negativeButton("No") { }
                }.show()


            }


        }
    }


    private fun getLeadPreviousDetails() {

        objUsefullData.showProgress("fetching details... ", "")

        val mCall = smallapiEndpointInterface!!.mGetDemoStageProduct("" + mLeadId, "" + mDemoId)


        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "==fdsfsfsfs=" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)


                            if (!item.getString("LeadStage").equals(null) && !item.getString("LeadStage").equals("null") && !item.getString("LeadStage").isEmpty()) {
                                sp_Spinner_Lead_Stage.setText(item.getString("LeadStage"))
                                LeadStage = item.getString("LeadStage")
                            }
                            if (!item.getString("InteractProduct").equals(null) && !item.getString("InteractProduct").equals("null") && !item.getString("InteractProduct").isEmpty()) {
                                sp_Spinner_Interested_Product.setText(item.getString("InteractProduct"))
                                InteractProduct = item.getString("InteractProduct")
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

    private fun checkValidationSale(): Boolean {
        if (objValidation.checkEmpty(et_Comments, "Comment ")) {
            et_Comments.requestFocus()
            return false
        }



        if (!H!! && !W!! && !C!!) {

            objUsefullData.showMsgOnUI(" Please select lead status")
            ll_LeadStatus.setBackgroundResource((R.drawable.red_border))
            return false

        }


        if (LeadStage.equals("")) {

            objUsefullData.showMsgOnUI(" Please select lead stage")
            sp_Spinner_Lead_Stage.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Lead_Stage.requestFocus()
            return false

        }
        if (InteractProduct.equals("")) {
            objUsefullData.showMsgOnUI(" Please select interact product")
            sp_Spinner_Interested_Product.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Interested_Product.requestFocus()
            return false

        }

        if (ResponseOfInteraction.equals("")) {
            objUsefullData.showMsgOnUI(" Please select response of interaction")
            sp_Response_Of_Iteraction.setBackgroundResource((R.drawable.red_border))
            sp_Response_Of_Iteraction.requestFocus()
            return false

        }

        return true
    }

    //================//
    private fun checkValidation(): Boolean {
        if (objValidation.checkEmpty(et_Comments, "Comment ")) {
            et_Comments.requestFocus()
            return false
        }

        if (!H!! && !W!! && !C!!) {

            objUsefullData.showMsgOnUI(" Please select lead status")
            ll_LeadStatus.setBackgroundResource((R.drawable.red_border))
            return false

        }



        if (LeadStage.equals("")) {

            objUsefullData.showMsgOnUI(" Please select lead stage")
            sp_Spinner_Lead_Stage.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Lead_Stage.requestFocus()
            return false

        }
        if (InteractProduct.equals("")) {
            objUsefullData.showMsgOnUI(" Please select interact product")
            sp_Spinner_Interested_Product.setBackgroundResource((R.drawable.red_border))
            sp_Spinner_Interested_Product.requestFocus()
            return false

        }
        if (Interaction_By.equals("")) {
            objUsefullData.showMsgOnUI(" Please select next itneraction")
            sp_Next_Intrection_By.setBackgroundResource((R.drawable.red_border))
            sp_Next_Intrection_By.requestFocus()
            return false

        }

        if (et_NextInteractionDate.text.toString().equals("")) {

            objUsefullData.showMsgOnUI(" Please select next itraction date")
            et_NextInteractionDate.setBackgroundResource((R.drawable.red_border))
            et_NextInteractionDate.requestFocus()
            return false

        }
        if (ResponseOfInteraction.equals("")) {
            objUsefullData.showMsgOnUI(" Please select response of interaction")
            sp_Response_Of_Iteraction.setBackgroundResource((R.drawable.red_border))
            sp_Response_Of_Iteraction.requestFocus()
            return false

        }

        return true
    }

    //=================================\\
//    private fun sendSaleCloseLeadDemoDetails() {
//
//
//        objUsefullData.showProgress("Please Wait...", "")
//        val paramObject = JSONObject()
//        paramObject.put("LeadId", mLeadId)
//        paramObject.put("DemoDate", et_DemoDate.text.toString())
//        paramObject.put("LeadSource", "")
//        paramObject.put("LeadStage", LeadStage)
//        paramObject.put("InteractProduct", InteractProduct)
//        paramObject.put("Interaction_By", "")
//        paramObject.put("Interaction_On", "1/1/1900 0:00:00")
//        paramObject.put("Comments", et_Comments.text.toString())
//        paramObject.put("ResponseOfInteraction", ResponseOfInteraction)
//        paramObject.put("Rating", Rating!!)
//        paramObject.put("H_Checked", H!!)
//        paramObject.put("W_Checked", W!!)
//        paramObject.put("C_Checked", C!!)
//        paramObject.put("EnquiryId", mEnqId)
//        paramObject.put("DemoId", mDemoId)
//
//        UsefullData.Log("========sendDataToServerForUpdate==========" + paramObject.toString())
//
//
//        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
//
//
//        val mCall = apiEndpointInterface!!.mSaleCloseLeadDemoDetails("" + mDemoId, body)
//
//        mCall.enqueue(object : Callback<JsonElement> {
//            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
//                objUsefullData.dismissProgress()
//                objUsefullData. getThrowableException(t)
//                UsefullData.Log("onFailure ===" + t)
//            }
//
//            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
//                objUsefullData.dismissProgress()
//                Log.d("URL", "===" + response!!.raw().request().url())
//                try {
//
//                    if (response.isSuccessful) {
//                        UsefullData.Log("User_Response    " + response.body().toString())
//
//
//                        var array = JSONArray(response!!.body().toString())
//                        var success = ""
//                        for (i in 0..(array.length() - 1)) {
//                            val item = array.getJSONObject(0)
//                            success = item.optString("Success")
//
//                        }
//                        if (success.equals("1")) {
//                            //objUsefullData.showMsgOnUI("Successfully Saved")
//                            //=================================\\
//                            sendUpdateEnqDetails_()
//
//                        } else {
//                            objUsefullData.showMsgOnUI("Update failed")
//                        }
//                    } else {
//
//                        UsefullData.Log("========" + response.code())
//                        objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))
//
//                    }
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//            }
//
//        })
//
//
//    }

    //=================================\\
    private fun sendUpdateEnqDetails() {

        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("IneterestProduct", InteractProduct)
        paramObject.put("LeadStage", LeadStage)
        paramObject.put("Status", "A")
        paramObject.put("NextInteractionBy", Interaction_By)
        paramObject.put("NextInteractionDate", et_NextInteractionDate.text.toString())
        paramObject.put("DemoGivenDate", et_DemoDate.text.toString())
        paramObject.put("EnquiryId", mEnqId)
        paramObject.put("LeadId", mLeadId)
        UsefullData.Log("========sendDataToServerForUpdate==========" + paramObject.toString())
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val mCall = apiEndpointInterface!!.mUpdateEnquiryDetails("" + mEnqId, "" + mLeadId, body)

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


                        UsefullData.Log(" GMResponse    " + response.body().toString())


                        var array = JSONArray(response!!.body().toString())

                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            success = item.optString("Success")

                        }
                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Update")

                            //fragmentManager.popBackStack()
                        var fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                        val fragmentManager = activity.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                        fragmentTransaction.replace(R.id.content_frame, fragment)
                        fragmentTransaction.commit()

                        } else {
                            objUsefullData.showMsgOnUI("Update Failed")
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

    //=================================\\
//    private fun sendUpdateEnqDetails_() {
//
//        objUsefullData.showProgress("Please Wait...", "")
//        val paramObject = JSONObject()
//        paramObject.put("IneterestProduct", InteractProduct)
//        paramObject.put("LeadStage", LeadStage)
//        paramObject.put("Status", "A")
//        paramObject.put("NextInteractionBy", "")
//        paramObject.put("NextInteractionDate", "1/1/1900 0:00:00")
//        paramObject.put("DemoGivenDate", et_DemoDate.text.toString())
//        paramObject.put("EnquiryId", mEnqId)
//        paramObject.put("LeadId", mLeadId)
//        UsefullData.Log("========sendDataToServerForUpdate==========" + paramObject.toString())
//        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
//
//        val mCall = apiEndpointInterface!!.mUpdateEnquiryDetails("" + mEnqId, "" + mLeadId, body)
//
//        mCall.enqueue(object : Callback<JsonElement> {
//            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
//                objUsefullData.dismissProgress()
//                objUsefullData. getThrowableException(t)
//                UsefullData.Log("onFailure ===" + t)
//            }
//
//            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
//                objUsefullData.dismissProgress()
//                Log.d("URL", "===" + response!!.raw().request().url())
//
//                try {
//                    if (response.isSuccessful) {
//
//                        UsefullData.Log("User_Response    " + response.body().toString())
//
//
//                        var array = JSONArray(response!!.body().toString())
//                        var success = ""
//                        for (i in 0..(array.length() - 1)) {
//                            val item = array.getJSONObject(0)
//                            success = item.optString("Success")
//
//                        }
//
//                        if (success.equals("1")) {
//                            objUsefullData.showMsgOnUI("Successfully Update")
//
//                            var mFragment = User_Sale_Clouser_Fragment.newInstance("" + mLeadId, "" + mEnqId, "" + mDemoId, "" + mEnqNumber, "" + InteractProduct)
//                            val fragmentManager = fragmentManager
//                            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
//                            fragmentTransaction.replace(R.id.content_frame, mFragment)
//                            fragmentTransaction.commit()
//
//                        } else {
//                            objUsefullData.showMsgOnUI("Update Failed")
//                        }
//                    } else {
//
//                        UsefullData.Log("========" + response.code())
//                        objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))
//
//                    }
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//            }
//
//        })
//
//
//    }

    //=================================\\
    private fun sendDataToServerForUpdate() {

        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("LeadId", mLeadId)
        paramObject.put("DemoDate", et_DemoDate.text.toString())
        paramObject.put("LeadSource", "")
        paramObject.put("LeadStage", LeadStage)
        paramObject.put("InteractProduct", InteractProduct)
        paramObject.put("Interaction_By", Interaction_By)
        paramObject.put("Interaction_On", et_NextInteractionDate.text.toString())
        paramObject.put("Comments", et_Comments.text.toString())
        paramObject.put("ResponseOfInteraction", ResponseOfInteraction)
        paramObject.put("Rating", Rating!!)
        paramObject.put("H_Checked", H!!)
        paramObject.put("W_Checked", W!!)
        paramObject.put("C_Checked", C!!)
        paramObject.put("EnquiryId", mEnqId)
        paramObject.put("DemoId", mDemoId)


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())


        val mCall = apiEndpointInterface!!.mSaveLeadDemoDetails("" + mDemoId, body)

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

                        UsefullData.Log(" GMResponse    " + response.body().toString())
                        var array = JSONArray(response!!.body().toString())


                        var success = ""
                        for (i in 0..(array.length() - 1)) {

                            val item = array.getJSONObject(0)
                            success = item.optString("Success")

                        }
                        if (success.equals("1")) {
                            //objUsefullData.showMsgOnUI("Successfully Saved")
                            //
                            // =================================\\
                            sendUpdateEnqDetails()
                        } else {
                            objUsefullData.showMsgOnUI("Update Failed")
                        }
                    } else {

                        UsefullData.Log("========" + response.code())
                        objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                    }
                }catch (e:Exception)
                {
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

            Interaction_By = animals!![item].toString()


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }

//    private fun getBindResponse(sp_Response_Of_Iteraction: EditText, s: String) {
//        mShowAlertDialogWithListview(sp_Response_Of_Iteraction, mArrayBindResponse as ArrayList<String>, s, 3)
//
//
//    }
//
//
//    private fun getAllProduct(sp_Spinner_Interested_Product: EditText, s: String) {
//        mShowAlertDialogWithListview(sp_Spinner_Interested_Product, mArrayBindProduct as ArrayList<String>, s, 2)
//
//    }
//
//
//    private fun getBindStages(sp_Spinner_Lead_Stage: EditText, s: String) {
//        mShowAlertDialogWithListview(sp_Spinner_Lead_Stage, mArrayBindStages as ArrayList<String>, s, 1)
//    }


    private fun getBindResponse(sp_Response_Of_Iteraction: EditText, s: String) {

        if (mArrayBindResponse.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.response_error))
        } else {
            mShowAlertDialogWithListview(sp_Response_Of_Iteraction, mArrayBindResponse as ArrayList<String>, s, 3)
        }


    }

    private fun getBindStages(sp_Spinner_Lead_Stage: EditText, s: String) {
        if (mArrayBindStages.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.stage_error))
        } else {
            mShowAlertDialogWithListview(sp_Spinner_Lead_Stage, mArrayBindStages as ArrayList<String>, s, 1)
        }


    }


    private fun getAllProduct(sp_Spinner_Interested_Product: EditText, s: String) {

        if (mArrayBindProduct.size < 1) {

            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.product_error))
        } else {
            mShowAlertDialogWithListview(sp_Spinner_Interested_Product, mArrayBindProduct as ArrayList<String>, s, 2)
        }

    }


    private fun mShowAlertDialogWithListview(sp_Spinner_Lead_Source: EditText?, animals: ArrayList<String>, s: String, i: Int) {


        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)

        val cs = animals.toArray(arrayOfNulls<CharSequence>(animals.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->

            sp_Spinner_Lead_Source!!.setText(animals!![item].toString())
            when (i) {
                1 -> LeadStage = animals!![item].toString()
                2 -> InteractProduct = animals!![item].toString()
                3 -> ResponseOfInteraction = animals!![item].toString()

            }

        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }


    private fun getLeadDemos() {

        val mCall = apiEndpointInterface!!.mGetLeadDemos((mEnqId!!))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)
                            et_DemoDate.setText(item.getString("DemoDate"))

                            if (!item.getString("Comments").equals(null) && !item.getString("Comments").equals("null")) {
                                et_Comments.setText(item.getString("Comments"))
                            }

                            if (item.getString("H_Checked").equals("H")) {

                                cb_H.isChecked = true
                            } else if (item.getString("H_Checked").equals("W")) {
                                cb_W.isChecked = true
                            } else if (item.getString("H_Checked").equals("C")) {
                                cb_C.isChecked = true

                            }

                            if (!item.getString("Rating").equals(null) && !item.getString("Rating").equals("null")) {
                                ratingBar.rating = item.getString("Rating").toFloat()
                                //   et_Comments.setText(item.getString("Rating"))
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

    private fun getLeadDetailsFromServer() {
        objUsefullData.showProgress("fetching details... ", "")

        val mCall = smallapiEndpointInterface!!.mGetEnquiryDetails((mEnqId.toString()))


        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {

                if (response.isSuccessful) {
                    UsefullData.Log("User_Response    " + response.body().toString())

                    val array = JSONArray(response!!.body().toString())
                    for (i in 0..(array.length() - 1)) {
                        val item = array.getJSONObject(i)

                        et_EnquiryNumber.setText("" + item.getString("EnquiryNum"))
                        mEnqNumber = (item.getString("EnquiryNum"))


                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }

                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }


        })


    }


    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"
        private val ARG_PARAM4 = "param4"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, param3: String?, param4: String): User_Update_Enquiry_Fragment {
            val fragment = User_Update_Enquiry_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            args.putString(ARG_PARAM4, param4)
            fragment.arguments = args
            return fragment
        }
    }


}
