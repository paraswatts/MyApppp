package com.nitesh.brill.saleslines._User_Classes.User_Fragment


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build

import android.os.Bundle

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
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
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class User_Search_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    //http://console.salelinecrm.com/saleslineapi/GetDemoStageProduct/95/null
    private var isOpen = false
    private var popupWindow: PopupWindow? = null

    private var adapterSearch: User_Adapter_Search_Box? = null


    private val dataList: MutableList<GetAllLeadsData> = ArrayList()

    //=============================\\

    private var FromDate = "1-1-0001"
    private var ToDate = "1-1-0001"
    private var LeadStage = "SaleLine"
    private var DemoDate = "1-1-0001"
    private var Prodcut = "SaleLine"
    private var Type = "S"
    private var LeadSource = "SaleLine"
    lateinit var filterView: View


    //==========================================\\

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

        return inflater!!.inflate(R.layout.fragment_user_search, container, false)

    }

    //================================================\\

    override fun onViewCreated(rootView: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //================================================\\
        if (isNetworkConnected) {


            when (Integer.parseInt(mParam1)) {

                1 -> prepareSearchFragmentDatas()
                2 -> prepareCloseFragmentDatas()
                3 -> prepareTodayFollowupFragmentDatas()
                4 -> prepareMissedFollowupFragmentDatas()
                5 -> prepareAddedTodayFragmentDatas()
                6 -> prepareFollowupLeadsFragmentDatas()
            }

        }
//        //===================  Swipe To Refresh =============================\\
//        swipeRefreshLayout.setOnRefreshListener {
//
//
//            if (isNetworkConnected) {
//                when (Integer.parseInt(mParam1)) {
//
//                    1 -> prepareSearchFragmentDatas()
//                    2 -> prepareCloseFragmentDatas()
//                    3 -> prepareTodayFollowupFragmentDatas()
//                    4 -> prepareMissedFollowupFragmentDatas()
//                    5 -> prepareAddedTodayFragmentDatas()
//                    6 -> prepareFollowupLeadsFragmentDatas()
//                }
//            }
//
//        }
        //================================================\\


        btn_AddEnquary!!.setOnClickListener {
            val fragment = User_Add_Enquiry_Fragment.newInstance("", "")
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, fragment)
            fragmentTransaction.commit()
        }


        //================================================\\

//      val bgShape = iv_Filter.getBackground() as VectorDrawable;
//        bgShape.setColor(Color.BLACK)

//        val wrapper = android.view.ContextThemeWrapper(context, R.style.DefaultScene);
//       val drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_filter, wrapper.getTheme());
//        iv_Filter.setImageDrawable(drawable);

        iv_Filter.setOnClickListener {

            //=========================\\
            if (isNetworkConnected)
                callSearchMenu()

        }

        //================================================\\

//        searchView.setOnClickListener {
//
//
//            if (et_Search.text.toString().trim().equals("")) {
//
//                objUsefullData.showMsgOnUI("Search box should not be empty")
//
//            } else {
//                if (isNetworkConnected)
//                    callSearch()
//            }
//
//        }

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
                adapterSearch = User_Adapter_Search_Box(activity, filteredList)

                rc_RecyclerView!!.adapter = adapterSearch

                adapterSearch!!.notifyDataSetChanged()
                // UsefullData.Log(" call ho gyaa  "+s.toString())
            }

        })

    }

    private fun callSearch() {


    }
    //=============  Call Search  ===============\\

    private fun callSearchMenu() {

        // val layoutInflater = this.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        filterView = LayoutInflater.from(this.activity).inflate(R.layout.filter_menu, null)

        val et_ToDatePicker = filterView.findViewById(R.id.et_ToDatePicker) as EditText
        val et_FromDate = filterView.findViewById(R.id.et_FromDate) as EditText
        val et_DemoDatePicker = filterView.findViewById(R.id.et_DemoDatePicker) as EditText
        val et_Lead_Source = filterView.findViewById(R.id.et_Lead_Source) as EditText
        val et_Lead_Stage = filterView.findViewById(R.id.et_Lead_Stage) as EditText
        val sp_Spinner_User = filterView.findViewById(R.id.sp_Spinner_User) as EditText
        val sp_Spinner_Interested_Product = filterView.findViewById(R.id.sp_Spinner_Interested_Product) as EditText
        val sp_Spinner_HWC = filterView.findViewById(R.id.sp_Spinner_HWC) as EditText
        val btn_Search = filterView.findViewById(R.id.btn_Search) as Button
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

        //========================================\\
        sp_Spinner_User.setText(objSaveData.getString(ConstantValue.NAME))

        //  ll_button!!.setBackgroundColor(activity.resources.getColor(R.color.textColorSecondary))
        sp_Spinner_HWC.setOnClickListener {

            val Animals = activity.resources.getStringArray(R.array.hwc)
            mShowAlertDialog(sp_Spinner_HWC, Animals, "Type")

        }

        //=========================================\\
        et_Lead_Source.setOnClickListener {

            if (mArrayBindSources.size < 1) {

                objUsefullData.showMsgOnUI(activity.resources.getString(R.string.source_error))
                //objUsefullData.showMsgOnUI(activity.resources.getString(R.string.source_error))
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
                    et_ToDatePicker.setText(null)

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

            }
            btn_Search.setOnClickListener {

                isOpen = false
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
                var mCall = apiEndpointInterface!!.getFilterSearchLeads(FromDate, ToDate, LeadStage, DemoDate, Prodcut, Type, LeadSource, Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)), "" + mParam2)

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

                                adapterSearch = User_Adapter_Search_Box(activity, dataList)
                                val mLayoutManager = GridLayoutManager(activity, 1)
                                rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                                rc_RecyclerView!!.adapter = adapterSearch

                                adapterSearch!!.notifyDataSetChanged()


                        } else {
                            UsefullData.Log("========" + response.code())
                            objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                        }
                        } catch (e: Exception) {
                            UsefullData.Log("=====Exception===" + e)

                        }
                    }

                })


            }
            popupWindow!!.isFocusable = true

            popupWindow!!.isOutsideTouchable = true

            //======================================//
            Log.e("Dp to PX",""+objUsefullData.dpToPx(20))


            Log.e("Top and bottom",""+fl_search.top +"=="+fl_search.bottom+"===");


           // Log.e("Location ",""+location.left+" == "+location.top+" == "+location.right+" == "+location.bottom);


            val location = IntArray(2)
            fl_search.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            Log.e("Top and bottom dsadas",""+x +"=="+y+"===");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e("If pop","windows");
                popupWindow!!.showAsDropDown(fl_search, fl_search.getWidth() - popupWindow!!.getWidth(), 0, Gravity.RIGHT);
            } else {
                Log.e("Else pop","windows");
                popupWindow!!.showAsDropDown(fl_search, fl_search.getWidth() - popupWindow!!.getWidth(), 0);
            }

            //===========================//
            //popupWindow!!.showAtLocation(rl_RelativeLayout, Gravity.RIGHT, 0, fl_search.top-fl_search.height)
            //popupWindow!!.showAsDropDown(rl_RelativeLayout, Gravity.RIGHT, 0, fl_search.top-fl_search.height)
            isOpen = false

        } else {
            isOpen = true
            popupWindow!!.dismiss()
        }
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
        super.onDestroyView()
    }

    override fun onDestroy() {

       Log.e("On destroy","Search fragment1")
        super.onDestroy()
        Log.e("On destroy","Search fragment2")
        objSaveData.save("toDateFilter","")
        objSaveData.save("fromDateFilter","")
        objSaveData.save("demoDateFilter","")
        objSaveData.save("leadSource","")
        objSaveData.save("leadStage","")
        objSaveData.save("interestedProduct","")
        objSaveData.save("HWC","")
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
                objUsefullData.dismissProgress()

                Log.d("URL", "======" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {



                        val size = response!!.body().toString()
                        UsefullData.Log("" + size + "User_Response    " + response.body().toString())


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
                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()

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
                objUsefullData.dismissProgress()
                Log.d("URL", "======" + response!!.raw().request().url())

                try {
                if (response!!.isSuccessful) {

                        val size = response!!.body().toString()


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

                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()


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
                objUsefullData.dismissProgress()
                Log.d("URL", "======" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {


                        Log.d("URL", "======" + response!!.raw().request().url())
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

                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()

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
                objUsefullData.dismissProgress()
                Log.d("URL", "======" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {


                        Log.d("URL", "======" + response!!.raw().request().url())
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

                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()

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
               // objUsefullData.showMsgOnUI("Error :" + t)


                objUsefullData. getThrowableException(t)
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
                objUsefullData.dismissProgress()
                Log.d("URL", "======" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {


                        Log.d("URL", "======" + response!!.raw().request().url())
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

                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()


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
                objUsefullData.dismissProgress()

                Log.e("URL", "======" + response!!.raw().request().url())
                try {
                if (response!!.isSuccessful) {




                        Log.e("URL", "======" + response!!.raw().request().url())
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

                        adapterSearch = User_Adapter_Search_Box(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()


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
                UsefullData.Log("===" + t!!.message)

            }

        })


    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        //============================\\


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_Search_Fragment {
            val fragment = User_Search_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
