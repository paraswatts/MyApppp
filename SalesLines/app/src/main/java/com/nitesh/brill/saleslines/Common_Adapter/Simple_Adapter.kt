package com.nitesh.brill.saleslines.Common_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nitesh.brill.saleslines.R

/**
 * Created by Nitesh Android on 11-08-2017.
 */


class Simple_Adapter(mContext: Activity, albumList: List<String>) : RecyclerView.Adapter<Simple_Adapter.ViewHolder>() {


    private val mContext: Context
    private var filterAlbumbList: List<String> = ArrayList<String>()

    init {
        this.mContext = mContext
        this.filterAlbumbList = albumList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.simpleadapterview, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list = filterAlbumbList!![position]
        // holder.mTextView.setText(list.get())
        Log.e("=====", list + "===" + filterAlbumbList!![position])

    }

    override fun getItemCount(): Int {

        return filterAlbumbList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView

        init {

            mTextView = itemView.findViewById(R.id.mTextView) as TextView


        }
    }


}
