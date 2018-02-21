package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.bumptech.glide.Glide
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.follow_up_reminder_user.*
import kotlinx.android.synthetic.main.sales_closure_user_reminder.*
import org.jetbrains.anko.backgroundDrawable
import java.io.File

class User_FollowUpReminder_Fragment : BaseFragment() {

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
        return inflater!!.inflate(R.layout.follow_up_reminder_user, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            val extraMessage = bundle.getString("extraMessage", "")
            val message = bundle.getString("message", "")
            val lastCalled = bundle.getString("lastCalled", "")
            val image = bundle.getString("image","")
            tv_lead_name.text = extraMessage
            tv_follow_up_message.text = message
            //val leadMobile = bundle.getString("leadMobile","")
            //val leadSMS = bundle.getString("leadSMS","")
            btn_SMS_Followup.setOnClickListener {


                val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode("123")))
                sendIntent.putExtra("sms_body", "")
                //sendIntent.putExtra("address", leadMobile);

//                sendIntent.setData(Uri.parse("sms:"+leadMobile));
                startActivity(sendIntent)


            }
            //Log.e("Lead MOBILE + Leas SMS",leadMobile +"  " +leadSMS)

//            tv_missed_reminders.text = reminders
//            val imgFile = File(image)
//            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            Log.e("imagePath in fragment", imgFile.absolutePath)
//            var ob = BitmapDrawable(getResources(), myBitmap)
//            //iv_followup_reminder.setImageBitmap(myBitmap)
//            iv_followup_reminder.backgroundDrawable = ob
            Glide
                    .with(this)
                    .asGif()
                    .load(image)

                    .into(iv_followup_reminder)

        }
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_FollowUpReminder_Fragment {
            val fragment = User_FollowUpReminder_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor