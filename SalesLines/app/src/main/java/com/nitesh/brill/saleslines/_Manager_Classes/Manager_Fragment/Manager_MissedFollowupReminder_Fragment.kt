package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.bumptech.glide.Glide
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_missed_followup_manager_reminder.*
import kotlinx.android.synthetic.main.fragment_missed_followup_reminder.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class Manager_MissedFollowupReminder_Fragment : BaseFragment() {

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
        return inflater!!.inflate(R.layout.fragment_missed_followup_manager_reminder, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            val image = bundle.getString("image", "")
            val reminders = bundle.getString("reminders", "")
            val userName = bundle.getString("userName","")
            tv_missed.text = userName + " has missed"

            tv_missed_reminders_manager.text = reminders
            Glide
                    .with(this)
                    .asGif()
                    .load(image)

                    .into(iv_missed_reminders)
            //ImageLoadTask().execute(image)

        }
    }

    inner class ImageLoadTask() : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap {
            try {
                Log.e("Url in async", params[0])
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
        fun newInstance(param1: String, param2: String): Manager_MissedFollowupReminder_Fragment {
            val fragment = Manager_MissedFollowupReminder_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor