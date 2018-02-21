package com.nitesh.brill.saleslines.Common_Fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Adapter.Graph_Adapter_Day
import com.nitesh.brill.saleslines.Common_Files.ChartData_
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_Adapter.Graph_Adapter
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.All_Product
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.All_Product_Funnel
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GetGraph
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GetGraphDay
import kotlinx.android.synthetic.main.fragment_manager_graph.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Graph_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var adapterSearch: Graph_Adapter? = null
    private var dayAdapter: Graph_Adapter_Day? = null
    var monthName = arrayOf("jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec")

    private val dataList: MutableList<GetGraph> = ArrayList()
    private val dataListDay: MutableList<GetGraphDay> = ArrayList()

    private val dataSaleValue: MutableList<BarEntry> = ArrayList()
    private val dataMonth: MutableList<String> = ArrayList()


    private val yvalues: MutableList<Entry> = ArrayList()
    private val xVals: MutableList<String> = ArrayList()

    //===========\\

    private val yvaluesFeb: MutableList<Entry> = ArrayList()
    private val xValsFeb: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesMarch: MutableList<Entry> = ArrayList()
    private val xValsMarch: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesApril: MutableList<Entry> = ArrayList()
    private val xValsFebApril: MutableList<String> = ArrayList()


    //===========\\
    private val yvaluesMay: MutableList<Entry> = ArrayList()
    private val xValsMay: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesJune: MutableList<Entry> = ArrayList()
    private val xValsJune: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesJuly: MutableList<Entry> = ArrayList()
    private val xValsJuly: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesAugust: MutableList<Entry> = ArrayList()
    private val xValsAugust: MutableList<String> = ArrayList()

    //===========\\
    private val yvalueSep: MutableList<Entry> = ArrayList()
    private val xValsSep: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesOct: MutableList<Entry> = ArrayList()
    private val xValsOct: MutableList<String> = ArrayList()
    //===========\\
    private val yvaluesNov: MutableList<Entry> = ArrayList()
    private val xValsNov: MutableList<String> = ArrayList()

    //===========\\
    private val yvaluesDec: MutableList<Entry> = ArrayList()
    private val xValsDec: MutableList<String> = ArrayList()


    val values: MutableList<ChartData_> = ArrayList()
    private val allData: MutableList<All_Product_Funnel> = ArrayList()

    private val allDataFebrurary: MutableList<All_Product_Funnel> = ArrayList()
    val valuesFebruary: MutableList<ChartData_> = ArrayList()

    val valuesMarch: MutableList<ChartData_> = ArrayList()
    private val allDataMarch: MutableList<All_Product_Funnel> = ArrayList()

    val valuesApril: MutableList<ChartData_> = ArrayList()
    private val allDataApril: MutableList<All_Product_Funnel> = ArrayList()

    val valuesMay: MutableList<ChartData_> = ArrayList()
    private val allDataMay: MutableList<All_Product_Funnel> = ArrayList()


    val valuesJune: MutableList<ChartData_> = ArrayList()
    private val allDataJune: MutableList<All_Product_Funnel> = ArrayList()

    val valuesJuly: MutableList<ChartData_> = ArrayList()
    private val allDataJuly: MutableList<All_Product_Funnel> = ArrayList()


    val valuesAugust: MutableList<ChartData_> = ArrayList()
    private val allDataAugust: MutableList<All_Product_Funnel> = ArrayList()

    val valuesSeptember: MutableList<ChartData_> = ArrayList()
    private val allDataSeptember: MutableList<All_Product_Funnel> = ArrayList()


    val valuesOctober: MutableList<ChartData_> = ArrayList()
    private val allDataOctober: MutableList<All_Product_Funnel> = ArrayList()

    val valuesNovember: MutableList<ChartData_> = ArrayList()
    private val allDataNovember: MutableList<All_Product_Funnel> = ArrayList()


    val valuesDecember: MutableList<ChartData_> = ArrayList()
    private val allDataDecember: MutableList<All_Product_Funnel> = ArrayList()

    var addFloat = ".0"
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
        return inflater!!.inflate(R.layout.fragment_manager_graph, container, false)


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //================================================\\


        if (isNetworkConnected) {

            val c = Calendar.getInstance()
            val month = c.get(Calendar.MONTH)

            Log.e("Current month",monthName[month])

            val currentMonth = monthName[month]
            if(currentMonth.equals("jan"))
            {
                getFunnelGraphJanuary()
                ll_feb.visibility = View.GONE
                ll_mar.visibility = View.GONE
                ll_apr.visibility = View.GONE
                ll_may.visibility = View.GONE
                ll_jun.visibility = View.GONE
                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE

            }
            if(currentMonth.equals("feb"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                ll_mar.visibility = View.GONE
                ll_apr.visibility = View.GONE
                ll_may.visibility = View.GONE
                ll_jun.visibility = View.GONE
                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE


            }
            if(currentMonth.equals("mar"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()

                ll_apr.visibility = View.GONE
                ll_may.visibility = View.GONE
                ll_jun.visibility = View.GONE
                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE


            }

            if(currentMonth.equals("apr"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                ll_may.visibility = View.GONE
                ll_jun.visibility = View.GONE
                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE

            }

            if(currentMonth.equals("may"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                ll_jun.visibility = View.GONE
                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE
            }

            if(currentMonth.equals("jun"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()

                ll_jul.visibility = View.GONE
                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE
            }

            if(currentMonth.equals("jul"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()

                ll_aug.visibility = View.GONE
                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE
            }


            if(currentMonth.equals("aug"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()
                getFunnelGraphAugust()

                ll_sep.visibility = View.GONE
                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE
            }


            if(currentMonth.equals("sep"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()
                getFunnelGraphAugust()
                getFunnelGraphSeptember()

                ll_oct.visibility = View.GONE
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE
            }

            if(currentMonth.equals("oct"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()
                getFunnelGraphAugust()
                getFunnelGraphSeptember()
                getFunnelGraphOctober()
                ll_nov.visibility = View.GONE
                ll_dec.visibility = View.GONE

            }

            if(currentMonth.equals("nov"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()
                getFunnelGraphAugust()
                getFunnelGraphSeptember()
                getFunnelGraphOctober()
                getFunnelGraphNovember()
                ll_dec.visibility = View.GONE

            }
            if(currentMonth.equals("nov"))
            {
                getFunnelGraphJanuary()
                getFunnelGraphFebruary()
                getFunnelGraphMarch()
                getFunnelGraphApril()
                getFunnelGraphMay()
                getFunnelGraphJune()
                getFunnelGraphJuly()
                getFunnelGraphAugust()
                getFunnelGraphSeptember()
                getFunnelGraphOctober()
                getFunnelGraphNovember()
                getFunnelGraphDecember()

            }



            getMonthGraph()


            //=================================\\

            getDayGraph()

            //=================================\\

            getTotalsalerevgraphs()


            //=================================\\

            getTotalProductsJanuary()

            //=================================\\

            getTotalProductsFebruary()

            //=================================\\

            getTotalProductsMarch()


            //=================================\\

            getTotalProductsApril()
            //=================================\\

            getTotalProductsMay()
            //=================================\\


            getTotalProductsJune()
            //=================================\\


            getTotalProductsJuly()
            //=================================\\


            getTotalProductsAugust()
            //=================================\\

            getTotalProductsSep()
            //=================================\\


            getTotalProductsOct()
            //=================================\\


            getTotalProductsNov()
            //=================================\\


            getTotalProductsDec()
        }


//        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        scroll.setFocusable(true);
//        scroll.setFocusableInTouchMode(true);
//        scroll.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                //  v!!.requestFocusFromTouch();
//
//                scroll.clearFocus();
//                return false;
//            }
//
//        })
//        scroll.smoothScrollTo(0, 0);
    }


    private fun getFunnelGraphJanuary() {

        val mCall = apiEndpointInterface!!.getTotalFunnel(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()

                objUsefullData.getThrowableException(t);
                //     objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())

                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allData.add(data)
                                    values.add(ChartData_(s.leadstages, s.leadcount.toInt()))
                                    Log.e("Month is", s.month
                                    )

                                }

                                tv_label_funnel_jan.text = "January"

                                pyramidChart.setData(values)


                            } else {
                                pyramidChart.visibility = View.GONE
                            }


                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }
        })
    }

    private fun getFunnelGraphFebruary() {

        val mCall = apiEndpointInterface!!.getTotalFunnelFebruary(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataFebrurary.add(data)
                                    valuesFebruary.add(ChartData_(s.leadstages, s.leadcount.toInt()))


                                }

                                tv_label_funnel_feb.text = "February"
                                fg_feb.setData(valuesFebruary)

                            } else {
                                fg_feb.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphMarch() {

        val mCall = apiEndpointInterface!!.getTotalFunnelMarch(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataMarch.add(data)
                                    valuesMarch.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_march.text = "March"
                                fg_march.setData(valuesMarch)


                            } else {
                                fg_march.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }

                }

            }
        })


    }

    private fun getFunnelGraphApril() {

        val mCall = apiEndpointInterface!!.getTotalFunnelApril(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataApril.add(data)
                                    valuesApril.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }
                                tv_label_funnel_april.text = "April"


                                fg_april.setData(valuesApril)

                            } else {
                                fg_april.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphMay() {

        val mCall = apiEndpointInterface!!.getTotalFunnelMay(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataMay.add(data)
                                    valuesMay.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_may.text = "May"

                                fg_may.setData(valuesMay)

                            } else {
                                fg_may.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphJune() {

        val mCall = apiEndpointInterface!!.getTotalFunnelJune(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataJuly.add(data)
                                    valuesJune.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_june.text = "June"

                                fg_june.setData(valuesJune)
                            } else {
                                fg_june.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphJuly() {

        val mCall = apiEndpointInterface!!.getTotalFunnelJuly(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataJuly.add(data)
                                    valuesJuly.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_july.text = "July"

                                fg_july.setData(valuesJuly)
                            } else {
                                fg_july.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphAugust() {

        val mCall = apiEndpointInterface!!.getTotalFunnelAugust(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataAugust.add(data)
                                    valuesAugust.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_august.text = "August"

                                fg_august.setData(valuesAugust)
                            } else {
                                fg_august.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphSeptember() {

        val mCall = apiEndpointInterface!!.getTotalFunnelSeptember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {


                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataSeptember.add(data)
                                    valuesSeptember.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_september.text = "September"

                                fg_september.setData(valuesSeptember)
                            } else {
                                fg_september.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphOctober() {

        val mCall = apiEndpointInterface!!.getTotalFunnelOctober(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataOctober.add(data)
                                    valuesOctober.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_october.text = "October"

                                fg_october.setData(valuesOctober)
                            } else {
                                fg_october.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphNovember() {

        val mCall = apiEndpointInterface!!.getTotalFunnelNovember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataNovember.add(data)
                                    valuesNovember.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_november.text = "November"

                                fg_november.setData(valuesNovember)
                            } else {
                                fg_november.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }

    private fun getFunnelGraphDecember() {

        val mCall = apiEndpointInterface!!.getTotalFunnelDecember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product_Funnel>> {
            override fun onFailure(call: Call<List<All_Product_Funnel>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product_Funnel>>?, response: Response<List<All_Product_Funnel>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                for (s in response.body()) {


                                    val data = All_Product_Funnel("", s.month, s.leadcount, s.leadstages)
                                    allDataDecember.add(data)
                                    valuesDecember.add(ChartData_(s.leadstages, s.leadcount.toInt()))

                                }

                                tv_label_funnel_december.text = "December"

                                fg_december.setData(valuesDecember)
                            } else {
                                fg_december.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }
        })


    }


    private fun getTotalProductsDec() {
        val mCall = apiEndpointInterface!!.getTotalProductsDecember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {

                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartDecemberll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesDec, "Total Product December")
                                    dataSet.sliceSpace = 0f

                                    yvaluesDec.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsDec.add(s.productsold)

                                    setPieChart(pc_PieChartDecember, dataSet, xValsDec)


                                }

                            } else {
                                pc_PieChartDecemberll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }


        })

    }

    private fun getTotalProductsNov() {


        val mCall = apiEndpointInterface!!.getTotalProductsNovember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartNovemberll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesNov, "Total Product November")
                                    dataSet.sliceSpace = 0f

                                    yvaluesNov.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsNov.add(s.productsold)

                                    setPieChart(pc_PieChartNovember, dataSet, xValsNov)


                                }

                            } else {
                                pc_PieChartNovemberll.visibility = View.GONE
                            }

                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }


        })


    }

    private fun getTotalProductsOct() {

        val mCall = apiEndpointInterface!!.getTotalProductsOctober(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartOctoberll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesOct, "Total Product October")
                                    dataSet.sliceSpace = 0f

                                    yvaluesOct.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsOct.add(s.productsold)

                                    setPieChart(pc_PieChartOctober, dataSet, xValsOct)


                                }

                            } else {
                                pc_PieChartOctoberll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }


        })
    }

    private fun getTotalProductsSep() {

        val mCall = apiEndpointInterface!!.getTotalProductsSeptember(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartSeptemberll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvalueSep, "Total Product September")
                                    dataSet.sliceSpace = 0f

                                    yvalueSep.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsSep.add(s.productsold)

                                    setPieChart(pc_PieChartSeptember, dataSet, xValsSep)


                                }

                            } else {
                                pc_PieChartSeptemberll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }

        })

    }

    private fun getTotalProductsAugust() {
        val mCall = apiEndpointInterface!!.getTotalProductsAugust(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartAugustll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesAugust, "Total Product August")
                                    dataSet.sliceSpace = 0f

                                    yvaluesAugust.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsAugust.add(s.productsold)

                                    setPieChart(pc_PieChartAugust, dataSet, xValsAugust)


                                }

                            } else {
                                pc_PieChartAugustll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }


        })

    }

    private fun getTotalProductsJuly() {


        val mCall = apiEndpointInterface!!.getTotalProductsJuly(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartJulyll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesJuly, "Total Product July")
                                    dataSet.sliceSpace = 0f

                                    yvaluesJuly.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsJuly.add(s.productsold)

                                    setPieChart(pc_PieChartJuly, dataSet, xValsJuly)


                                }

                            } else {
                                pc_PieChartJulyll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }

                }
            }


        })

    }

    private fun getTotalProductsJune() {
        val mCall = apiEndpointInterface!!.getTotalProductsJune(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartJunell.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesJune, "Total Product June")
                                    dataSet.sliceSpace = 0f

                                    yvaluesJune.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsJune.add(s.productsold)

                                    setPieChart(pc_PieChartJune, dataSet, xValsJune)


                                }

                            } else {
                                pc_PieChartJunell.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }


        })


    }

    private fun getTotalProductsMay() {

        val mCall = apiEndpointInterface!!.getTotalProductsMay(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartMAyll.visibility = View.VISIBLE
                                for (s in response.body()) {

                                    val dataSet = PieDataSet(yvaluesMay, "Total Product May")
                                    dataSet.sliceSpace = 0f

                                    yvaluesMay.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsMay.add(s.productsold)

                                    setPieChart(pc_PieChartMAy, dataSet, xValsMay)

                                }

                            } else {
                                pc_PieChartMAyll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }

                }
            }


        })

    }

    private fun getTotalProductsApril() {
        val mCall = apiEndpointInterface!!.getTotalProductsApril(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartAprilll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesApril, "Total Product April")
                                    dataSet.sliceSpace = 0f

                                    yvaluesApril.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsFebApril.add(s.productsold)

                                    setPieChart(pc_PieChartApril, dataSet, xValsFebApril)


                                }

                            } else {
                                pc_PieChartAprilll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }

                }

            }
        })


    }

    private fun getTotalProductsMarch() {
        val mCall = apiEndpointInterface!!.getTotalProductsMarch(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())

                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body())

                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartMll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesMarch, "Total Product March")
                                    dataSet.sliceSpace = 0f

                                    yvaluesMarch.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsMarch.add(s.productsold)

                                    setPieChart(pc_PieChartM, dataSet, xValsMarch)


                                }
                            } else {
                                pc_PieChartMll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }

            }


        })


    }

    private fun getTotalProductsFebruary() {


        val mCall = apiEndpointInterface!!.getTotalProductsFebruary(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {


                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartFebll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvaluesFeb, "Total Product February")
                                    dataSet.sliceSpace = 0f

                                    yvaluesFeb.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))

                                    xValsFeb.add(s.productsold)
                                    setPieChart(pc_PieChartFeb, dataSet, xValsFeb)


                                }
                            } else {
                                pc_PieChartFebll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }

        })

    }

    private fun setPieChart(pc_PieChart: PieChart?, dataSet: PieDataSet, xVal: MutableList<String>) {

        val colors = resources.getIntArray(R.array.colors)

        dataSet.setColors(colors)

        val data = PieData(xVal, dataSet)
        pc_PieChart!!.setData(data)
        pc_PieChart.setDescription("")

        pc_PieChart.setDrawHoleEnabled(true)
        pc_PieChart.setTransparentCircleRadius(40f)
        pc_PieChart.setHoleRadius(40f)
        data.setValueTextSize(15f)
        data.setValueTextColor(Color.DKGRAY)
        pc_PieChart.setDrawSliceText(false);
        pc_PieChart.setTouchEnabled(false)
//        pc_PieChart. setExtraOffsets(-50f,-10f,-10f,-10f);
        pc_PieChart.animateXY(1400, 1400)
        var l = pc_PieChart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        // l.setTypeface(...);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f);


    }

    private fun getTotalProductsJanuary() {

        val mCall = apiEndpointInterface!!.getTotalProductsJanuary(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<List<All_Product>> {
            override fun onFailure(call: Call<List<All_Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<All_Product>>?, response: Response<List<All_Product>>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())

                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body())


                            val size = response!!.body().size
                            if (size > 0) {
                                pc_PieChartll.visibility = View.VISIBLE
                                for (s in response.body()) {


                                    val dataSet = PieDataSet(yvalues, "Total Product January")
                                    dataSet.sliceSpace = 0f
                                    yvalues.add(Entry(s.productCount.toFloat(), s.`$id`.toInt()))
                                    xVals.add(s.productsold)
                                    setPieChart(pc_PieChart, dataSet, xVals)

                                }

                            } else {
                                pc_PieChartll.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }


        })


    }


    private fun getTotalsalerevgraphs() {

        val mCall = apiEndpointInterface!!.getTotalsalerevgraphs(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {


                        try {
//                        UsefullData.Log("User_Response    " + response.body().toString())
                            val array = JSONArray(response!!.body().toString())
                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(i)
                                dataSaleValue.add(BarEntry(item.getString("salesvalue")!!.toFloat(), i))
                                dataMonth.add(item.getString("Month").toString().substring(0, 3))
                                UsefullData.Log("salesvalue    " + item.getString("salesvalue")!!.toFloat())

                                val xAxis = barChart.getXAxis()
                                val rightAxis = barChart.axisRight
                                val leftAxis = barChart.axisLeft
                                leftAxis.setAxisMinValue(0f);
                                rightAxis.setAxisMinValue(0f);
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                                xAxis.textSize = 15f
                                barChart.getLegend().setEnabled(false);
                                barChart.axisLeft.setDrawGridLines(false)
                                barChart.axisRight.setDrawGridLines(false)
                                barChart.xAxis.setDrawGridLines(false)
                                val dataSets = BarDataSet(dataSaleValue, " ")
                                val colors = resources.getIntArray(R.array.mColoursBlue)
                                dataSets.setColors(colors)
                                var data = BarData(dataMonth, dataSets)
                                data.setValueFormatter(MyValueFormatter())
                                barChart.data = data
                                dataSets.setValueTextSize(12f)
                                barChart.setTouchEnabled(false);
                                barChart.setScaleEnabled(false);
                                barChart.getAxisRight().setDrawLabels(false);
                                barChart.getAxisRight().setEnabled(false);
                                barChart.getAxisLeft().setDrawGridLines(false);
                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.getAxisLeft().setDrawLabels(false);
                                barChart.getAxisLeft().setEnabled(false);
                                barChart.setDescription("")

                                scroll.fullScroll(ScrollView.FOCUS_UP)

                            }

                            barChart.isFocusableInTouchMode = true

                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }
        })

    }

    private fun getDayGraph() {

        //   objUsefullData.showProgress("please wait ... ", "")

        val mCall = apiEndpointInterface!!.getDaygraphs(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body().toString())
                            val array = JSONArray(response!!.body().toString())
                            if (array.length() > 0) {

                                dayEmptyView.visibility = View.GONE
                                rv_Horizontal_RecyclerView_Day.visibility = View.VISIBLE
                                for (i in 0..(array.length() - 1)) {

                                    val item = array.getJSONObject(i)

                                    val data = GetGraphDay(item.getString("salevalue"),
                                            item.getString("Newleads"),
                                            item.getString("FollowedUps"),
                                            item.getString("Missedfollowedups"),
                                            item.getString("Day"))
                                    dataListDay.add(data)

                                }
                                dayAdapter = Graph_Adapter_Day(activity, dataListDay)
                                rv_Horizontal_RecyclerView_Day.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
                                rv_Horizontal_RecyclerView_Day!!.adapter = dayAdapter
                                dayAdapter!!.notifyDataSetChanged()
                            } else {
                                dayEmptyView.visibility = View.VISIBLE
                                rv_Horizontal_RecyclerView_Day.visibility = View.GONE


                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }
        })


    }
    //================================================\\

    private fun getMonthGraph() {


        objUsefullData.showProgress("Please Wait... ", "")

        val mCall = apiEndpointInterface!!.getMonthgraphs(Integer.parseInt(mParam2), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.getThrowableException(t);
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (getActivity() != null && isAdded()) {
                    objUsefullData.dismissProgress()
                    Log.d("URL", "===" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        try {
                            UsefullData.Log("User_Response    " + response.body().toString())
                            val array = JSONArray(response!!.body().toString())


                            if (array.length() > 0) {

                                monthEmptyView.visibility = View.GONE
                                rv_Horizontal_RecyclerView_Month.visibility = View.VISIBLE
                                for (i in 0..(array.length() - 1)) {
                                    val item = array.getJSONObject(i)

                                    val data = GetGraph(item.getString("salevalue"),
                                            item.getString("Newleads"),
                                            item.getString("FollowedUps"),
                                            item.getString("Missedfollowedups"),
                                            item.getString("Month"))
                                    dataList.add(data)

                                }
                                adapterSearch = Graph_Adapter(activity, dataList)
                                rv_Horizontal_RecyclerView_Month.layoutManager = LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
                                rv_Horizontal_RecyclerView_Month!!.adapter = adapterSearch
                                adapterSearch!!.notifyDataSetChanged()
                            } else {

                                monthEmptyView.visibility = View.VISIBLE
                                rv_Horizontal_RecyclerView_Month.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }
            }

        })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Graph_Fragment {
            val fragment = Graph_Fragment()
            val args = Bundle()


            /**
             *
             *   param2 contain userid and passed inot param 1 and show data via param 1
             *
             */
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    inner internal class MyValueFormatter : ValueFormatter {
        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
            return Math.round(value).toString() + ""
        }
    }

}// Required empty public constructor
