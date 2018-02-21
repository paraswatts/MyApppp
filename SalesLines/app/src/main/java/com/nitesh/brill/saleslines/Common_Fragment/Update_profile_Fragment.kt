package com.nitesh.brill.saleslines.Common_Fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_GetPreviousDetails
import kotlinx.android.synthetic.main.fragment_user_update_profile.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.singleLine
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat

class Update_profile_Fragment : BaseFragment(), ConstantValue {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mCount = 0
    private var leadId: String? = ""

    internal var b: Byte = 100

    private var flag = false
    private var CompanyName: String? = ""
    private var DOJ: String? = ""
    private var Designation: String? = ""
    // val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    var SectorPreviouslyWorked: String = ""
    var mPreviousId: String = ""
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
        return inflater!!.inflate(R.layout.fragment_user_update_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
       *   GetUserProfileData of the manager from server
       */
        if (isNetworkConnected) {
            getUserProfileData()


            /*
             *    GetPreviousDetails of the manager from server
             */

            getPreviousDetails()

        }


        // ll_PreviousEmp!!.visibility = View.GONE
//        et_Email!!.setText("comgm@gmail.com")
//        et_Password!!.setText("comgm")


        //=======================================\\

//        img_Previous_Emp!!.setOnClickListener {
//
//            if (!flag) {
//                ll_PreviousEmp!!.visibility = View.VISIBLE
//                flag = true
//            } else {
//
//                ll_PreviousEmp!!.visibility = View.GONE
//                flag = false
//            }
//
//        }

        //=======================================\\

        btn_Save.setOnClickListener {

            if (checkValidation()) {
                if (isNetworkConnected)
                    sendProfileToServer()
                if (flag) {

                    //sendPreviousDataeToServer(et_one.text.toString().trim(), et_two.text.toString().trim(), et_three.text.toString().trim(), et_four.text.toString().trim(), et_five.text.toString().trim(), Id)
                }


            }

        }


        //=======================================\\

        et_DOB.setOnClickListener {

            objUsefullData.getDatePicker(et_DOB)

        }


        //=======================================\\
        et_PreviousCompanyDoj.setOnClickListener {
            objUsefullData.getDatePicker(et_PreviousCompanyDol)

        }


        //=======================================\\
        et_PreviousCompanyDol.setOnClickListener {


            objUsefullData.getDatePicker(et_PreviousCompanyDol)
        }

        //================================================\\

        et_Spinner.setOnClickListener {

            //ShowAlertDialogWithListview(et_five)
        }

        //================================================\\
        val et_one = EditText(this!!.activity)
        val et_two = EditText(activity)
        val et_three = EditText(activity)
        val et_four = EditText(activity)
        val bt_plus = Button(activity)

        //================================================\\


        img_Previous_Emp.setOnClickListener(getOnClickDoSomething(et_one, et_two, et_three, et_four, bt_plus))

    }

    //================================================\\
    private fun sendPreviousDataeToServer(trim: String, trim1: String, trim2: String, trim3: String, trim4: String, id: String) {

        try {


            val paramObject = JSONObject()
            paramObject.put("CompanyName", trim)
            paramObject.put("Designation", trim1)
            paramObject.put("DateOfJoin", trim2)
            paramObject.put("DateOfLeaving", trim3)
            paramObject.put("SectorPreviouslyWorked", trim4)
            paramObject.put("Id", id)

            Log.d("", "=========updatePreviousDetails=========" + paramObject)


            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
            val call = apiEndpointInterface!!.updatePreviousDetails2(Integer.parseInt(id), body)

            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                    Log.d("URL", "===updatePreviousDetails==" + response!!.raw().request().url())

                    try {
                        if (response.isSuccessful) {
                            UsefullData.Log("=============" + response!!.body().toString())


                            var array = JSONArray(response!!.body().toString())

                            var success = ""

                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(0)

                                UsefullData.Log("===" + item)
                                success = item.optString("Success")

                            }



                            if (success.equals("1")) {
                                objUsefullData.showMsgOnUI("Successfully Updated")


                            } else {
                                objUsefullData.showMsgOnUI("Update failed")
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
                    objUsefullData. getThrowableException(t)

                    UsefullData.Log("=============" + t)

                }

            })


        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    private fun savePreviousDataeToServer(trim: String, trim1: String, trim2: String, trim3: String, trim4: String) {

        try {


            val paramObject = JSONObject()
            paramObject.put("CompanyName", trim)
            paramObject.put("Designation", trim1)
            paramObject.put("DateOfJoin", trim2)
            paramObject.put("DateOfLeaving", trim3)
            paramObject.put("SectorPreviouslyWorked", trim4)

            paramObject.put("UserId", mParam2)



            UsefullData.Log("=========updatePreviousDetails =========" + paramObject)
            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
            val call = apiEndpointInterface!!.InsertPreviousDetails(body)

            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
try {
    Log.d("URL", "===updatePreviousDetails==" + response!!.raw().request().url())
    if (response.isSuccessful) {

        UsefullData.Log("=============" + response!!.body().toString())


        var array = JSONArray(response!!.body().toString())

        var success = ""
//                        for (i in 0..(array.length() - 1)) {
//                            val item = array.getJSONArray(0)
//
//                            for (i in 0..(item.length() - 1)) {
//                                val item_ = item.getJSONObject(0)
//                                val item2 = item.getJSONObject(2)
//
//                                UsefullData.Log("===" + item_)
//                                success = item_.optString("Success")
//                                leadId = item2.optString("ID")
//
//                            }
//
//                        }


        for (i in 0..(array.length() - 1)) {
            val item = array.getJSONObject(0)

            UsefullData.Log("===" + item)
            success = item.optString("Success")
            leadId = item.optString("ID")

        }
        if (success.equals("1")) {
            objUsefullData.showMsgOnUI("Successfully Saved")


        } else {
            objUsefullData.showMsgOnUI("Save failed")
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


        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    //================================================\

    private fun sendProfileToServer() {

        objUsefullData.showProgress("Please Wait...", "")


        try {


            val paramObject = JSONObject()


            paramObject.put("Name", et_Name.text.toString())
            paramObject.put("RoleId", Integer.parseInt(objSaveData.getString(ConstantValue.ROLE_ID)))
            paramObject.put("ManagerId", Integer.parseInt(objSaveData.getString(ConstantValue.MANAGER_ID)))
            paramObject.put("DateOfJoin", (et_DOB.text.toString()))


            paramObject.put("EmailId", et_Email.text.toString())
            paramObject.put("DateOfBirth", (DOJ))
            paramObject.put("Designation", Designation!!)
            paramObject.put("Education", et_Qualification.text.toString())
            paramObject.put("Status", "Active")
            paramObject.put("ModifiedUserId", Integer.parseInt(mParam2))
            paramObject.put("password", et_Password.text.toString())
            paramObject.put("TotalExperience", et_TotalExp.text.toString())
            paramObject.put("CompanyName", CompanyName!!)
            paramObject.put("LinkedIn", et_LinkedIn.text.toString())
            paramObject.put("Phone", et_Mobile.text.toString())
            paramObject.put("UserId", Integer.parseInt(mParam2))


            UsefullData.Log("==================" + paramObject)

            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
            val call = apiEndpointInterface!!.addUpdateProfile(Integer.parseInt(mParam2), body)

            call.enqueue(object : Callback<JsonElement> {

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
                            var array = JSONArray(response!!.body().toString())

                            var success = ""

                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(0)

                                UsefullData.Log("===" + item)
                                success = item.optString("Success")

                            }




                            if (success.equals("1")) {
                                objUsefullData.showMsgOnUI("Successfully Update")


                            } else {
                                objUsefullData.showMsgOnUI("Update failed")
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

        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }


    //================================================\\
    private fun ShowAlertDialog() {

        val Animals = activity.resources.getStringArray(R.array.role)
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Role")
        dialogBuilder.setItems(Animals, DialogInterface.OnClickListener { dialog, item ->

            val selectedText = Animals[item].toString()  //Selected item in listview
            objUsefullData.showMsgOnUI(selectedText)

        })

        val alertDialogObject = dialogBuilder.create()

        alertDialogObject.show()


    }
    //================================================\\


    private fun ShowAlertDialogWithListview(et_five: EditText) {
        val Animals = activity.resources.getStringArray(R.array.user_previous_sector)
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Previously worked sectors")
        dialogBuilder.setItems(Animals, DialogInterface.OnClickListener { dialog, item ->

            SectorPreviouslyWorked = Animals[item].toString()  //Selected item in listview
            objUsefullData.showMsgOnUI(SectorPreviouslyWorked)
            et_Spinner.setText("" + SectorPreviouslyWorked)
            et_five.setText("" + SectorPreviouslyWorked)

        })

        val alertDialogObject = dialogBuilder.create()

        alertDialogObject.show()
    }

    //=======================================\\
    private fun getPreviousDetails() {

        val mCall = apiEndpointInterface!!.getPreviousDetails("" + mParam2)
        mCall.enqueue(object : Callback<List<Manager_GetPreviousDetails>> {

            override fun onFailure(call: Call<List<Manager_GetPreviousDetails>>?, t: Throwable?) {
                UsefullData.Log("===onFailure" + t)
                objUsefullData. getThrowableException(t)
            }

            override fun onResponse(call: Call<List<Manager_GetPreviousDetails>>?, response: Response<List<Manager_GetPreviousDetails>>?) {
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        val size = response!!.body().size

                        UsefullData.Log("" + size + "User_Response    " + response.body().toString())

                        for (s in response.body()) {



                            if(s.DateOfJoin.trim().equals("01/01/0001") && s.DateOfLeaving.trim().equals("01/01/0001") && s.SectorPreviouslyWorked.trim().equals("&nbsp;")){

                                mCount = mCount + 1

                                val format = SimpleDateFormat("dd/MM/yyyy")
                                val d = format.parse(s.DateOfJoin)
                                val serverFormat = SimpleDateFormat("MM/dd/yyyy")

                                val j = format.parse(s.DateOfLeaving)

                                viewCreate(s.CompanyName, s.Designation, "", "", "", s.Id)

                                mPreviousId = s.Id
                            }else {


                                mCount = mCount + 1

                                val format = SimpleDateFormat("dd/MM/yyyy")
                                val d = format.parse(s.DateOfJoin)
                                val serverFormat = SimpleDateFormat("MM/dd/yyyy")

                                val j = format.parse(s.DateOfLeaving)

                                viewCreate(s.CompanyName, s.Designation, serverFormat.format(d), serverFormat.format(j), s.SectorPreviouslyWorked, s.Id)

                                mPreviousId = s.Id
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

        })

    }

    private fun viewCreate(companyName: String, designation: String, dateOfJoin: String, dateOfLeaving: String, sectorPreviouslyWorked: String, Id: String) {
        ll_PreviousProfile.visibility = View.GONE
        var ll_PreviousEmp_child = LinearLayout(activity)
        ll_PreviousEmp_child.orientation = LinearLayout.VERTICAL
        val mainLayout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll_PreviousEmp_child.layoutParams = mainLayout


        val paramsButton = LinearLayout.LayoutParams(
                objUsefullData.dpToPx(25), objUsefullData.dpToPx(25))

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))

        val paramsEditText = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, objUsefullData.dpToPx(45))
        paramsEditText.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(4), objUsefullData.dpToPx(8), objUsefullData.dpToPx(4))

        val paramsEditTextFive = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))
        paramsEditTextFive.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(4), objUsefullData.dpToPx(8), objUsefullData.dpToPx(0))

        paramsButton.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(2), 0, 0)

        val three = LinearLayout(activity)
        three.orientation = LinearLayout.HORIZONTAL
        val bt_plus = Button(activity)
        bt_plus.setBackgroundResource((R.drawable.button_add))
        bt_plus.id = mCount

        bt_plus.layoutParams = paramsButton
        UsefullData.Log("Button Id" + bt_plus.id)

        val paramsText = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(25))
        paramsText.gravity = Gravity.CENTER
        paramsText.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(0), 0, 0)


        val paramsTextheader = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        paramsTextheader.gravity = Gravity.CENTER
        paramsTextheader.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(1), 0, objUsefullData.dpToPx(8))
        val textView = TextView(activity)
        textView.layoutParams = paramsTextheader
        textView.text = "Previous Employer"
        textView.textSize = 18f

        val paramsButtonRemove = LinearLayout.LayoutParams(
                90, 90)
        val bt_remove_view = Button(activity)
        paramsButtonRemove.setMargins(16, 8, 0, 0)
        bt_remove_view.layoutParams = paramsButtonRemove
        bt_remove_view.setBackgroundResource(R.drawable.button_remove)
        bt_remove_view.setOnClickListener {
            UsefullData.Log("mCount " + mCount)
            ll_PreviousEmp.removeViewAt(mCount - 1)
            mCount = mCount - 1
        }


        three.addView(bt_plus)
        three.addView(textView)
        three.addView(bt_remove_view)

        //  three.addView(bt_update)


        val five = LinearLayout(activity)
        five.orientation = LinearLayout.HORIZONTAL;

        val param_five = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        five.layoutParams = param_five
        val one = LinearLayout(activity)
        one.orientation = LinearLayout.VERTICAL

        val layoutOne = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll_PreviousEmp_child.layoutParams = mainLayout
        layoutOne.weight = 1.0f
        one.layoutParams = layoutOne

        //=====First EditText======//
        //params.weight = 5.0f
        params.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(0), objUsefullData.dpToPx(4), objUsefullData.dpToPx(0))

        val tv_company = TextView(activity)
        tv_company.layoutParams = paramsText
        tv_company.text = "Company"
        tv_company.textSize = 15.0f

        one.addView(tv_company)

        val mCompanyName = TextView(activity)
        mCompanyName.setText("Company Name")
        val et_one = EditText(activity)

        et_one.textSize = 15f
        et_one.background = resources.getDrawable(R.drawable.text_view_border)
        et_one.layoutParams = params
        et_one.singleLine = true
        et_one.setText(companyName)
        et_one.setHint("Enter company name")
        et_one.imeOptions = EditorInfo.IME_ACTION_NEXT
        et_one.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        one.addView(et_one)

        val tv_JoiningDate = TextView(activity)
        tv_JoiningDate.layoutParams = paramsText
        tv_JoiningDate.text = "Joining Date"
        tv_JoiningDate.textSize = 15.0f

        one.addView(tv_JoiningDate)

        val et_three = EditText(activity)

        et_three.background = resources.getDrawable(R.drawable.text_view_border)
        et_three.layoutParams = params
        et_three.textSize = 15f
        et_three.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
        et_three.setText(dateOfJoin)
        et_three.setHint("Enter date")
        et_three.singleLine = true
        et_three.imeOptions = EditorInfo.IME_ACTION_NEXT
        et_three.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        et_three.setOnClickListener {
            objUsefullData.getDatePicker(et_three)
        }
        et_three.isFocusableInTouchMode = false

        one.addView(et_three)

        val two = LinearLayout(activity)

        two.orientation = LinearLayout.VERTICAL

        two.layoutParams = layoutOne

        val tv_Designation = TextView(activity)
        tv_Designation.layoutParams = paramsText
        tv_Designation.text = "Designation"
        tv_Designation.textSize = 15.0f

        two.addView(tv_Designation)
        //=====Second EditText======//
        val mDesignationName = TextView(activity)
        mDesignationName.setText("Designation")
        val et_two = EditText(activity)
        et_two.background = resources.getDrawable(R.drawable.text_view_border)
        et_two.setText(designation)
        et_two.layoutParams = params
        et_two.setHint("Enter designation")
        et_two.textSize = 15f
        et_two.singleLine = true
        et_two.imeOptions = EditorInfo.IME_ACTION_NEXT
        et_two.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        //=====Third EditText======//


        two.addView(et_two)
        val tv_LeavingDate = TextView(activity)
        tv_LeavingDate.layoutParams = paramsText
        tv_LeavingDate.text = "Leaving Date"
        tv_LeavingDate.textSize = 15.0f

        two.addView(tv_LeavingDate)


        val et_four = EditText(activity)
        et_four.background = resources.getDrawable(R.drawable.text_view_border)
        et_four.textSize = 15f
        et_four.setText(dateOfLeaving)
        et_four.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
        et_four.layoutParams = params
        et_four.singleLine = true
        et_four.setHint("Enter date")
        et_four.imeOptions = EditorInfo.IME_ACTION_NEXT
        et_four.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        et_four.setOnClickListener {
            objUsefullData.getDatePicker(et_four)
        }
        et_four.isFocusableInTouchMode = false

        //=====Fourth EditText======//
        five.addView(one)
        five.addView(two)
        two.addView(et_four)

        val mPreviousWorkingSector = TextView(activity)
        mPreviousWorkingSector.setText("Sector Previously Worked")
        mPreviousWorkingSector.layoutParams = paramsText
        mPreviousWorkingSector.textSize = 15.0f

        val et_five = EditText(activity)
        et_five.background = resources.getDrawable(R.drawable.text_view_border)
        et_five.textSize = 15f
        et_five.setText(sectorPreviouslyWorked)
        et_five.setHint("Enter sector previously worked")
        et_five.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
        et_five.singleLine = true
        et_five.imeOptions = EditorInfo.IME_ACTION_NEXT
        et_five.layoutParams = paramsEditTextFive
        et_five.isFocusableInTouchMode = false
        et_five.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        et_five.setOnClickListener { ShowAlertDialogWithListview(et_five) }


        val paramsButtonUpdate = LinearLayout.LayoutParams(
                objUsefullData.dpToPx(200), objUsefullData.dpToPx(37))

        val bt_update = Button(activity)
        paramsButtonUpdate.gravity = Gravity.CENTER_HORIZONTAL
        paramsButtonUpdate.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8))
        bt_update.setText("Update")
        bt_update.setBackgroundResource(R.drawable.button_background)
        bt_update.setTextColor(Color.parseColor("#FFFFFF"))
        bt_update.textSize = 15f

        bt_update.layoutParams = paramsButtonUpdate
        bt_update.setOnClickListener {
            if (isNetworkConnected) {

                sendPreviousDataeToServer(et_one.text.toString().trim(),
                        et_two.text.toString().trim(),
                        et_three.text.toString().trim(),
                        et_four.text.toString().trim(),
                        et_five.text.toString().trim(),
                        Id
                )
                UsefullData.Log("Previous Values" + et_one.text.toString()
                        + et_two.text.toString()
                        + et_three.text.toString()
                        + et_four.text.toString()
                        + et_five.text.toString()

                )
            }

        }

        var line = View(activity)

        val lineParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3)
        line.layoutParams = lineParams
        lineParams.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(4), objUsefullData.dpToPx(8), objUsefullData.dpToPx(4))
        line.setBackgroundColor(Color.parseColor("#d8dfb5"))
        ll_PreviousEmp_child!!.addView(three)
        ll_PreviousEmp_child!!.addView(five)
        ll_PreviousEmp_child!!.addView(mPreviousWorkingSector)

        ll_PreviousEmp_child!!.addView(et_five)

        ll_PreviousEmp_child!!.addView(bt_update)

        ll_PreviousEmp_child!!.addView(line)

        ll_PreviousEmp!!.addView(ll_PreviousEmp_child)

        bt_plus.setOnClickListener(getOnClickDoSomething(et_one, et_two, et_three, et_four, bt_plus))
    }

    internal fun getOnClickDoSomething(etOne: EditText, etTwo: EditText, etThree: EditText, etFour: EditText, button: Button): View.OnClickListener {
        return View.OnClickListener {
            ll_PreviousProfile.visibility = View.GONE
            mCount = mCount + 1

            var ll_PreviousEmp_child = LinearLayout(activity)
            ll_PreviousEmp_child.orientation = LinearLayout.VERTICAL
            val mainLayout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            ll_PreviousEmp_child.layoutParams = mainLayout

            UsefullData.Log(etOne.text.toString().trim() +
                    etTwo.text.toString().trim() +
                    etThree.text.toString().trim() +
                    etFour.text.toString().trim())
            //   etFive.text.toString().trim())

//            val paramsButton = LinearLayout.LayoutParams(
//                    objUsefullData.dpToPx(25), objUsefullData.dpToPx(25))
//
//            val params = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))
//
//            val paramsEditText = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, 125)
//            paramsEditText.setMargins(16, 8, 16, 8)
//
//            val paramsEditTextFive = LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))
//            paramsEditTextFive.setMargins(16, 16, 16, 8)
//
//            paramsButton.setMargins(16, 8, 0, 0)

            val paramsButton = LinearLayout.LayoutParams(
                    objUsefullData.dpToPx(25), objUsefullData.dpToPx(25))

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))

            val paramsEditText = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, objUsefullData.dpToPx(45))
            paramsEditText.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(4), objUsefullData.dpToPx(8), objUsefullData.dpToPx(4))

            val paramsEditTextFive = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))
            paramsEditTextFive.setMargins(objUsefullData.dpToPx(4), objUsefullData.dpToPx(4), objUsefullData.dpToPx(0), objUsefullData.dpToPx(0))

            paramsButton.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(2), 0, 0)
            val paramsTextheader = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsTextheader.gravity = Gravity.CENTER
            paramsTextheader.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(1), 0, objUsefullData.dpToPx(8))

            val three = LinearLayout(activity)
            val bt_plus = Button(activity)
            bt_plus.setBackgroundResource((R.drawable.button_add))
            bt_plus.id = mCount
            bt_plus.layoutParams = paramsButton
            UsefullData.Log("Button Id" + bt_plus.id)

            val paramsText = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(25))
            paramsText.gravity = Gravity.CENTER
            paramsText.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(0), 0, 0)

            paramsText.setMargins(16, 8, 0, 0)
            val textView = TextView(activity)
            textView.layoutParams = paramsTextheader
            textView.text = "Previous Employer"
            textView.textSize = 15f

            val paramsButtonRemove = LinearLayout.LayoutParams(
                    90, 90)
            val bt_remove_view = Button(activity)
            paramsButtonRemove.setMargins(16, 8, 0, 0)
            bt_remove_view.layoutParams = paramsButtonRemove

            bt_remove_view.setBackgroundResource(R.drawable.button_remove)
            bt_remove_view.setOnClickListener {
                UsefullData.Log("mCount " + mCount)
                ll_PreviousEmp.removeViewAt(mCount - 1)
                mCount = mCount - 1
            }


            three.addView(bt_plus)
            three.addView(textView)
            three.addView(bt_remove_view)

            //  three.addView(bt_update)

            val five = LinearLayout(activity)
            five.orientation = LinearLayout.HORIZONTAL;

            val param_five = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            five.layoutParams = param_five
            val one = LinearLayout(activity)
            one.orientation = LinearLayout.VERTICAL

            val layoutOne = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            ll_PreviousEmp_child.layoutParams = mainLayout
            layoutOne.weight = 1.0f
            one.layoutParams = layoutOne

            //=====First EditText======//
            //params.weight = 5.0f
            params.setMargins(16, 8, 8, 8)

            val tv_company = TextView(activity)
            tv_company.layoutParams = paramsText
            tv_company.text = "Company"
            tv_company.textSize = 15.0f

            one.addView(tv_company)

            val mCompanyName = TextView(activity)
            mCompanyName.setText("Company Name")
            val et_one = EditText(activity)


            et_one.textSize = 15f
            et_one.background = resources.getDrawable(R.drawable.text_view_border)
            et_one.layoutParams = params
            et_one.setHint("Enter company name")
            et_one.singleLine = true
            et_one.imeOptions = EditorInfo.IME_ACTION_NEXT
            et_one.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
            one.addView(et_one)

            val tv_JoiningDate = TextView(activity)
            tv_JoiningDate.layoutParams = paramsText
            tv_JoiningDate.text = "Joining Date"
            tv_JoiningDate.textSize = 15.0f

            one.addView(tv_JoiningDate)

            val et_three = EditText(activity)


            et_three.background = resources.getDrawable(R.drawable.text_view_border)
            et_three.layoutParams = params
            et_three.textSize = 15f
            et_three.singleLine = true
            et_three.setHint("Enter date")
            et_three.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
            et_three.imeOptions = EditorInfo.IME_ACTION_NEXT
            et_three.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
            et_three.setOnClickListener {
                objUsefullData.getDatePicker(et_three)
            }
            et_three.isFocusableInTouchMode = false

            one.addView(et_three)

            val two = LinearLayout(activity)

            two.orientation = LinearLayout.VERTICAL

            two.layoutParams = layoutOne

            val tv_Designation = TextView(activity)
            tv_Designation.layoutParams = paramsText
            tv_Designation.text = "Designation"
            tv_Designation.textSize = 15.0f

            two.addView(tv_Designation)
            //=====Second EditText======//
            val mDesignationName = TextView(activity)

            val et_two = EditText(activity)
            et_two.background = resources.getDrawable(R.drawable.text_view_border)

            et_two.layoutParams = params
            et_two.textSize = 15f
            et_two.setHint("Enter designation")
            et_two.singleLine = true
            et_two.imeOptions = EditorInfo.IME_ACTION_NEXT
            et_two.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)

            //=====Third EditText======//


            two.addView(et_two)
            val tv_LeavingDate = TextView(activity)
            tv_LeavingDate.layoutParams = paramsText
            tv_LeavingDate.text = "Leaving Date"
            tv_LeavingDate.textSize = 15.0f

            two.addView(tv_LeavingDate)


            val et_four = EditText(activity)
            et_four.background = resources.getDrawable(R.drawable.text_view_border)

            et_four.textSize = 15f
            et_four.layoutParams = params
            et_four.setHint("Enter date")
            et_four.singleLine = true
            et_four.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
            et_four.imeOptions = EditorInfo.IME_ACTION_NEXT
            et_four.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
            et_four.setOnClickListener {
                objUsefullData.getDatePicker(et_four)
            }
            et_four.isFocusableInTouchMode = false

            //=====Fourth EditText======//

            five.addView(one)
            five.addView(two)
            two.addView(et_four)

            val mPreviousWorkingSector = TextView(activity)
            mPreviousWorkingSector.setText("Sector Previously Worked")
            mPreviousWorkingSector.layoutParams = paramsText
            mPreviousWorkingSector.textSize = 15.0f

            val et_five = EditText(activity)
            et_five.background = resources.getDrawable(R.drawable.text_view_border)

            et_five.textSize = 15f
            et_five.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
            et_five.singleLine = true
            et_five.setHint("Enter Sector Previously Worked")
            et_five.imeOptions = EditorInfo.IME_ACTION_NEXT
            et_five.layoutParams = paramsEditTextFive
            et_five.isFocusableInTouchMode = false
            et_five.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
            et_five.setOnClickListener { ShowAlertDialogWithListview(et_five) }


            val paramsButtonUpdate = LinearLayout.LayoutParams(
                    objUsefullData.dpToPx(200), objUsefullData.dpToPx(37))

            val bt_update = Button(activity)
            paramsButtonUpdate.gravity = Gravity.CENTER_HORIZONTAL
            paramsButtonUpdate.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8))

            bt_update.setText("Update")
            bt_update.setBackgroundResource(R.drawable.button_background)
            bt_update.setTextColor(Color.parseColor("#FFFFFF"))
            bt_update.textSize = 15f

            bt_update.layoutParams = paramsButtonUpdate
            bt_update.visibility = View.INVISIBLE
            bt_update.setOnClickListener {
                sendPreviousDataeToServer(et_one.text.toString().trim(),
                        et_two.text.toString().trim(),
                        et_three.text.toString().trim(),
                        et_four.text.toString().trim(),
                        et_five.text.toString().trim(),
                        leadId.toString()

                )
                UsefullData.Log("Previous Values" + et_one.text.toString()
                        + et_two.text.toString()
                        + et_three.text.toString()
                        + et_four.text.toString()
                        + et_five.text.toString()

                )


            }


            val bt_save = Button(activity)

            bt_save.setText("Save")
            bt_save.setBackgroundResource(R.drawable.button_background)
            bt_save.setTextColor(Color.parseColor("#FFFFFF"))
            bt_save.textSize = 15f

            bt_save.layoutParams = paramsButtonUpdate

            bt_save.setOnClickListener {
                bt_update.visibility = View.VISIBLE
                bt_save.visibility = View.INVISIBLE


                savePreviousDataeToServer(et_one.text.toString().trim(),
                        et_two.text.toString().trim(),
                        et_three.text.toString().trim(),
                        et_four.text.toString().trim(),
                        et_five.text.toString().trim()

                )

                UsefullData.Log("Previous Values" + et_one.text.toString()
                        + et_two.text.toString()
                        + et_three.text.toString()
                        + et_four.text.toString()
                        + et_five.text.toString()

                )


            }


            var line = View(activity)

            val lineParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3)
            line.layoutParams = lineParams
            lineParams.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(4), objUsefullData.dpToPx(8), objUsefullData.dpToPx(4))
            line.setBackgroundColor(Color.parseColor("#d8dfb5"))





            ll_PreviousEmp_child!!.addView(three)

            ll_PreviousEmp_child!!.addView(five)
            ll_PreviousEmp_child!!.addView(mPreviousWorkingSector)

            ll_PreviousEmp_child!!.addView(et_five)
            ll_PreviousEmp_child!!.addView(bt_save)

            ll_PreviousEmp_child!!.addView(bt_update)

            ll_PreviousEmp_child!!.addView(line)
            ll_PreviousEmp!!.addView(ll_PreviousEmp_child)

            bt_plus.setOnClickListener(getOnClickDoSomething(et_one, et_two, et_three, et_four, bt_plus))

        }
    }


    //=======================================\\

    private fun getUserProfileData() {
        objUsefullData.showProgress("Loading Professional Profile ... ", "")
        val mCall = apiEndpointInterface!!.getUserProfileData("" + mParam2)
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                try {
                if (response!!.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())




                        if (array.length() > 0) {
// $id = 1, TotalExperience = null], DOB = 01/01/1990], Education = null], UserId = 172], UName = Manager Projexel], CompanyName = Projexel], DOJ = 12/18/1992], v = null]]
                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(i)
                                if (!item.getString("Name").equals(null) && !item.getString("Name").equals("null") && !item.getString("Name").isEmpty()) {

                                    et_Name.setText(item.getString("Name"))

                                }

                                if (!item.getString("EmailId").equals(null) && !item.getString("EmailId").equals("null") && !item.getString("EmailId").isEmpty()) {

                                    et_Email.setText(item.getString("EmailId"))

                                }
                                if (!item.getString("password").equals(null) && !item.getString("password").equals("null") && !item.getString("password").isEmpty()) {

                                    et_Password.setText(item.getString("password"))

                                }
                                if (!item.getString("Phone").equals(null) && !item.getString("Phone").equals("null") && !item.getString("Phone").isEmpty()) {

                                    et_Mobile.setText(item.getString("Phone"))

                                }
                                if (!item.getString("LinkedIn").equals(null) && !item.getString("LinkedIn").equals("null") && !item.getString("LinkedIn").isEmpty()) {

                                    et_LinkedIn.setText(item.getString("LinkedIn"))

                                }
                                if (!item.getString("TotalExperience").equals(null) && !item.getString("TotalExperience").equals("null") && !item.getString("TotalExperience").isEmpty()) {

                                    et_TotalExp.setText(item.getString("TotalExperience"))

                                }
                                if (!item.getString("DOB").equals(null) && !item.getString("DOB").equals("null") && !item.getString("DOB").isEmpty()) {

                                    et_DOB.setText(item.getString("DOB"))

                                }
                                if (!item.getString("Education").equals(null) && !item.getString("Education").equals("null") && !item.getString("Education").isEmpty()) {

                                    et_Qualification.setText(item.getString("Education"))

                                }
                                if (!item.getString("UName").equals(null) && !item.getString("UName").equals("null") && !item.getString("UName").isEmpty()) {

                                    et_ImmedateManager.setText(item.getString("UName"))

                                }
                                if (!item.getString("RoleId").equals(null) && !item.getString("RoleId").equals("null") && !item.getString("RoleId").isEmpty()) {
                                                                        when (item.getString("RoleId")) {
                                        "" + (-1) -> et_Role.setText("Super Admin")
                                        "" + 0 -> et_Role.setText("Admin")
                                        "" + 1 -> et_Role.setText("Gm")
                                        "" + 2 -> et_Role.setText("Agm")
                                        "" + 3 -> et_Role.setText("Asm")
                                        "" + 4 -> et_Role.setText("Mgr")
                                        "" + 5 -> et_Role.setText("SaleMan")
                                    }

                                }

//                                et_Name.setText("" + s.Name)
//                                et_Email.setText("" + s.EmailId)
//                                et_Password.setText("" + s.password)
//                                et_Mobile.setText("" + s.Phone)
//                                if (!s.LinkedIn.isEmpty())
//                                    et_LinkedIn.setText("" + s.LinkedIn)
//                                et_TotalExp.setText("" + s.TotalExperience)
//                                et_DOB.setText("" + s.DOB)
//                                et_Qualification.setText("" + s.Education)
//                                et_ImmedateManager.setText("" + s.UName)

                                if (!item.getString("DOB").equals(null) && !item.getString("DOB").equals("null") && !item.getString("DOB").isEmpty()) {

                                    et_DOB.setText(item.getString("DOB"))

                                }
                                if (!item.getString("Education").equals(null) && !item.getString("Education").equals("null") && !item.getString("Education").isEmpty()) {

                                    et_Qualification.setText(item.getString("Education"))

                                }
                                if (!item.getString("UName").equals(null) && !item.getString("UName").equals("null") && !item.getString("UName").isEmpty()) {

                                    et_ImmedateManager.setText(item.getString("UName"))

                                }
                                if (!item.getString("CompanyName").equals(null) && !item.getString("CompanyName").equals("null") && !item.getString("CompanyName").isEmpty()) {

                                    CompanyName=(item.getString("CompanyName"))

                                }
                                if (!item.getString("DOJ").equals(null) && !item.getString("DOJ").equals("null") && !item.getString("DOJ").isEmpty()) {

                                    DOJ=(item.getString("DOJ"))

                                }
                                if (!item.getString("Designation").equals(null) && !item.getString("Designation").equals("null") && !item.getString("Designation").isEmpty()) {

                                    Designation=(item.getString("Designation"))

                                }

                            }
                        } else {


                            objUsefullData.showMsgOnUI(" No profile data found")
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

    override fun onDestroyView() {
        super.onDestroyView()

    }


    // =================== check the edit text empty  ======================\\

    private fun checkValidation(): Boolean {

        if (objValidation.checkEmpty(et_Name, "Name ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Email, "Email ")) {
            return false
        }
        if (!objValidation.checkForEmail(et_Email, "Email ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Password, "Password ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Mobile, "Mobile ")) {
            return false
        }
        if (objValidation.checkEmpty(et_LinkedIn, "Linkedin ")) {
            return false
        }
        if (objValidation.checkEmpty(et_TotalExp, "Total Experience ")) {
            return false
        }
        if (objValidation.checkEmpty(et_DOB, "Date of Birth ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Qualification, "Qualification ")) {
            return false
        }

        if (flag) {
            if (objValidation.checkEmpty(et_PreviousCompanyName, "Company Name ")) {
                return false
            }
            if (objValidation.checkEmpty(et_PreviousCompanyDesignation, "Company Designation ")) {
                return false
            }
            if (objValidation.checkEmpty(et_PreviousCompanyDoj, "Date of Joining ")) {
                return false
            }
            if (objValidation.checkEmpty(et_PreviousCompanyName, "Company Name ")) {
                return false
            }
        }


        return true
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        fun newInstance(param1: String, param2: String): Update_profile_Fragment {
            val fragment = Update_profile_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor