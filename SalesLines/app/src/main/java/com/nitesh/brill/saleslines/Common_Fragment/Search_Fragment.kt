package com.nitesh.brill.saleslines.Common_Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Adapter.Search_Adapter
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Adapter.User_Adapter_Search_Box
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.GetAllLeadsData
import kotlinx.android.synthetic.main.fragment_user_search.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var SalesmanName: String? = null
    private var Salesmanid: String? = null

    private var isOpen = false
    private var popupWindow: PopupWindow? = null

    private var adapterSearch: Search_Adapter? = null


    private var FromDate = "1-1-0001"
    private var ToDate = "1-1-0001"
    private var LeadStage = "SaleLine"
    private var DemoDate = "1-1-0001"
    private var Prodcut = "SaleLine"
    private var Type = "S"
    private var LeadSource = "SaleLine"
    lateinit var filterView: View

    private val dataList: MutableList<GetAllLeadsData> = ArrayList()
    private val nameList: MutableList<String> = ArrayList()

    private val idList: MutableList<String> = ArrayList()

    var flag = false
    //=============================\\


    //==========================================\\

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
            SalesmanName = arguments.getString(ARG_PARAM3)
            Salesmanid = arguments.getString(ARG_PARAM4)
        }
    }

    //=============================================\\

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_user_search, container, false)

    }

    //================================================\\

    override fun onViewCreated(rootView: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        if (SalesmanName!!.trim().equals("") && Salesmanid!!.trim().equals("")) {
            Salesmanid = "0"
            getOtherUserFromServer()

        } else {
            flag = true
        }
//        //===================  Swipe To Refresh =============================\\
//        swipeRefreshLayout.setOnRefreshListener {
//
//
//            if (isNetworkConnected) {
//
//                try {
//
//                    when (Integer.parseInt(mParam1)) {
//
//                        1 -> prepareSearchFragmentDatas()
//                        2 -> prepareCloseFragmentDatas()
//                        3 -> prepareTodayFollowupFragmentDatas()
//                        4 -> prepareMissedFollowupFragmentDatas()
//                        5 -> prepareAddedTodayFragmentDatas()
//                        6 -> prepareFollowupLeadsFragmentDatas()
//                    }
//                } catch (e: Exception) {
//
//                    e.printStackTrace()
//                }
//            }
//
//        }


        //================================================\\
        if (isNetworkConnected) {

            UsefullData.Log(mParam1+"======mParam2=====" + mParam2);
            try {

                when (Integer.parseInt(mParam1)) {

                    1 -> prepareSearchFragmentDatas()
                    2 -> prepareCloseFragmentDatas()
                    3 -> prepareTodayFollowupFragmentDatas()
                    4 -> prepareMissedFollowupFragmentDatas()
                    5 -> prepareAddedTodayFragmentDatas()
                    6 -> prepareFollowupLeadsFragmentDatas()
                }
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
        //================================================\\
        btn_AddEnquary.visibility = View.GONE


        //================================================\\

        iv_Filter.setOnClickListener {

            //=========================\\
            if (isNetworkConnected)
                callSearchMenu()

        }





        et_Search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = java.util.ArrayList<GetAllLeadsData>()

                for (i in 0..(dataList.size - 1)) {

                    val name = dataList[i].leadname.toLowerCase() +dataList[i].Phone
                    if (name.contains(s.toString().toLowerCase())) {
                        filteredList.add(dataList.get(i))

                    }
                }

                val mLayoutManager = GridLayoutManager(activity, 1)
                rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                adapterSearch = Search_Adapter(activity, filteredList)

                rc_RecyclerView!!.adapter = adapterSearch

                adapterSearch!!.notifyDataSetChanged()
                // UsefullData.Log(" call ho gyaa  "+s.toString())
            }

        })


    }

    private fun getOtherUserFromServer() {
        nameList.clear()
        idList.clear()
        val mCall = apiEndpointInterface!!.mGettingSalemanUnderUserRole(objSaveData.getString(ConstantValue.USER_ID), (objSaveData.getString(ConstantValue.ROLE_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "===" + response!!.raw().request().url())


                if (response.isSuccessful) {
                    try {
                        UsefullData.Log("User_Response    " + response.body().toString())

                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            UsefullData.Log("" + item)
                            nameList.add(item.getString("Name"))
                            idList.add(item.getString("userid"))
                            UsefullData.Log("======Other=====" + item.getString("Name"));
                        }
                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }
                    objUsefullData.dismissProgress()


                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

        })

    }

    private fun callSearch() {

        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.mUpdateEnqStatusClose(et_Search.text.toString(), "" + mParam2, objSaveData.getString(ConstantValue.CLIENT_ID))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "======" + response!!.raw().request().url())

                if (response.isSuccessful) {

                    try {
                        val array = JSONArray(response!!.body().toString())
                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No data found")

                        }
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("leadname"), Phone = item.getString("Phone"), rating = item.getString("rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    ,

                                    //==============send GM id in Managerid==============\\
                                    ManagerId = item.getString("gm"),

                                    UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()

                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })


    }
    //=============  Call Search  ===============\\

    private fun callSearchMenu() {


        filterView = LayoutInflater.from(this.activity).inflate(R.layout.filter_menu, null)

        val et_ToDatePicker = filterView.findViewById(R.id.et_ToDatePicker) as EditText
        val et_FromDate = filterView.findViewById(R.id.et_FromDate) as EditText
        val et_DemoDatePicker = filterView.findViewById(R.id.et_DemoDatePicker) as EditText
        val et_Lead_Source = filterView.findViewById(R.id.et_Lead_Source) as EditText
        val et_Lead_Stage = filterView.findViewById(R.id.et_Lead_Stage) as EditText
        val sp_Spinner_Interested_Product = filterView.findViewById(R.id.sp_Spinner_Interested_Product) as EditText
        val sp_Spinner_HWC = filterView.findViewById(R.id.sp_Spinner_HWC) as EditText
        val btn_Search = filterView.findViewById(R.id.btn_Search) as Button
        val sp_Spinner_User = filterView.findViewById(R.id.sp_Spinner_User) as EditText
        val btn_Clear = filterView.findViewById(R.id.btn_reset) as Button


        if(!TextUtils.isEmpty(objSaveData.getString("toDateFilter"))) {
            et_ToDatePicker.setText(objSaveData.getString("toDateFilter"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("fromDateFilter"))) {
            et_FromDate.setText(objSaveData.getString("fromDateFilter"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("demoDateFilter"))) {
            et_DemoDatePicker.setText(objSaveData.getString("demoDateFilter"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("leadSource"))) {
            et_Lead_Source.setText(objSaveData.getString("leadSource"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("leadStage"))) {
            et_Lead_Stage.setText(objSaveData.getString("leadStage"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("interestedProduct"))) {
            sp_Spinner_Interested_Product.setText(objSaveData.getString("interestedProduct"))
        }
        if(!TextUtils.isEmpty(objSaveData.getString("HWC"))) {
            sp_Spinner_HWC.setText(objSaveData.getString("HWC"))
        }

        if(!TextUtils.isEmpty(objSaveData.getString("salesman"))) {
            sp_Spinner_User.setText(objSaveData.getString("salesman"))
        }




        UsefullData.Log(SalesmanName + "===== callSearchMenu =====" + Salesmanid)
        //======================================\\


        if (SalesmanName!!.trim().equals("") && Salesmanid!!.trim().equals("") || Salesmanid!!.trim().equals("0")) {

            sp_Spinner_User.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
            sp_Spinner_User.setText("Select User")

            sp_Spinner_User.setTextColor(activity.resources.getColor(R.color.black))
        }
        else
        {
            sp_Spinner_User.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
            sp_Spinner_User.setText("" + SalesmanName)
        }

//        } else {
//            sp_Spinner_User.setText("" + SalesmanName)
//            sp_Spinner_User.setTextColor(activity.resources.getColor(R.color.black))
//
//            if (flag) {
//
//                sp_Spinner_User.isEnabled = false
//
//            }
//        }



        sp_Spinner_User.setOnClickListener {


            mShowDialog(sp_Spinner_User, nameList as ArrayList<String>, "Assign Employee")


        }
        //========================================\\


        sp_Spinner_HWC.setOnClickListener {

            val Animals = activity.resources.getStringArray(R.array.hwc)
            mShowAlertDialog(sp_Spinner_HWC, Animals, "Type")


        }


        //=========================================\\
        et_Lead_Source.setOnClickListener {

            if (mArrayBindSources.size < 1) {

                objUsefullData.showMsgOnUI(activity.resources.getString(R.string.source_error))
            } else {
                mShowAlertDialogWithListview(et_Lead_Source, mArrayBindSources as ArrayList<String>, "Lead Source")
            }


        }


        //=========================================\\
        et_Lead_Stage.setOnClickListener {

            if (mArrayBindStages.size < 1) {

                objUsefullData.showMsgOnUI(activity.resources.getString(R.string.stage_error))
            } else {
                mShowAlertDialogWithListview_(et_Lead_Stage, mArrayBindStages as ArrayList<String>, "Lead Stage")
            }

        }
        //=========================================\\
        sp_Spinner_Interested_Product.setOnClickListener {

            if (mArrayBindProduct.size < 1) {

                objUsefullData.showMsgOnUI(activity.resources.getString(R.string.product_error))
            } else {
                mShowAlertDialogWithListviewInterestedProduct(sp_Spinner_Interested_Product, mArrayBindProduct as ArrayList<String>, "Product")
            }

        }

        popupWindow = PopupWindow(filterView, objUsefullData.dpToPx(250), ViewGroup.LayoutParams.WRAP_CONTENT)

        if (!isOpen!!) {

            //========Hide keyboard on popup window open===========//
            try{
                val view = activity.getCurrentFocus()
                if (view != null) {
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                }
            }catch (e:Exception){e.printStackTrace()}
            //================================================\\

            et_FromDate!!.setOnClickListener {


                objUsefullData.getDatePickerFrom(et_FromDate)


            }

            et_DemoDatePicker!!.setOnClickListener {

                objUsefullData.getDatePickerDemo(et_DemoDatePicker)


            }

            //================================================\\


            et_ToDatePicker!!.setOnClickListener {

                objUsefullData.getDatePickerTo(et_ToDatePicker)
            }

            btn_Clear.setOnClickListener{
                objSaveData.save("toDateFilter","")
                objSaveData.save("fromDateFilter","")
                objSaveData.save("demoDateFilter","")
                objSaveData.save("leadSource","")
                objSaveData.save("leadStage","")
                objSaveData.save("interestedProduct","")
                objSaveData.save("HWC","")
                objSaveData.save("salesman","")
                et_ToDatePicker.setText(null)
                sp_Spinner_User.setText(null)

                et_FromDate.setText(null)

                et_DemoDatePicker.setText(null)

                et_Lead_Source.setText(null)

                et_Lead_Stage.setText(null)

                sp_Spinner_Interested_Product.setText(null)

                sp_Spinner_HWC.setText(null)


                FromDate = "1-1-0001"
                ToDate = "1-1-0001"
                LeadStage = "SaleLine"
                DemoDate = "1-1-0001"
                Prodcut = "SaleLine"
                Type = "S"
                LeadSource = "SaleLine"
                SalesmanName = ""
                Salesmanid = "0"

            }

            btn_Search.setOnClickListener {
                popupWindow!!.dismiss()
                if (!et_FromDate.text.toString().trim().equals("")) {
                    FromDate = et_FromDate.text.toString()
                }
                if (!et_DemoDatePicker.text.toString().trim().equals("")) {
                    DemoDate = et_DemoDatePicker.text.toString()
                }
                if (!et_ToDatePicker.text.toString().trim().equals("")) {
                    ToDate = et_ToDatePicker.text.toString()
                }

                objUsefullData.showProgress("Please Wait...", "")
                dataList.clear()
                var mCall = apiEndpointInterface!!.getFilterSearchLeads(FromDate, ToDate, LeadStage, DemoDate, Prodcut, Type, LeadSource, Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)), "" + Salesmanid)

                mCall.enqueue(object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                        objUsefullData.dismissProgress()
                        objUsefullData.showMsgOnUI("Error :" + t)
                        UsefullData.Log("onFailure ===" + t)
                    }

                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        Log.d("URL", "===" + response!!.raw().request().url())

                        if (response.isSuccessful) {

                            try {
                                UsefullData.Log("User_Response    " + response.body().toString())

                                val array = JSONArray(response!!.body().toString())

                                if (array.length() < 1) {

                                    objUsefullData.showMsgOnUI(" No data found")

                                }

                                for (i in 0..(array.length() - 1)) {
                                    val item = array.getJSONObject(i)
                                    val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                            , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                                    dataList.add(data)
                                }

                                adapterSearch = Search_Adapter(activity, dataList)
                                val mLayoutManager = GridLayoutManager(activity, 1)
                                rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                                rc_RecyclerView!!.adapter = adapterSearch



                            adapterSearch!!.notifyDataSetChanged()
                            isOpen = false
                            popupWindow!!.dismiss()
                            } catch (e: Exception) {
                                objUsefullData.getException(e)
                            }
                            objUsefullData.dismissProgress()

                        } else {
                            UsefullData.Log("========" + response.code())
                            objUsefullData.getError("" + response.code())
                        }

                    }

                })


            }




            popupWindow!!.isFocusable = true

            popupWindow!!.isOutsideTouchable = true
            //======================================//











            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("If pop","windows");
                popupWindow!!.showAsDropDown(fl_search, fl_search.getWidth() - popupWindow!!.getWidth(), 0, Gravity.RIGHT);
            } else {
                Log.e("Else pop","windows");
                popupWindow!!.showAsDropDown(fl_search, fl_search.getWidth() - popupWindow!!.getWidth(), 0);
            }

            //===========================//
            //popupWindow!!.showAtLocation(rl_RelativeLayout, Gravity.RIGHT, 0, objUsefullData.dpToPx(20))
            isOpen = false
        } else {

            isOpen = true
            popupWindow!!.dismiss()
        }
    }

    fun locateView(v: View?): Rect? {
        val loc_int = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(loc_int)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }

        val location = Rect()
        location.left = loc_int[0]
        location.top = loc_int[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    override fun onDestroyView() {
        Log.e("On destroy","Search fragment1")
        Log.e("On destroy","Search fragment2")
        objSaveData.save("toDateFilter","")
        objSaveData.save("fromDateFilter","")
        objSaveData.save("demoDateFilter","")
        objSaveData.save("leadSource","")
        objSaveData.save("leadStage","")
        objSaveData.save("interestedProduct","")
        objSaveData.save("HWC","")
        objSaveData.save("salesman","")
        super.onDestroyView()
    }

    private fun mShowDialog(mEditText: EditText?, salesmanArray: ArrayList<String>, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)
        val cs = salesmanArray.toArray(arrayOfNulls<CharSequence>(salesmanArray.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->


                objSaveData.save("salesman", salesmanArray!![item].toString().trim())

                mEditText!!.setText(salesmanArray!![item].toString().trim())
                SalesmanName = salesmanArray!![item].toString().trim()
                Salesmanid = idList[item].trim()

        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()

    }

    private fun mShowAlertDialogWithListview_(et_Lead_Stage: EditText, leadStageArray: ArrayList<String>, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)

        val cs = leadStageArray.toArray(arrayOfNulls<CharSequence>(leadStageArray.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->


                objSaveData.save("leadStage", leadStageArray!![item].toString().trim())

                et_Lead_Stage!!.setText(leadStageArray!![item].toString().trim())
                LeadStage = leadStageArray!![item].toString().trim()



        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }

    private fun mShowAlertDialogWithListviewInterestedProduct(sp_Spinner_Interested_Product: EditText, interestedProductArray: ArrayList<String>, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)

        val cs = interestedProductArray.toArray(arrayOfNulls<CharSequence>(interestedProductArray.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->


                objSaveData.save("interestedProduct", interestedProductArray!![item].toString().trim())

                sp_Spinner_Interested_Product!!.setText(interestedProductArray!![item].toString().trim())
                Prodcut = interestedProductArray!![item].toString().trim()


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }

    private fun mShowAlertDialogWithListview(et_Lead_Source: EditText, leadSourceArray: ArrayList<String>, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)

        val cs = leadSourceArray.toArray(arrayOfNulls<CharSequence>(leadSourceArray.size))
        dialogBuilder.setItems(cs, DialogInterface.OnClickListener { dialog, item ->


                objSaveData.save("leadSource", leadSourceArray!![item].toString().trim())

                et_Lead_Source!!.setText(leadSourceArray!![item].toString().trim())
                LeadSource = leadSourceArray!![item].toString().trim()


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }

    private fun mShowAlertDialog(sp_Spinner_HWC: EditText, HWCArray: Array<out String>?, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)


        dialogBuilder.setItems(HWCArray, DialogInterface.OnClickListener { dialog, item ->
                objSaveData.save("HWC", HWCArray!![item].toString().trim())


                sp_Spinner_HWC!!.setText(HWCArray!![item].toString().trim())
//                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
//                sp_Spinner_HWC.setTypeface(boldTypeface)
                Type = HWCArray!![item].toString().trim()


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()

    }


    private fun prepareAddedTodayFragmentDatas() {


        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getCommittedProspectLeads(mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())

                if (response.isSuccessful) {

                    try {
                        val array = JSONArray(response!!.body().toString())

                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }

                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()

                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }

            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })


    }
    //================================================\\

    private fun prepareFollowupLeadsFragmentDatas() {

        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getFollowedUpLeads(mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())

                if (response.isSuccessful) {

                    try {
                        val size = response!!.body().toString()
                        //  UsefullData.Log("" + size + "User_Response    " + response.body().toString())


                        val array = JSONArray(response!!.body().toString())

                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }

                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()


                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }


                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })
    }

    //================================================\\
    private fun prepareMissedFollowupFragmentDatas() {


        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getLeadsMissedFollowups(mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())

                if (response.isSuccessful) {
                    try {
                        val size = response!!.body().toString()
                        //  UsefullData.Log("" + size + "User_Response    " + response.body().toString())


                        val array = JSONArray(response!!.body().toString())
                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()

                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })
    }

    //================================================\\
    private fun prepareTodayFollowupFragmentDatas() {

        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getFollowupsToday(mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())
                if (response.isSuccessful) {

                    try {

                        val size = response!!.body().toString()
                        //   UsefullData.Log("" + size + "User_Response    " + response.body().toString())


                        val array = JSONArray(response!!.body().toString())
                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()

                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })


    }

    //================================================\\
    private fun prepareCloseFragmentDatas() {

        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getLeadsClosed("Closed", mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())

                if (response.isSuccessful) {

                    try {
                        val size = response!!.body().toString()
                        // UsefullData.Log("" + size + "User_Response    " + response.body().toString())


                        val array = JSONArray(response!!.body().toString())
                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()

                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }


                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })
    }

    //================================================\\

    private fun prepareSearchFragmentDatas() {

        objUsefullData.showProgress("Getting Leads... ", "")
        dataList.clear()
        val mCall = smallapiEndpointInterface!!.getAllLeadsOfEmployee(mParam2, Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.d("URL", "======" + response!!.raw().request().url())
                if (response.isSuccessful) {
                    try {

                        val size = response!!.body().toString()
                        // UsefullData.Log("" + size + "User_Response    " + response.body().toString())


                        val array = JSONArray(response!!.body().toString())
                        if (array.length() < 1) {

                            objUsefullData.showMsgOnUI(" No Lead found")

                        }
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)
                            val data = GetAllLeadsData(Data_PC = item.getString("Data_PC"), type = item.getString("type"), LeadId = item.getString("LeadId"), leadname = item.getString("LeadName"), Phone = item.getString("Phone"), rating = item.getString("Rating"), EnquiryDate = item.getString("EnquiryDate"), EnquiryId = item.getString("Eid"), DemoId = item.getString("DemoId")
                                    , ManagerId = item.getString("Mgr"), UserId = item.getString("saleman"))
                            dataList.add(data)
                        }

                        adapterSearch = Search_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
                        objUsefullData.dismissProgress()


                    } catch (e: Exception) {
                        objUsefullData.showMsgOnUI("" + e)

                    }

                } else {

                    UsefullData.Log("========" + response.code())
                    objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                }


            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("===" + t!!.message)

            }

        })


    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"
        private val ARG_PARAM4 = "param4"

        //============================\\


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, mSalesmanName: String, mSalesmanid: String): Search_Fragment {
            val fragment = Search_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, mSalesmanName)
            args.putString(ARG_PARAM4, mSalesmanid)
            fragment.arguments = args
            return fragment
        }
    }
}