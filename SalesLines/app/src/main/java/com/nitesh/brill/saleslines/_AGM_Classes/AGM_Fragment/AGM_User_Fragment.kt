package com.nitesh.brill.saleslines._AGM_Classes.AGM_Fragment

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
import com.nitesh.brill.saleslines._GM_Classes.GM_Adapter.GM_All_User_Adapter

import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.AllUserList

import kotlinx.android.synthetic.main.fragment_manager__user_.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AGM_User_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mAdapter: GM_All_User_Adapter? = null
    private val dataAGM: MutableList<AllUserList>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }


    init {
        // Required empty public constructor
        dataAGM = ArrayList<AllUserList>()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_manager__user_, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //=======================================\\
        if (isNetworkConnected)
            gettingListOfUser()


//        //=======================================\\
//        searchView.setOnClickListener {
//
//            if (et_Search.text.toString().trim().equals("")) {
//
//                objUsefullData.showMsgOnUI("Search box should not be empty")
//
//            } else {
//                if (isNetworkConnected)
//                callSearch()
//            }
//
//        }


    }

//    private fun callSearch() {
//        dataAGM.clear()
//        objUsefullData.showProgress("getting user ... ", "")
//        val mCall = apiEndpointInterface!!.getAllUserOfGm(et_Search.text.toString(), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)), Integer.parseInt(objSaveData.getString(ConstantValue.ROLE_ID)), Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
//        mCall.enqueue(object : Callback<JsonElement> {
//            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
//                objUsefullData.dismissProgress()
//                Log.d("URL", "===" + response!!.raw().request().url())
//                UsefullData.Log("User_Response    " + response.body().toString())
//
//                val array = JSONArray(response!!.body().toString())
//                for (i in 0..(array.length() - 1)) {
//                    val item = array.getJSONObject(i)
//                    UsefullData.Log("" + item)
//                    val data = AllUserList(item.getString("ManagerId"),
//                            item.getString("Name"),
//                            Integer.parseInt(item.getString("Status")),
//                            item.getString("Phone"),
//                            Integer.parseInt(item.getString("UserId")),
//                            item.getString("EmailId"),
//                            //                            item.getString("$id"),
//                            item.getString("RoleId"))
//
//                    dataAGM.add(data)
//
//                }
//
//                mAdapter = GM_All_User_Adapter(activity = activity!!, userList = dataAGM)
//                val mLayoutManager = GridLayoutManager(activity, 1)
//                rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
//                rc_RecyclerView!!.adapter = mAdapter
//                mAdapter!!.notifyDataSetChanged()
//                UsefullData.Log("dataAGM=========size" + mAdapter!!.getItemCount())
//
//            }
//
//            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
//                objUsefullData.dismissProgress()
//                objUsefullData.showMsgOnUI("Error :" + t)
//                UsefullData.Log("onFailure ===" + t)
//            }
//
//        })
    //    }

    private fun gettingListOfUser() {
        dataAGM.clear()
        objUsefullData.showProgress("getting user ... ", "")
        val mCall = apiEndpointInterface!!.getAllUserOfGm(mParam1.toString(), Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))
        mCall.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                    if (response.isSuccessful) {
                        try {
                            UsefullData.Log("User_Response    " + response.body().toString())

                            val array = JSONArray(response!!.body().toString())
                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(i)
                                UsefullData.Log("" + item)
                                val data = AllUserList(item.getString("Manager"),
                                        item.getString("Name"),
                                        Integer.parseInt(item.getString("Status")),
                                        item.getString("Phone"),
                                        Integer.parseInt(item.getString("UserId")),
                                        item.getString("EmailId"),
                                        //                            item.getString("$id"),
                                        item.getString("RoleId"))

                                dataAGM.add(data)

                            }

                            mAdapter = GM_All_User_Adapter(activity = activity!!, userList = dataAGM)
                            val mLayoutManager = GridLayoutManager(activity, 1)
                            rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                            rc_RecyclerView!!.adapter = mAdapter
                            mAdapter!!.notifyDataSetChanged()
                            UsefullData.Log("dataAGM=========size" + mAdapter!!.getItemCount())
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    } else {
                        UsefullData.Log("========" + response.code())
                        objUsefullData.getError("" + response.code())
                    }
                }catch (e:Exception){
                    e.printStackTrace()
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


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): AGM_User_Fragment {
            val fragment = AGM_User_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
