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
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.Common_Fragment.Graph_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_Adapter.GM_Home_Adapter
import com.nitesh.brill.saleslines._GM_Classes.GM_Location.GM_UsersMap_Fragment
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GM_AsmView
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.GM_User_Fragment
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Location.Manager_UsersMap_Fragment
import kotlinx.android.synthetic.main.fragment_gm__home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GM_HomeFragment : BaseFragment() {


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFragment: Fragment? = null


    private var mAdapter: GM_Home_Adapter? = null
    private val dataAGM: MutableList<GM_AsmView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }


    }

    init {
        // Required empty public constructor
        dataAGM = ArrayList<GM_AsmView>()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_gm__home, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /**
         *  get the list of agm  employee  from server
         *
         */

        //=================================\\
        if (isNetworkConnected)
            getASMFromServer()

        //====================================\\

        tv_GraphView.setOnClickListener {

            mFragment = Graph_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            callFragment(mFragment)


        }
        //====================================\\

        tv_User_Search.setOnClickListener {

            mFragment = GM_User_Fragment.newInstance("" + objSaveData.getString(ConstantValue.USER_ID), "")
            callFragment(mFragment)
        }

        //====================================\\
        tv_Lead_Search.setOnClickListener {
            mFragment = GM_All_Lead_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
            callFragment(mFragment)


        }

        tv_MapView.setOnClickListener{
            val fragment = GM_UsersMap_Fragment.newInstance("", "")
            //==== Call Fragment  ====\\
            callFragment(fragment)
        }


    }

    private fun getASMFromServer() {
        objUsefullData.showProgress("Please wait ... ", "")
        dataAGM.clear()
        val mCall = apiEndpointInterface!!.getGMEmployee(objSaveData.getString(ConstantValue.USER_ID), objSaveData.getString(ConstantValue.CLIENT_ID))
        mCall.enqueue(object : Callback<List<GM_AsmView>> {
            override fun onFailure(call: Call<List<GM_AsmView>>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData.showMsgOnUI("Failed To fetch data")
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<List<GM_AsmView>>?, response: Response<List<GM_AsmView>>?) {
                objUsefullData.dismissProgress()

                try {

                    if (response!!.isSuccessful) {
                        try {
                            Log.d("URL", "===" + response!!.raw().request().url())
                            val size = response!!.body().size
                            UsefullData.Log("" + size + "User_Response    " + response.body().toString())

                            if (size > 0) {
                                for (s in response.body()) {

                                    val data = GM_AsmView(s.`$id`, s.UserId, s.RoleId, s.Name)


                                    dataAGM.add(data)


                                }
                            }



                            mAdapter = GM_Home_Adapter(activity!!, dataAGM)
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
                }catch (e:Exception){e.printStackTrace()}

            }

        })
    }

    //========================================================\\
    private fun callFragment(mFragment: Fragment?) {
        if (isNetworkConnected) {
            if (mFragment != null) {
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, mFragment)
                fragmentTransaction.commit()

            }

        }
    }


    //========================================================\\


    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): GM_HomeFragment {
            val fragment = GM_HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


//    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //do whatever you need for the hardware 'back' button
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
