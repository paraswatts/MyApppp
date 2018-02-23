package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Fragment.Graph_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Location.Manager_UsersMap_Fragment
import kotlinx.android.synthetic.main.fragment_manager_view.*


/**
 * Created by Nitesh Android on 08-08-2017.
 */
class Manager_View_Home_Fragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFragment: Fragment? = null

    internal lateinit var packageName: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_manager_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        packageName = "com.nitesh.brill.saleslines";



        if (objSaveData.getString(ConstantValue.ROLE_ID).equals("1") || objSaveData.getString(ConstantValue.ROLE_ID).equals("2") || objSaveData.getString(ConstantValue.ROLE_ID).equals("3")) {

            tv_Inactive_User.visibility = View.VISIBLE
            tv_Assign_Target.visibility = View.GONE
        } else {
            tv_Inactive_User.visibility = View.GONE
            tv_Assign_Target.visibility = View.VISIBLE

        }


        //===============================\\

        tv_Graphs.setOnClickListener {


            mFragment = Graph_Fragment.newInstance("", "" + mParam2)
            callFragment(mFragment)


        }

        //===============================\\

        tv_Leader_Board.setOnClickListener {
            mFragment = Manager_Leader_Board_Fragment.newInstance("" + mParam2, "")
            callFragment(mFragment)


        }

        //===============================\\

        tv_Users.setOnClickListener {

            mFragment = Manager_User_Fragment.newInstance("", "" + mParam2)
            callFragment(mFragment)

        }


        //====================================\\
        tv_Lead_Search.setOnClickListener {
            mFragment = Manager_All_Lead_Fragment.newInstance("", "" + mParam2)
            callFragment(mFragment)


        }


        //===============================\\

        tv_Inactive_User.setOnClickListener {
            mFragment = Manager_Inactive_UserFragment.newInstance("" + mParam2, "")
            callFragment(mFragment)


        }


        //==============================\\

        tv_Assign_Target.setOnClickListener {

            mFragment = Manager_Assign_User_Fragment.newInstance("", "" + mParam2)
            callFragment(mFragment)

        }


        //val roleId = objSaveData.getString("role_id")
        //Log.e("roleid",roleId)
        //if(roleId.equals("4")) {
            tv_MapView.visibility = View.VISIBLE;
            tv_MapView.setOnClickListener {
                val fragment = Manager_UsersMap_Fragment.newInstance("", "")
                val bundle = Bundle()
                bundle.putString("user_id",""+arguments.getString("user_id"));
                mFragment!!.arguments = bundle
                //==== Call Fragment  ====\\
                callFragment(fragment)
            }
//            }
//        }
//        else{
//            tv_MapView.visibility = View.GONE
//        }

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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        fun newInstance(param1: String, param2: String): Manager_View_Home_Fragment {
            val fragment = Manager_View_Home_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}