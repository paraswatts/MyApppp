package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.sales_closure_user_reminder.*
import java.io.File

class User_SaleCloserReminder_Fragment : BaseFragment() {

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
        return inflater!!.inflate(R.layout.sales_closure_user_reminder, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            val image = bundle.getString("image", "")
            val title = bundle.getString("title", "")
            val message = bundle.getString("message", "")
            val extra = bundle.getString("extra", "")
            Log.e("values",title+message+extra)

            //tv_sale_closure_title.text = title
            tv_sale_closure_message.text = message
            tv_sale_closure_extra.text = extra

//            val imgFile = File(image)
//            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            Log.e("imagePath in fragment",imgFile.absolutePath)
//            var ob = BitmapDrawable(getResources(), myBitmap)
            //iv_sale_closure_reminder.setImageDrawable(ob)

            Glide
                    .with(this)
                    .asGif()
                    .load(image)

                    .into(iv_sale_closure_reminder)

        }
    }




    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_SaleCloserReminder_Fragment {
            val fragment = User_SaleCloserReminder_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor