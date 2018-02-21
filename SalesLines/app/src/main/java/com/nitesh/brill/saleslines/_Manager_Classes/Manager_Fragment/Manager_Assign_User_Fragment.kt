package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter.Manager_Assign_User_Adapter
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_User_List
import kotlinx.android.synthetic.main.fragment_manager__user_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Manager_Assign_User_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mAdapter: Manager_Assign_User_Adapter? = null

    private val dataUser: MutableList<Manager_User_List>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    init {
        // Required empty public constructor
        dataUser = ArrayList<Manager_User_List>()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_manager__user_, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         *  get the list of user inder the managr
         *
         */

        //=================================\\
        if (isNetworkConnected)
        getUserFromServer()


    }


    //=================================\\

    private fun getUserFromServer() {
        objUsefullData.showProgress(" getting users... ", "")
        dataUser.clear()

        val mCall = apiEndpointInterface!!.getUserUnderManager("" + mParam2)
        mCall.enqueue(object : Callback<List<Manager_User_List>> {
            override fun onResponse(call: Call<List<Manager_User_List>>?, response: Response<List<Manager_User_List>>?) {
                try{
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                if (response.isSuccessful){


                val size = response!!.body().size
                UsefullData.Log("" + size + "User_Response    " + response.body().toString())
                if (size > 0) {
                    for (s in response.body()) {

                        val data = Manager_User_List(s.`$id`, s.UserId, s.Name,""+mParam2)


                        dataUser.add(data)


                    }
                }

                mAdapter = Manager_Assign_User_Adapter(activity!!, dataUser)
                val mLayoutManager = GridLayoutManager(activity, 1)
                rc_RecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
                rc_RecyclerView!!.adapter = mAdapter
                mAdapter!!.notifyDataSetChanged()
                UsefullData.Log("dataAGM=========size" + mAdapter!!.getItemCount())

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }

            }

            override fun onFailure(call: Call<List<Manager_User_List>>?, t: Throwable?) {
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
        fun newInstance(param1: String, param2: String): Manager_Assign_User_Fragment {
            val fragment = Manager_Assign_User_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor