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
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter.Leader_Boarder_Adapter
import kotlinx.android.synthetic.main.fragment_manager_leader_board.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Manager_Leader_Board_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val dataList = ArrayList<String>()
    private val countList = ArrayList<String>()
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
        return inflater!!.inflate(R.layout.fragment_manager_leader_board, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //===========================\\
        if (isNetworkConnected)
            getDatafromServer()


    }

    private fun getDatafromServer() {

        objUsefullData.showProgress("Please Wait... ", "")


        val mCall = apiEndpointInterface!!.mLeaderBoard("" + mParam1, objSaveData.getString(ConstantValue.CLIENT_ID))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())

                if (response.isSuccessful) {
try{
                    UsefullData.Log("User_Response    " + response.body().toString())

                    var array = JSONArray(response!!.body().toString())

                    if (array.length() < 1) {

                        objUsefullData.showMsgOnUI(" No data found")


                    }
                    for (i in 0..(array.length() - 1)) {
                        val item = array.getJSONObject(i)
                        UsefullData.Log("=====" + array.getJSONObject(i))
                        dataList.add(item.getString("sman"))
                        countList.add(item.getString("EID"))
                    }
                    var mAdapter = Leader_Boarder_Adapter(activity!!, dataList,countList)
                    val mLayoutManager = GridLayoutManager(activity, 1)
                    rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                    rc_RecyclerView!!.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()


} catch (e: Exception) {
    objUsefullData.getException(e)
}
                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
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


        fun newInstance(param1: String, param2: String): Manager_Leader_Board_Fragment {
            val fragment = Manager_Leader_Board_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
