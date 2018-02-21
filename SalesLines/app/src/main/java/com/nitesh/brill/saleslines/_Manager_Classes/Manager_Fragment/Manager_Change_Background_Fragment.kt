package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity
import kotlinx.android.synthetic.main.fragment_user_change_background.*
import org.jetbrains.anko.support.v4.selector
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


@Suppress("UNREACHABLE_CODE")
class Manager_Change_Background_Fragment : BaseFragment() {

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
        val view = inflater!!.inflate(R.layout.fragment_user_change_background, container, false)

        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // tv_Font.setText("Current Font :- " + objSaveData.getString("mFontNameManager"))
        //=========================\\
        tv_FirstTheme.setOnClickListener {

            objSaveData.save(ConstantValue.ManagerAppTheme,"0")
          //  Manager_Home_Activity.THEME = ConstantValue.AppTheme_Blue

            //-------------------------\\

            callHomeActivity()

        }

        //=========================\\
        tv_SecondTheme.setOnClickListener {
            objSaveData.save(ConstantValue.ManagerAppTheme,"1")
//            Manager_Home_Activity.THEME = ConstantValue.AppTheme_Purple
            //-------------------------\\

            callHomeActivity()
        }

        //=========================\\
        tv_ThirdTheme.setOnClickListener {

            objSaveData.save(ConstantValue.ManagerAppTheme,"2")
          //  Manager_Home_Activity.THEME = ConstantValue.AppTheme
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

                objSaveData.save("mFontNameManager",mArrayFontName.get(i).toString())
                objSaveData.save("mFont", "" + i)
                objSaveData.save("mFontPathManager", "" + mArrayFontPath.get(i).toString())
                tv_Font.setText("Current Font :- " + objSaveData.getString("mFontNameManager"))

                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                        .setDefaultFontPath(objSaveData.getString("mFontPathManager"))
                        .setFontAttrId(R.attr.fontPath)
                        .build()

                )
                callHomeActivity()
            }

        }

    }

    private fun callHomeActivity() {


        val intent = Intent(activity,Manager_Home_Activity::class.java)
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


        fun newInstance(param1: String, param2: String): Manager_Change_Background_Fragment {
            val fragment = Manager_Change_Background_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
