package com.nitesh.brill.saleslines._GM_Classes.GM_Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Fragment.About_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.GM_Change_Background_Fragment

import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_Change_Notification_Sound_Fragment
import kotlinx.android.synthetic.main.fragment_user_setting.*


class GM_Setting_Fragment : BaseFragment() {
     private var mFragment: Fragment? = null

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
        val view = inflater!!.inflate(R.layout.fragment_user_setting, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_SyncNetwork.visibility=View.GONE
        //==================\\
        tv_Notification.setOnClickListener {
            mFragment = User_Change_Notification_Sound_Fragment.newInstance("", "")
            callFragment(mFragment)
        }
        //==================\\
        tv_Change.setOnClickListener {
            mFragment = GM_Change_Background_Fragment.newInstance("", "")
            callFragment(mFragment)
        }
        //==================\\
        tv_About.setOnClickListener {
            mFragment = About_Fragment.newInstance("", "")
            callFragment(mFragment)
        }

        rl_switch_location.visibility = View.GONE

    }

    private fun callFragment(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, mFragment)
            fragmentTransaction.commit()

        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): GM_Setting_Fragment {
            val fragment = GM_Setting_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
