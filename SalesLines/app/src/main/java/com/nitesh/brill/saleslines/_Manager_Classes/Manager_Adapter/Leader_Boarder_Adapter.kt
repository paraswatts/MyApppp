package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.nitesh.brill.saleslines.R

/**
 * Created by Nitesh Android on 11-08-2017.
 */

class Leader_Boarder_Adapter(activity: Activity, val userList: MutableList<String>,val countList: MutableList<String>) : RecyclerView.Adapter<Leader_Boarder_Adapter.ViewHolder>() {

    private val mContext: Context


    init {

        this.mContext = activity as Context

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Leader_Boarder_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_leaderboard, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: Leader_Boarder_Adapter.ViewHolder, position: Int) {

        if (position>0){
            holder.ll_main.visibility=View.GONE

        }

        holder.tv_Saleman.setText(userList[position])
        holder.tv_Count.setText(countList[position])

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_Count: TextView
        var tv_Saleman: TextView
        var ll_main: LinearLayout



        init {
            tv_Count = itemView.findViewById(R.id.tv_Count) as TextView
            tv_Saleman = itemView.findViewById(R.id.tv_Saleman) as TextView
            ll_main = itemView.findViewById(R.id.ll_main) as LinearLayout


        }


    }
}