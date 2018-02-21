package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class User_Audio_List : BaseFragment() {

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
        return inflater!!.inflate(R.layout.audio_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toast("HIiiiiiiiiiiii Hello")
        getAudioList()
    }

    private fun getAudioList() {
        toast("HIiiiiiiiiiiii Hello 00000000000000000")

        val mCall = apiEndpointInterface!!.mGetAudioFiles(100.toString(),1.toString())

        mCall.enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                UsefullData.Log("===onFailure" + t)
                objUsefullData. getThrowableException(t)


            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                Log.e("URL", "===" + response!!.raw().request().url())
//                val size = response!!.body().size
                toast("HIiiiiiiiiiiii Hello 00000000000000000sadfsdfsdsd")

                UsefullData.Log("" + "User_Response    " + response.body().toString())

//                for (s in response.body()) {
//                    //viewCreate( s.CompanyName,s.Designation,s.DateOfJoin,s.DateOfLeaving,s.SectorPreviouslyWorked ,s.Id)
////
//                    //mPreviousId = s.Id
//
//
//                }
            }

        })

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        fun newInstance(param1: String, param2: String): User_Audio_List {
            val fragment = User_Audio_List()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor