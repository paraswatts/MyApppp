package com.nitesh.brill.saleslines.Common_Fragment

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
import kotlinx.android.synthetic.main.fragment_user_feedback.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Feedback_Fragment : BaseFragment() {

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
        return inflater!!.inflate(R.layout.fragment_user_feedback, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_Save.setOnClickListener {
            val str = et_Remarks.text.toString().trim { it <= ' ' }
            if (str.length == 0) {
                objUsefullData.showMsgOnUI("Remark should not be empty")
            } else {
                if (isNetworkConnected)
                    sendFeedBack()
            }
        }


    }

    private fun sendFeedBack() {


        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("UserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
        paramObject.put("ClientId", Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        paramObject.put("Remarks", et_Remarks.text.toString())
        paramObject.put("CreatedUserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))

        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val call = apiEndpointInterface!!.mSaveFeedback(body)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("=============" + response!!.body())


                        var array = JSONArray(response!!.body().toString())
//                for (i in 0..(array.length() - 1)) {
//                    val item = array.getJSONArray(i)
//
//                    for (i in 0..(item.length() - 1)) {
//                        val item_ = item.getJSONObject(i)
//
//                        UsefullData.Log("===" + item_)
//                        var success = item_.optString("Success")
//
//                        if (success.equals("1")) {
//                            objUsefullData.showMsgOnUI("Successfully Inserted")
//                        }
//
//
//                    }
//
//                }
                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }

                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Inserted")


                        } else {
                            objUsefullData.showMsgOnUI("Inserted Failed ")
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


        fun newInstance(param1: String, param2: String): Feedback_Fragment {
            val fragment = Feedback_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
