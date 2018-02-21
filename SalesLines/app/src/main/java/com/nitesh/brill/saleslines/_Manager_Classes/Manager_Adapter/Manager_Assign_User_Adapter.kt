package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_Assign_User_Product_Fragment
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_User_List

class Manager_Assign_User_Adapter(activity: Activity, val userList: List<Manager_User_List>) : RecyclerView.Adapter<Manager_Assign_User_Adapter.ViewHolder>() {

    private val mContext: Context


    init {

        this.mContext = activity as Context

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Manager_Assign_User_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gm_home, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: Manager_Assign_User_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(gM_AsmView: Manager_User_List) {

            val textViewName = itemView.findViewById(R.id.tv_View) as Button

            textViewName.text = gM_AsmView.Name


            //==============================================\\

            textViewName.setOnClickListener {
                var mFragment = Manager_Assign_User_Product_Fragment.newInstance(""+gM_AsmView.ManagerId, "" + gM_AsmView.UserId,""+gM_AsmView.Name)

                val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, mFragment)
                fragmentTransaction.commit()
            }

        }
    }
}