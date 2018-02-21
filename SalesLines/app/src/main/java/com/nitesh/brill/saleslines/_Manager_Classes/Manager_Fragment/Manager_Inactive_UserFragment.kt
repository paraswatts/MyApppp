package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_manager__inactive__user.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.SpannableString


class Manager_Inactive_UserFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

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
        return inflater!!.inflate(R.layout.fragment_manager__inactive__user, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val steps = "Are you sure you want to inactive salesman "


        val ss1 = SpannableString(mParam2)
        ss1.setSpan(StyleSpan(Typeface.BOLD), 0, ss1.length, 0)
        textView2.append(steps)
        textView2.append(ss1)
        textView2.append(" ?")


        btn_Yes.setOnClickListener {
            if (isNetworkConnected)
                sendInactiveUserData()
        }

        btn_No.setOnClickListener {
            activity.onBackPressed()

        }
    }


    private fun sendInactiveUserData() {

        objUsefullData.showProgress("Please Waitt...", "")
        val paramObject = JSONObject()
        paramObject.put("Status", "In-Active")
        paramObject.put("UserId", mParam2)
        paramObject.put("ClientId", objSaveData.getString(ConstantValue.CLIENT_ID))

        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.mFreezeUser(objSaveData.getString(ConstantValue.CLIENT_ID), mParam1, body)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                objUsefullData.dismissProgress()


                Log.d("URL", "=====" + response!!.raw().request().url())
                try{
                if (response.isSuccessful) {

                    UsefullData.Log("=============" + response!!.body())


                    var array = JSONArray(response!!.body().toString())
//                    for (i in 0..(array.length() - 1)) {
//                        val item = array.getJSONArray(i)
//                        for (i in 0..(item.length() - 1)) {
//                            val item_ = item.getJSONObject(0)
//
//
//                            var success = item_.optString("Success")
//
//                            if (success.equals("1")) {
//                                objUsefullData.showMsgOnUI("Successfully In-Activated")
//                            } else {
//                                objUsefullData.showMsgOnUI("Failed In-Activated")
//                            }
//
//                        }
//
//                    }

                    var success = ""
                    for (i in 0..(array.length() - 1)) {
                        val item = array.getJSONObject(0)

                        UsefullData.Log("===" + item)
                        success = item.optString("Success")

                    }

                    if (success.equals("1")) {
                        objUsefullData.showMsgOnUI("Successfully In-Activated")


                    } else {
                        objUsefullData.showMsgOnUI("In-Activated Failed ")
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

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Manager_Inactive_UserFragment {
            val fragment = Manager_Inactive_UserFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
