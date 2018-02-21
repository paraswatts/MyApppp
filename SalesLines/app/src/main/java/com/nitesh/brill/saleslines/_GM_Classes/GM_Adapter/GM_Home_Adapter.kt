package com.nitesh.brill.saleslines._GM_Classes.GM_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.AGM_HomeFragment
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GM_AsmView


/**
 * Created by Nitesh Android on 11-08-2017.
 */

class GM_Home_Adapter(activity: Activity, val userList: List<GM_AsmView>) : RecyclerView.Adapter<GM_Home_Adapter.ViewHolder>() {

    private val mContext: Context


    init {

        this.mContext = activity as Context

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GM_Home_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gm_home, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: GM_Home_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(gM_AsmView: GM_AsmView) {

            val textViewName = itemView.findViewById(R.id.tv_View) as Button

            textViewName.text = gM_AsmView.Name


            //==============================================\\

            textViewName.setOnClickListener {

                var mFragment = AGM_HomeFragment.newInstance("", ""+gM_AsmView.UserId)
                val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, mFragment)
                fragmentTransaction.commit()
            }

        }
    }
}