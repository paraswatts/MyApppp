package com.nitesh.brill.saleslines._GM_Classes.GM_Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.nitesh.brill.saleslines.Common_Fragment.Graph_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Fragment.ASM_All_Lead_Fragment
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Fragment.ASM_User_Fragment
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Location.ASM_UsersMap_Fragment
import com.nitesh.brill.saleslines._GM_Classes.GM_Adapter.ASM_Home_Adapter
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GM_AsmView
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_Inactive_UserFragment
import kotlinx.android.synthetic.main.fragment_asm__home.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ASM_HomeFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFragment: Fragment? = null

    val dataAGM = ArrayList<GM_AsmView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    private var adapterSearch: ASM_Home_Adapter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_asm__home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        adapterSearch = ASM_Home_Adapter(activity, dataAGM)
        if (objSaveData.getString(ConstantValue.ROLE_ID).equals("1") || objSaveData.getString(ConstantValue.ROLE_ID).equals("2")) {

            tv_Inactive_User.visibility = View.VISIBLE
        } else {
            tv_Inactive_User.visibility = View.GONE
        }

        textView2.text = "ASM View"


        /**
         *  get the list of agm  employee from server
         *
         */

        //=================================\\
        if (isNetworkConnected)
            getManagerFromServer()

        //====================================\\

        tv_GraphView.setOnClickListener {
            if (isNetworkConnected) {
                mFragment = Graph_Fragment.newInstance("", "" + mParam2)
                callFragment(mFragment)
            }

        }
        //====================================\\

        tv_User_Search.setOnClickListener {
            if (isNetworkConnected) {
                mFragment = ASM_User_Fragment.newInstance("" + mParam2, "")
                callFragment(mFragment)
            }
        }

        //====================================\\
        tv_Lead_Search.setOnClickListener {
            if (isNetworkConnected) {
                mFragment = ASM_All_Lead_Fragment.newInstance("", "" + mParam2)
                callFragment(mFragment)
            }


        }

        tv_MapView.setOnClickListener {
            val fragment = ASM_UsersMap_Fragment.newInstance("", "")
            val bundle = Bundle()
            bundle.putString("user_id",""+arguments.getString("user_id"));
            mFragment!!.arguments = bundle
            //==== Call Fragment  ====\\
            callFragment(fragment)
        }
        //=======================================\\


        //======================================\\

        tv_Inactive_User.setOnClickListener {

            mFragment = Manager_Inactive_UserFragment.newInstance("" + mParam2, "")
            callFragment(mFragment)
        }



        val mLayoutManager = GridLayoutManager(activity, 1)
        rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?


    }


    private fun sendInactiveUserData() {

        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("Status", "In-Active")
        paramObject.put("UserId", mParam2)
        paramObject.put("ClientId", objSaveData.getString(ConstantValue.CLIENT_ID))

        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.mFreezeUser(objSaveData.getString(ConstantValue.CLIENT_ID), mParam2, body)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                Log.d("URL", "=====" + response!!.raw().request().url())
                try {
                    if (response!!.isSuccessful) {
                        try {
                            UsefullData.Log("=============" + response!!.body())


                            var array = JSONArray(response!!.body().toString())
                            var success = ""
                            for (i in 0..(array.length() - 1)) {
                                val item = array.getJSONObject(0)

                                UsefullData.Log("===" + item)
                                success = item.optString("Success")

                            }

                            if (success.equals("1")) {
                                objUsefullData.showMsgOnUI("Successfully In-Activated")


                            } else {
                                objUsefullData.showMsgOnUI("Failed In Activated ")
                            }
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
                UsefullData.Log("=============" + t)
                objUsefullData.showMsgOnUI("Update failed ")

            }

        })

    }

    private fun getManagerFromServer() {
        objUsefullData.showProgress("Pease wait ... ", "")
        dataAGM.clear()
        val mCall = apiEndpointInterface!!.getAGMEmployee(mParam2!!, objSaveData.getString(ConstantValue.CLIENT_ID))
        mCall.enqueue(object : Callback<List<GM_AsmView>> {
            override fun onFailure(call: Call<List<GM_AsmView>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Failed To fetch data")
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<GM_AsmView>>?, response: Response<List<GM_AsmView>>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                val size = response.body().size
                UsefullData.Log("" + size + "User_Response    " + response.body().toString())

                if (size > 0) {
                    for (s in response.body()) {

                        val data = GM_AsmView(s.`$id`, s.UserId, s.RoleId, s.Name)


                        dataAGM.add(data)
                        UsefullData.Log("dataAGM    " + dataAGM.size)
                        rc_RecyclerView!!.adapter = adapterSearch
                        adapterSearch!!.notifyDataSetChanged()


                    }
                }


            }

        })
    }

    //========================================================\\
    private fun callFragment(mFragment: Fragment?) {

        if (mFragment != null) {
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, mFragment)
            fragmentTransaction.commit()

        }


    }

    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ASM_HomeFragment {
            val fragment = ASM_HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)

            fragment.arguments = args
            return fragment
        }
    }


}
