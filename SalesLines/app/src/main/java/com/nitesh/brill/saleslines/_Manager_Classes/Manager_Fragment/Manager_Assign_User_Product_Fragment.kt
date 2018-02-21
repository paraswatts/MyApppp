package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter.TargetAdapter
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_Total_Target
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_User_List
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Product
import kotlinx.android.synthetic.main.fragment_manager_product.*
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.selector
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Manager_Assign_User_Product_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    var mCount = 0

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mParam3: String? = null


    lateinit var listAdapter: TargetAdapter
    private val dataList: MutableList<Manager_Total_Target> = ArrayList()

    private val dataUser: MutableList<Manager_User_List> = ArrayList()
    private val productArray: MutableList<String> = ArrayList()
    var productName = ""
    private var TargetId: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
            mParam3 = arguments.getString(ARG_PARAM3)
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_manager_product, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         *  get the list of user inder the managr
         *
         */

        //=================================\\
        if (isNetworkConnected)
            getTargetFromServer()
        getTotalTargetSalesMan()


        //==========================\\

        viewCreate()


    }

    //=========================================\\


    private fun getTotalTargetSalesMan() {


        dataList.clear()
        objUsefullData.showProgress("fetching details... ", "")

        val mCall = smallapiEndpointInterface!!.mGetTotalSaleManTargets(mParam2.toString())


        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "==fdsfsfsfs=" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())


                        if (array.length() <= 0) {

                            objUsefullData.showMsgOnUI("No result found")
                            textView3.visibility = View.INVISIBLE
                            rc_RecyclerView.visibility = View.INVISIBLE


                        } else {
                            textView3.visibility = View.VISIBLE
                            rc_RecyclerView.visibility = View.VISIBLE

                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(i)

                                val data = Manager_Total_Target(""+mParam2, item.getString("TargetId"), item.getString("EmployeeName"), item.getString("Target"), item.getString("ProductName"))

                                dataList.add(data)

                            }

                            var sortedList = dataList.sortedWith(compareBy({ it.TargetId }))

                            for (obj in sortedList) {
                                UsefullData.Log("======data List===" + obj.TargetId)
                            }

                            Collections.reverse(sortedList)
                            for (obj in sortedList) {
                                UsefullData.Log("======data List reverse===" + obj.TargetId)
                            }


                            listAdapter = TargetAdapter(activity, sortedList)
                            val mLayoutManager = GridLayoutManager(activity, 1)
                            rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                            rc_RecyclerView!!.adapter = listAdapter
                            listAdapter!!.notifyDataSetChanged()


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
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }


        })


    }


    private fun viewCreate() {

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, objUsefullData.dpToPx(45))

        val paramsButton = LinearLayout.LayoutParams(
                objUsefullData.dpToPx(25), objUsefullData.dpToPx(25))

        paramsButton.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8))
        val one = LinearLayout(activity)
        //=====First EditText======//
        params.weight = 5.0f
        params.setMargins(objUsefullData.dpToPx(4), objUsefullData.dpToPx(4), objUsefullData.dpToPx(4), objUsefullData.dpToPx(4))
        val mProductName = EditText(activity)
        // mProductName.setPadding(10, 0, 0, 0)
        mProductName.setHint(" Select Product")
        mProductName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
        mProductName.setSingleLine(true);
        mProductName.setMaxLines(1);
        mProductName.setLines(1);
        mProductName.isClickable = false
        mProductName.gravity = (Gravity.LEFT or Gravity.CENTER);
        mProductName.setKeyListener(null);
        mProductName.isFocusableInTouchMode = false
        mProductName.background = resources.getDrawable(R.drawable.text_view_border)
        mProductName.textSize = 15f
        mProductName.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)

        //   mProductName.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.cursor_), null, null)
        mProductName.layoutParams = params
        var flag = false

        mProductName.setOnClickListener {
            // mProductName.isEnabled = false

            if (!flag) {
                selector("Product", productArray) { dialogInterface, i ->
                    mProductName.setText("   " + productArray.get(i).toString())
                    productName = productArray.get(i).toString()

                }
                if (!mProductName.text.toString().isEmpty())
                    flag = true
            }
            mProductName.setError(null)

        }
        one.addView(mProductName)

        //=====Second EditText======//
        val mTarget = EditText(activity)
//        mTarget.setPadding(12, 12, 12, 12)
        mTarget.setHint(" Enter Target")
        mTarget.textSize = 15f

        mTarget.maxLines = 1
        mTarget.gravity = (Gravity.LEFT or Gravity.CENTER);
        mTarget.background = resources.getDrawable(R.drawable.text_view_border)
        mTarget.layoutParams = params
        mTarget.inputType = InputType.TYPE_CLASS_NUMBER
        mTarget.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
        one.addView(mTarget)


        val bt_plus = Button(activity)
        bt_plus.setBackgroundResource((R.drawable.button_add))


        bt_plus.layoutParams = paramsButton


        val bt_update = Button(activity)
        //    bt_update.setBackgroundResource((R.drawable.ic_refresh_arrows))


        bt_update.layoutParams = paramsButton
        bt_update.visibility = View.GONE


        one.addView(bt_plus)

        one.addView(bt_update)


        ll_Add_Layout.addView(one)


        bt_update.setOnClickListener {
            //            if (checkValidation(product, target)) {//            objUsefullData.showMsgOnUI("" + TargetId.toString())
//                callUpdateSaleManTargets(mTarget.text.toString(), TargetId.toString())
//            }
        }

        bt_plus.setOnClickListener(


                getOnClickDoSomething(mProductName, mTarget, bt_plus, bt_update)
        )


    }


    //==============================\\

    internal fun getOnClickDoSomething(product: EditText, target: EditText, buttonAdd: Button, buttonUpdate: Button): View.OnClickListener {
        return View.OnClickListener {
            if (checkValidation(product, target)) {

                product.isClickable = false
                target.isClickable = false
                target.setKeyListener(null);
                target.isFocusableInTouchMode = false
                product.setKeyListener(null);
                product.isFocusableInTouchMode = false
                mCount = mCount + 1


                buttonAdd.isEnabled = false
                if (isNetworkConnected) {
                    buttonAdd.visibility = View.GONE
                    buttonUpdate.visibility = View.INVISIBLE
                    callServer(product.text.toString().trim(), target.text.toString().trim())
                }


                val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, objUsefullData.dpToPx(45))
                params.setMargins(objUsefullData.dpToPx(4), objUsefullData.dpToPx(4), objUsefullData.dpToPx(4), objUsefullData.dpToPx(4))

                UsefullData.Log(target.text.toString().trim() + "====" + product.text.toString().trim())


                val paramsButton = LinearLayout.LayoutParams(
                        objUsefullData.dpToPx(25), objUsefullData.dpToPx(25))
                paramsButton.setMargins(objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8), objUsefullData.dpToPx(8))


                //=========================\\


                val one = LinearLayout(activity)


                //=====First EditText======//
                params.weight = 5.0f

                val mProductName = EditText(activity)
                mProductName.setHint("Select Product")
                mProductName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
                mProductName.textSize = 15f
                mProductName.setSingleLine(true);
                mProductName.setMaxLines(1);
                mProductName.setLines(1);
                mProductName.isClickable = false
                mProductName.gravity = (Gravity.LEFT or Gravity.CENTER);
                mProductName.isFocusableInTouchMode = false
                mProductName.background = resources.getDrawable(R.drawable.text_view_border)
                mProductName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_spinner, 0);
                mProductName.layoutParams = params
                mProductName.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)
                mProductName.setKeyListener(null);
                mProductName.setFocusable(false)
                mProductName.setFocusableInTouchMode(false)
                mProductName.isClickable = false
                var flag2 = false
                mProductName.setOnClickListener {

                    if (!flag2) {
                        selector("Product", productArray) { dialogInterface, i ->
                            mProductName.setText("   " + productArray.get(i).toString())
                            productName = productArray.get(i).toString()

                        }
                        if (!mProductName.text.toString().isEmpty())
                            flag2 = true
                    }


                    mProductName.setError(null)
                }
                one.addView(mProductName)

                //=====Second EditText======//
                val mTarget = EditText(activity)
                mTarget.setHint(" Enter Target")
                mTarget.maxLines = 1
                mTarget.gravity = (Gravity.LEFT or Gravity.CENTER);
                mTarget.background = resources.getDrawable(R.drawable.text_view_border)
                mTarget.textSize = 15f
                mTarget.layoutParams = params
                mTarget.inputType = InputType.TYPE_CLASS_NUMBER
                mTarget.setPadding(objUsefullData.dpToPx(paddingLeft), 0, 0, 0)



                one.addView(mTarget)


                val bt_plus = Button(activity)
                bt_plus.setBackgroundResource((R.drawable.button_add))


                bt_plus.layoutParams = paramsButton


                val bt_update = Button(activity)
                bt_update.setBackgroundResource((R.drawable.ic_refresh_arrows))

                bt_update.layoutParams = paramsButton
                bt_update.visibility = View.GONE

                one.addView(bt_plus)

                bt_update.setOnClickListener {

                    if (checkValidation(mProductName, mTarget)) {

                        callUpdateSaleManTargets(mTarget.text.toString(), TargetId.toString())
                    }
                }



                one.addView(bt_update)
                ll_Add_Layout.addView(one)

                bt_plus.setOnClickListener(
                        getOnClickDoSomething(mProductName, mTarget, bt_plus, bt_update))


                //=========================\\

            }
        }
    }

    private fun checkValidation(mProductName: EditText, mTarget: EditText): Boolean {


        if (objValidation.checkEmpty(mProductName, "Product")) {

            mProductName.requestFocus()
            return false
        }

        if (objValidation.checkEmpty(mTarget, "Target ")) {

            mTarget.requestFocus()
            return false
        }
        return true
    }

    private fun callServer(product: String, target: String) {

        objUsefullData.showProgress("Please Wait...", "")

        val paramObject = JSONObject()
        paramObject.put("UserId", mParam2)
        paramObject.put("ClientId", objSaveData.getString(ConstantValue.CLIENT_ID))
        paramObject.put("ManagerId", mParam1)
        paramObject.put("EmployeeName", mParam3)
        paramObject.put("ProductName", product)
        paramObject.put("Target", target)
        paramObject.put("CreatedUserId", mParam2)

        UsefullData.Log("==================" + paramObject.toString())
        objUsefullData.dismissProgress()
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val call = apiEndpointInterface!!.mSaveSaleManTargets(body)

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: retrofit2.Call<JsonElement>?, response: retrofit2.Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {


                        UsefullData.Log("=============" + response!!.body())


                        var array = JSONArray(response!!.body().toString())
                        /*

                        val item2 = item.getJSONObject(2)

                                    UsefullData.Log("===" + item_)
                                    success = item_.optString("Success")
                                    leadId = item2.optString("ID")
                         */
                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item_ = array.getJSONObject(0)
                            val item = array.getJSONObject(2)
                            success = item_.optString("Success")
                            TargetId = item.optString("TargetId")

                        }
                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Saved")
                            //========================\\
                            getTotalTargetSalesMan()

                        } else {
                            objUsefullData.showMsgOnUI("Failed Save")
                        }


                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }}
                catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonElement>?, t: Throwable?) {

                objUsefullData.dismissProgress()
                UsefullData.Log("=============" + t)
                objUsefullData.showMsgOnUI("Update failed  " + t)

            }

        })


    }


    //==================== Update Data Product  ===================\\

    private fun callUpdateSaleManTargets(target: String, targetId: String) {

        objUsefullData.showProgress("Please Wait...", "")

        val paramObject = JSONObject()
        paramObject.put("ModifiedUserId", mParam2)

        paramObject.put("Target", target)
        paramObject.put("TargetId", targetId)

        UsefullData.Log("==================" + paramObject.toString())
        objUsefullData.dismissProgress()
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val call = apiEndpointInterface!!.mUpdateSaleManTargets(targetId, body)

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: retrofit2.Call<JsonElement>?, response: retrofit2.Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                try {

                if (response.isSuccessful) {
                        UsefullData.Log("=============" + response!!.body())


                        var array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item_ = array.getJSONObject(0)
                            var success = item_.optString("Success")

                            if (success.equals("1")) {
                                objUsefullData.showMsgOnUI("Successfully Updated")
                                getTotalTargetSalesMan()
                            } else {
                                objUsefullData.showMsgOnUI("Failed Update")
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

            override fun onFailure(call: retrofit2.Call<JsonElement>?, t: Throwable?) {

                objUsefullData.dismissProgress()
                UsefullData.Log("=============" + t)
                objUsefullData.showMsgOnUI("Update failed  " + t)

            }

        })


    }


    //=================================\\

    private fun getTargetFromServer() {
        productArray.clear()

        objUsefullData.showProgress(" getting target... ", "")
        dataUser.clear()

        val mCall = apiEndpointInterface!!.mGetSaleManTargets("" + objSaveData.getString(ConstantValue.CLIENT_ID))
        mCall.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>?, response: Response<List<Product>>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        val size = response!!.body().size
                        UsefullData.Log("" + size + "User_Response    " + response.body().toString())
                        if (size > 0) {
                            for (s in response.body()) {

                                productArray.add(s.Products)
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

            override fun onFailure(call: Call<List<Product>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)


            }

        })

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, name: String): Manager_Assign_User_Product_Fragment {
            val fragment = Manager_Assign_User_Product_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, name)

            fragment.arguments = args
            return fragment
        }
    }


}