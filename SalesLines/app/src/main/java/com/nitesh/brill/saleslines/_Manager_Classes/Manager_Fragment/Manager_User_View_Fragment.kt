package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.Common_Fragment.Graph_Fragment
import com.nitesh.brill.saleslines.Common_Fragment.Search_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Location.Manager_SingleLeadMap_Fragment
import kotlinx.android.synthetic.main.fragment_manager__user__view.*


class Manager_User_View_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mParam3: String? = null
    private var mFragment: Fragment? = null

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
        return inflater!!.inflate(R.layout.fragment_manager__user__view, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UsefullData.Log("=======user id=====" + mParam2)
        //=========================================\\


        title.setText(arguments.getString("username") + " details ")
        btn_Graph.setOnClickListener {
            mFragment = Graph_Fragment.newInstance("", "" + mParam2)
            callFragment(mFragment)


        }


        //=========================================\\

        btn_All_Leads.setOnClickListener {
            mFragment = Search_Fragment.newInstance("1", "" + mParam2, "" + mParam3, "" + mParam2)
            callFragment(mFragment)


        }


        //=========================================\\

        btn_MissedCall.setOnClickListener {

            mFragment = Search_Fragment.newInstance("4", "" + mParam2, "" + mParam3, "" + mParam2)
            callFragment(mFragment)
        }

        //=========================================\\

        btn_FollowUp_Call.setOnClickListener {
            mFragment = Search_Fragment.newInstance("6", "" + mParam2, "" + mParam3, "" + mParam2)
            callFragment(mFragment)

        }


        //=========================================\\

        btn_Today_FollowUp.setOnClickListener {

            mFragment = Search_Fragment.newInstance("3", "" + mParam2, "" + mParam3, "" + mParam2)
            callFragment(mFragment)
        }

        //=========================================\\

        btn_Total_Sales.setOnClickListener {

            mFragment = Manager_Total_Sales_Fragment.newInstance("4", "" + mParam2)
            callFragment(mFragment)

        }

        //=========================================\\

        btn_InUser.setOnClickListener {
            mFragment = Manager_Inactive_UserFragment.newInstance("" + mParam2, "" + mParam3)
            callFragment(mFragment)

        }

        //=========================================\\
        btn_Assign_Target.setOnClickListener {
            //Manager_Assign_User_Product_Fragment.newInstance(""+gM_AsmView.ManagerId, "" + gM_AsmView.UserId,""+gM_AsmView.Name)
            mFragment = Manager_Assign_User_Product_Fragment.newInstance("" + mParam1, "" + mParam2, "" + mParam3)
            callFragment(mFragment)

        }

        val roleId = objSaveData.getString("role_id")
        Log.e("roleid",roleId)
        if(roleId.equals("4")) {
            btn_trackUser.visibility =  View.VISIBLE
            btn_trackUser.setOnClickListener {
                Log.e("Param1",mParam1+mParam2+"");

                mFragment = Manager_SingleLeadMap_Fragment.newInstance("" + mParam1, "" + mParam2)
                val bundle = Bundle()
                bundle.putString("user_id",""+arguments.getString("user_id"));
                bundle.putString("username",""+arguments.getString("username"));
                mFragment!!.arguments = bundle
                callFragment(mFragment)
            }
        }
        else
        {
            btn_trackUser.visibility =  View.GONE
        }


        /*

          var mFragment =
                val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, mFragment)
                fragmentTransaction.commit()
         */

    }

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


    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"

        fun newInstance(param1: String, param2: String, param3: String): Manager_User_View_Fragment {
            val fragment = Manager_User_View_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
