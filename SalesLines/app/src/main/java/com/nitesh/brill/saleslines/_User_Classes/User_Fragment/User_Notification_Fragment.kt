package com.nitesh.brill.saleslines._User_Classes.User_Fragment


import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.AudioContract
import com.nitesh.brill.saleslines._User_Classes.User_Call_Record.Database.NotificationCursorAdapter
import kotlinx.android.synthetic.main.fragment_user_notification.*


class User_Notification_Fragment : BaseFragment() {

    private val mLoaderCallback=LoaderCallback()


    private val NOTIFICATION_LOADER = 0

    private var cursorAdapter: NotificationCursorAdapter? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("I am in Notification ", "Fragment")

        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("I am in Notification ", "Fragment")

        objUsefullData.showMsgOnUI("onCreateView")
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_notification, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lv_Notification_List.adapter = cursorAdapter

        Log.e("I am in Notification ", "Fragment")
        objUsefullData.showMsgOnUI("onViewCreated")


        val selectQuery = "SELECT  * FROM " + AudioContract.AudioEntry.TABLE_NOTIFICATIONS_MISSED_FOLLOWUPS

      //  val db = activity.getWritableDatabase()
     //   val cursor = db.rawQuery(selectQuery, null)


   //     activity.loaderManager.initLoader(0,null,mLoaderCallback)

       // val notificationColumnIndex = cursor.getColumnIndex(AudioContract.AudioEntry.COLUMN_NOTIFICATION_MESSAGE)


        //loaderManager.initLoader(NOTIFICATION_LOADER,null,activity)


        cursorAdapter = NotificationCursorAdapter(activity,null)
            cursorAdapter!!.notifyDataSetChanged()

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): User_Notification_Fragment {
            val fragment = User_Notification_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private inner class LoaderCallback : LoaderManager.LoaderCallbacks<Cursor> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            Log.e("I am in Create Loader", "")


            // This loader will execute the ContentProvider's query method on a background thread
            return CursorLoader(activity, // Parent activity context
                    AudioContract.AudioEntry.CONTENT_URI_NOTIFICATION, // No selection arguments
                    null, // Columns to include in the resulting Cursor
                    null, // No selection clause
                    null,
                    null)// Provider To change body of created functions use File | Settings | File Templates.
        }

        override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
            cursorAdapter!!.swapCursor(null);

        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            cursorAdapter!!.swapCursor(null);

        }
    }



}// Required empty public constructor
