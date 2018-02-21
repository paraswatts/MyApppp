package com.nitesh.brill.saleslines.Common_Adapter

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nitesh.brill.saleslines.Common_Files.SaveData
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R

/**
 * Created by nitesh on 8/1/18.
 */

class ListExampleAdapter(activity: Activity, mArrayFontName: ArrayList<String>) : BaseAdapter() {

    private val mContext: Context

    lateinit var objSaveData: SaveData
    private var filterList: ArrayList<String>? = null
    private var mInflator: LayoutInflater? = null

    init {

        this.mContext = activity
        objSaveData = SaveData(activity)
        this.filterList = mArrayFontName
        mInflator = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = this.mInflator!!.inflate(R.layout.spinner_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = filterList!!.get(position)

//        if (objSaveData.getString("mFontNameUser").equals(filterList!!.get(position))) {
//            vh.label.text = filterList!!.get(position)
//            vh.label.setTypeface(null, Typeface.BOLD);
//        } else {
//            vh.label.text = filterList!!.get(position)
//        }


        UsefullData.Log("======= Adapter===========" +filterList!!.get(position))

        return view!!
    }

    override fun getItem(position: Int): Any {
        return filterList!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filterList!!.size
    }

    private class ListRowHolder(row: View?) {
        val label: RadioButton


        init {


            this.label = row?.findViewById(R.id.radioButton) as RadioButton

        }


    }
}