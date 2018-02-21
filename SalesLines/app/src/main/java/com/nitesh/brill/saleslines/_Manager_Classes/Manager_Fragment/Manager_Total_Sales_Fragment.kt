package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter.Manager_TotalSales_Adapter
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter.TotalSale_ListAdapter
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_TotalSalesDetails
import kotlinx.android.synthetic.main.layout_total_sales.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Manager_Total_Sales_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var adapterSearch: Manager_TotalSales_Adapter? = null
    lateinit var listAdapter: TotalSale_ListAdapter
    var groupList = ArrayList<String>()
    var childList = ArrayList<String>()
    var mAllDataList = LinkedHashMap<String, List<String>>();


    private val dataList: MutableList<Manager_TotalSalesDetails> = ArrayList()
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
        return inflater!!.inflate(R.layout.layout_total_sales, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date = SimpleDateFormat("MMM dd, hh:mm aa").format(Calendar.getInstance().getTime());

        val c = Calendar.getInstance()
        System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("dd-MM-yyyy ")
        val formattedDate = df.format(c.time)


        UsefullData.Log("============ " + formattedDate)

        c.add(Calendar.DAY_OF_YEAR, -10);

        UsefullData.Log("============ " + formattedDate + "======" + df.format(c.time))



        et_FromDate.setText(df.format(c.time).trim())

        et_ToDate.setText(formattedDate.trim())

        UsefullData.Log("============ " + date)

        //===============================\\

        getDataPreviousDate();

        //===============================\\
        et_FromDate.setOnClickListener {

            objUsefullData.getDatePicker(et_FromDate)

        }


        //===============================\\
        et_ToDate.setOnClickListener {
            objUsefullData.getDatePicker(et_ToDate)

        }


        //===============================\\
        searchView.setOnClickListener {


            if (checkValidation()) {

                if (isNetworkConnected)
                    sendDataToServer()
            }


        }


    }

    private fun getDataPreviousDate() {


        val c = Calendar.getInstance()
        System.out.println("Current time => " + c.time)
        val df = SimpleDateFormat("dd-MM-yyyy ")
        val formattedDate = df.format(c.time)


        UsefullData.Log("============ " + formattedDate)

        c.add(Calendar.DAY_OF_YEAR, -10);

        UsefullData.Log("============ " + formattedDate + "======" + df.format(c.time))

        dataList.clear()
        objUsefullData.showProgress("Fetching details... ", "")

//        val mCall = smallapiEndpointInterface!!.mTotalSales("" + mParam2, objSaveData.getString(ConstantValue.CLIENT_ID), df.format(c.time).trim(), "" + formattedDate.trim())

        val mCall = smallapiEndpointInterface!!.mTotalSales("" + mParam2, objSaveData.getString(ConstantValue.CLIENT_ID), "01-01-2018", "12-01-2018")
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "==fdsfsfsfs=" + response!!.raw().request().url())
                try{
                if (response.isSuccessful) {





                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())
                        UsefullData.Log("User_Response_array    "+array.length())

                        if (array.length() <= 0) {

                            objUsefullData.showMsgOnUI("No Lead Found")
                        } else {


                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(i)

                                //[{"$id":"1","EnquiryNum":"SL000061","leadname":"New Lead10","ClosedDate":"08/30/17 11:34:11 AM","ProductSold":"Land Rover1","Salevalue":42000.0,"sman":"LRSales5"}]
                                val data = Manager_TotalSalesDetails("", item.getString("EnquiryNum"), item.getString("leadname"), item.getString("ClosedDate"), item.getString("ProductSold"), item.getString("Salevalue"), item.getString("sman"))

                                dataList.add(data)

                            }


                            adapterSearch = Manager_TotalSales_Adapter(activity, dataList)
                            val mLayoutManager = GridLayoutManager(activity, 1)
                            rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                            rc_RecyclerView!!.adapter = adapterSearch

                            adapterSearch!!.notifyDataSetChanged()
                        }
//                    UsefullData.Log("User_Response    " + response.body().toString())
//                    val array = JSONArray(response!!.body().toString())
//
//
//                    if (array.length() <= 0) {
//
//                        objUsefullData.showMsgOnUI("No Lead Found")
//                    } else {
//
//
//                        for (i in 0..(array.length() - 1)) {
//                            val item = array.getJSONObject(i)
//
//                            //[{"$id":"1","EnquiryNum":"SL000061","leadname":"New Lead10","ClosedDate":"08/30/17 11:34:11 AM","ProductSold":"Land Rover1","Salevalue":42000.0,"sman":"LRSales5"}]
//                         val data = Manager_TotalSalesDetails("", item.getString("EnquiryNum"), item.getString("leadname"), item.getString("ClosedDate"), item.getString("ProductSold"), item.getString("Salevalue"), item.getString("sman"))
//
//                            groupList.add("LeadName :- " + item.getString("leadname"))
//                            val addingTarget = arrayOf("Enquiry No. :- " + item.getString("EnquiryNum"), "Lead Name :- " + item.getString("leadname"), "Closed Date :- " + item.getString("ClosedDate"), "Product Sold :- " + item.getString("ProductSold"),"Product Sold :- " + item.getString("ProductSold"),"Sale Value :- " + item.getString("Salevalue"))
//
//
//
////                            for (data in groupList!!) {
////                                if (data == "Lead Name :- " + item.getString("leadname")) {
////                                    childList = ArrayList()
////                                    for (model in addingTarget)
////                                        childList.add(model)
////                                }
////
////                                mAllDataList.put(data, childList)
////                            }
//                           dataList.add(data)
//
//                        }
//
//
////                        listAdapter = TotalSale_ListAdapter(activity, groupList, mAllDataList)
//                        // setting list adapter
////                        expListView!!.setAdapter(listAdapter)
////                        listAdapter.notifyDataSetInvalidated()
//
//                        adapterSearch = Manager_TotalSales_Adapter(activity, dataList)
//                        val mLayoutManager = GridLayoutManager(activity, 1)
//                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
//                        rc_RecyclerView!!.adapter = adapterSearch
//
//                        adapterSearch!!.notifyDataSetChanged()
//                    }

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

    private fun sendDataToServer() {
        dataList.clear()
        objUsefullData.showProgress("Fetching details... ", "")

        val mCall = smallapiEndpointInterface!!.mTotalSales("" + mParam2, objSaveData.getString(ConstantValue.CLIENT_ID), et_FromDate.text.toString(), "" + et_ToDate.text.toString())


        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "==fdsfsfsfs=" + response!!.raw().request().url())
                try{
                if (response.isSuccessful) {

                    UsefullData.Log("User_Response    " + response.body().toString())
                    val array = JSONArray(response!!.body().toString())


                    if (array.length() <= 0) {

                        objUsefullData.showMsgOnUI("Not Found")
                    } else {


                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            //[{"$id":"1","EnquiryNum":"SL000061","leadname":"New Lead10","ClosedDate":"08/30/17 11:34:11 AM","ProductSold":"Land Rover1","Salevalue":42000.0,"sman":"LRSales5"}]
                            val data = Manager_TotalSalesDetails("", item.getString("EnquiryNum"), item.getString("leadname"), item.getString("ClosedDate"), item.getString("ProductSold"), item.getString("Salevalue"), item.getString("sman"))


                            dataList.add(data)

                        }


                        adapterSearch = Manager_TotalSales_Adapter(activity, dataList)
                        val mLayoutManager = GridLayoutManager(activity, 1)
                        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                        rc_RecyclerView!!.adapter = adapterSearch

                        adapterSearch!!.notifyDataSetChanged()
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

    private fun checkValidation(): Boolean {


        if (objValidation.checkEmpty(et_FromDate, "FromDate ")) {

            objUsefullData.showMsgOnUI(" FromDate should not empty")

            et_FromDate.requestFocus()
            return false
        }
        if (objValidation.checkEmpty(et_ToDate, "ToDate ")) {
            objUsefullData.showMsgOnUI(" ToDate should not empty")
            et_ToDate.requestFocus()
            return false
        }
        return true
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Manager_Total_Sales_Fragment {
            val fragment = Manager_Total_Sales_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

}// Required empty public constructor