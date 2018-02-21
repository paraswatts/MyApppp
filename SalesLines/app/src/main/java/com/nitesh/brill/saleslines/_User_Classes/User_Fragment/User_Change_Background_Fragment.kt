package com.nitesh.brill.saleslines._User_Classes.User_Fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity
import kotlinx.android.synthetic.main.fragment_user_change_background.*

import com.nitesh.brill.saleslines.Common_Files.UsefullData
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.*
import com.nitesh.brill.saleslines.Common_Adapter.ListExampleAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import android.widget.AdapterView
import com.nitesh.brill.saleslines.R.layout.dialog
import org.jetbrains.anko.support.v4.selector


class User_Change_Background_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var list: ListView? = null
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
        val view = inflater!!.inflate(R.layout.fragment_user_change_background, container, false)

        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  tv_Font.setText("Current Font :- " + objSaveData.getString("mFontNameUser"))
        //=========================\\
        tv_FirstTheme.setOnClickListener {


            objSaveData.save(ConstantValue.UserAppTheme, "0")

            //User_Home_Activity.THEME = ConstantValue.AppTheme_Blue

            //-------------------------\\

            callHomeActivity()

        }

        //=========================\\
        tv_SecondTheme.setOnClickListener {
            //  User_Home_Activity.THEME = ConstantValue.AppTheme_Purple
            objSaveData.save(ConstantValue.UserAppTheme, "1")
            //-------------------------\\

            callHomeActivity()
        }

        //=========================\\
        tv_ThirdTheme.setOnClickListener {
            objSaveData.save(ConstantValue.UserAppTheme, "2")
            //-------------------------\\

            callHomeActivity()
        }

        //=========================\\
        tv_Font.setOnClickListener {


            var mArrayFontName: MutableList<String> = ArrayList()
            var mArrayFontPath: MutableList<String> = ArrayList()
            mArrayFontName.add("GTW")
            mArrayFontName.add("OpenSans-Regular")
            mArrayFontName.add("Oswald-Stencbab")
            mArrayFontName.add("Roboto-Bold")
            mArrayFontName.add("Roboto-thin")
            mArrayFontName.add("RobotoCondensed-Regular")

            mArrayFontPath.add("fonts/gtw.ttf")
            mArrayFontPath.add("fonts/OpenSans-Regular.ttf")
            mArrayFontPath.add("fonts/Oswald-Stencbab.ttf")
            mArrayFontPath.add("fonts/Roboto-Bold.ttf")
            mArrayFontPath.add("fonts/Roboto-ThinItalic.ttf")
            mArrayFontPath.add("fonts/RobotoCondensed-Regular.ttf")

            selector("Select Font", mArrayFontName) { dialogInterface, i ->

                objSaveData.save("mFontNameUser", mArrayFontName.get(i).toString())
                objSaveData.save("mFont", "" + i)
                objSaveData.save("mFontPathUser", "" + mArrayFontPath.get(i).toString())
                tv_Font.setText("Current Font :- " + objSaveData.getString("mFontNameUser"))

                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(objSaveData.getString("mFontPathUser"))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                )
                callHomeActivity()
            }


//              val  dialog =  Dialog(context);
//            dialog.setContentView(R.layout.spinner_layout);
////            val dialogBuilder = AlertDialog.Builder(activity)
//            dialog.setTitle("Select Font")
//          //  val inflater = this.layoutInflater
//           // val dialogView = inflater.inflate(R.layout.spinner_layout, null)
//            //dialogBuilder.setView(dialogView)
//
//             list = dialog.findViewById(R.id.list) as ListView
//
//           // dialogBuilder.setTitle("Select Font")
////            dialogBuilder.setPositiveButton("Done", DialogInterface.OnClickListener { dialog, whichButton ->
////                //do something with edt.getText().toString();
////            })
////            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
////                //pass
////            })
//
//            var mArrayFontName = ArrayList<String>()
//            var mArrayFontPath = ArrayList<String>()
//            mArrayFontName.add("GTW")
//            mArrayFontName.add("OpenSans-Regular")
//            mArrayFontName.add("Oswald-Stencbab")
//            mArrayFontName.add("Roboto-Bold")
//            mArrayFontName.add("Roboto-thin")
//            mArrayFontName.add("RobotoCondensed-Regular")
//            mArrayFontPath.add("fonts/gtw.ttf")
//            mArrayFontPath.add("fonts/OpenSans-Regular.ttf")
//            mArrayFontPath.add("fonts/Oswald-Stencbab.ttf")
//            mArrayFontPath.add("fonts/Roboto-Bold.ttf")
//            mArrayFontPath.add("fonts/Roboto-ThinItalic.ttf")
//            mArrayFontPath.add("fonts/RobotoCondensed-Regular.ttf")
//            val mAdapter = ListExampleAdapter(activity!!, mArrayFontName)
//            list!!.adapter = mAdapter
//            mAdapter!!.notifyDataSetChanged()
//
//
//
////            list.setOnItemClickListener(object : AdapterView.OnItemClickListener {
////                override fun onItemClick(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
////
////                    objSaveData.save("mFontNameUser", mArrayFontName.get(i).toString())
////                    objSaveData.save("mFont", "" + i)
////                    objSaveData.save("mFontPathUser", "" + mArrayFontPath.get(i).toString())
////                    tv_Font.setText("Current Font :- " + objSaveData.getString("mFontNameUser"))
////
////                    CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
////                            .setDefaultFontPath(objSaveData.getString("mFontPathUser"))
////                            .setFontAttrId(R.attr.fontPath)
////                            .build())
////                    b.dismiss()
////                    callHomeActivity()
////                }
////
////
////            })
//
//
//            dialog.show();

        }


//        list!!.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val listItem = list!!.getItemAtPosition(position)
//
//
//                UsefullData.Log("=========onItemClickListener=========" + listItem)
//
//                dialog.dismiss();
//            }
//        }
    }


    private fun callHomeActivity() {


        val intent = Intent(activity, User_Home_Activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        fun newInstance(param1: String, param2: String): User_Change_Background_Fragment {
            val fragment = User_Change_Background_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
