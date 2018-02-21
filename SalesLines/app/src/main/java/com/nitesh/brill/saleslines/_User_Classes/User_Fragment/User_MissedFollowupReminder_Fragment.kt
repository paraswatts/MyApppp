package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment

import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_missed_followup_reminder.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.os.AsyncTask
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.follow_up_reminder_user.*
import kotlinx.android.synthetic.main.fragment_user_home.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class User_MissedFollowupReminder_Fragment : BaseFragment() {

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
        return inflater!!.inflate(R.layout.fragment_missed_followup_reminder, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            val image = bundle.getString("image", "")
            val reminders = bundle.getString("reminders", "")

            tv_missed_reminders.text = reminders
            Log.e("Gif url",image);
//            val imgFile = File(image)
//            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            Log.e("imagePath in fragment",imgFile.absolutePath)
//            var ob = BitmapDrawable(getResources(), myBitmap)
//            iv_missed_reminders.setImageBitmap(myBitmap)
            Glide
                    .with(this)
                    .asGif()
                    .load(image)

                    .into(iv_missed_reminders)            //ImageLoadTask().execute(image)

        }
    }

    inner class ImageLoadTask() : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap {
            try {
               Log.e("Url in async",params[0])
                val urlConnection = URL(params[0])
                val connection = urlConnection
                        .openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input = connection.getInputStream()
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null!!
        }


        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
            iv_missed_reminders.setImageBitmap(result)

        }

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_MissedFollowupReminder_Fragment {
            val fragment = User_MissedFollowupReminder_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
