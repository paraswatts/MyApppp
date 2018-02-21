package com.nitesh.brill.saleslines._GM_Classes.GM_Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Fragment.Search_Fragment
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_gm__all__lead_.*

class GM_All_Lead_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFragment: Fragment? = null
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
        return inflater!!.inflate(R.layout.fragment_gm__all__lead_, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        textView2.setText("GM View")
        //=============================================\\

        btn_All_Leads.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"0")
            mFragment = Search_Fragment.newInstance("1", ""+mParam2, "", "")
            callFragment(mFragment)
        }

        //=============================================\\

        btn_Leads_Today_Added.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"0")
            mFragment = Search_Fragment.newInstance("5", ""+mParam2, "", "")
            callFragment(mFragment)
        }

        //=============================================\\

        btn_Close_Leads.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"1")
            mFragment = Search_Fragment.newInstance("2", ""+mParam2, "", "")
            callFragment(mFragment)
        }

        //=============================================\\

        btn_Today_FollowUp.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"0")
            mFragment = Search_Fragment.newInstance("3", ""+mParam2, "", "")
            callFragment(mFragment)
        }

        //=============================================\\

        btn_FollowUp_Leads.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"0")
            mFragment = Search_Fragment.newInstance("6", ""+mParam2, "", "")
            callFragment(mFragment)
        }

        //=============================================\\

        btn_Missed_FollowUp.setOnClickListener {
            objSaveData.save(ConstantValue.CLOSELEAD,"0")
            mFragment = Search_Fragment.newInstance("4", ""+mParam2, "", "")
            callFragment(mFragment)
        }


    }

    private fun callFragment(mFragment: Fragment?) {

        if (isNetworkConnected){
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

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): GM_All_Lead_Fragment {
            val fragment = GM_All_Lead_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

}// Required empty public constructor