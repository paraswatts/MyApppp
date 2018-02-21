package com.nitesh.brill.saleslines._User_Classes.User_Adapter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.LeadDemo

/**
 * Created by Web Designing Brill on 04-07-2017.
 */

class User_Adapter_Lead_Demo(mContext: FragmentActivity, private val albumList: MutableList<LeadDemo>) : RecyclerView.Adapter<User_Adapter_Lead_Demo.ViewHolder>() {
    private val mContext: Context


    init {

        this.mContext = mContext

    }









    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_lead_demo, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list = albumList[position]

        if (position.equals(0)) {
            holder.tv_DemoDate.visibility = View.VISIBLE
        } else {
            holder.tv_DemoDate.visibility = View.GONE
        }
        if(list.DemoDate.equals("01/01/1990")) {
            holder.tv_DemoDate.visibility = View.GONE
        }else{
            holder.tv_DemoDate.setText("Demo Given On  :- " + list.DemoDate)
        }
        holder.tv_Name.setText("Last Called On :- " + list.CreatedDate)
        holder.tv_Mobile.setText("Comments :- " + list.Comments)
        if (list.H_Checked.equals("H")){

            holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.orange))
            holder.tv_H.setText(list.H_Checked)
        }else  if (list.H_Checked.equals("W")){
            holder.tv_H.setText(list.H_Checked)

            holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.yello))
        } else if (list.H_Checked.equals("C")){
            holder.tv_H.setText(list.H_Checked)
            holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.turquish))
        }






        Log.d("==", "====" + position)


    }

    override fun getItemCount(): Int {

        return albumList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tv_Name: TextView
        var tv_Mobile: TextView
        var tv_H: TextView
        var tv_DemoDate: TextView


        var rl_RootLayput: RelativeLayout


        init {


            tv_Name = itemView.findViewById(R.id.tv_Name) as TextView
            tv_Mobile = itemView.findViewById(R.id.tv_Mobile) as TextView
            tv_H = itemView.findViewById(R.id.tv_H) as TextView
            tv_DemoDate = itemView.findViewById(R.id.tv_DemoDate) as TextView

            rl_RootLayput = itemView.findViewById(R.id.rl_RootLayput) as RelativeLayout


            /*

             */


        }
    }
}