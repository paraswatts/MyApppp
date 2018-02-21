package com.nitesh.brill.saleslines.Common_Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.hsalf.smilerating.SmileRating
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_user_rating.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject


class Rate_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    var Rate = ""

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
        return inflater!!.inflate(R.layout.fragment_user_rating, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        button2.setOnClickListener {
            if (!Rate.isEmpty() && !Rate.equals("")) {
                //=======================\\
                if (isNetworkConnected) {
                    sendDataToServer()
                }
            } else {
                objUsefullData.showMsgOnUI("Please give a rating")
            }
        }

        smile_rating.setOnSmileySelectionListener(SmileRating.OnSmileySelectionListener { smiley, reselected ->

            when (smiley) {
                SmileRating.BAD -> Rate = ("Bad")
                SmileRating.GOOD -> Rate = ("Good")
                SmileRating.GREAT -> Rate = ("Great")
                SmileRating.OKAY -> Rate = ("Okay")
                SmileRating.TERRIBLE -> Rate = ("Terrible")
            }
        })


    }

    private fun sendDataToServer() {


        objUsefullData.showProgress("Please Wait...", "")

        val paramObject = JSONObject()
        paramObject.put("UserId", objSaveData.getString(ConstantValue.USER_ID))
        paramObject.put("ClientId", objSaveData.getString(ConstantValue.USER_ID))
        paramObject.put("Rate", Rate)
        paramObject.put("Comments", et_Comment.text.toString())
        paramObject.put("CreatedUserId", objSaveData.getString(ConstantValue.USER_ID))

        UsefullData.Log("==================" + paramObject.toString())
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val call = apiEndpointInterface!!.mSaveRateApp(body)

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: retrofit2.Call<JsonElement>?, response: retrofit2.Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                if (response.isSuccessful) {
try{
                    UsefullData.Log("=============" + response!!.body())


                    var array = JSONArray(response .body().toString())
                    for (i in 0..(array.length() - 1)) {
                        val item_ = array.getJSONObject(0)
                        var success = item_.optString("Success")

                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Updated")
                        } else {
                            objUsefullData.showMsgOnUI("Failed Update")
                        }

                    }
} catch (e: Exception) {
    objUsefullData.getException(e)
}
                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonElement>?, t: Throwable?) {

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
        fun newInstance(param1: String, param2: String): Rate_Fragment {
            val fragment = Rate_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}
