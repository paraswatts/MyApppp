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
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GM_AsmView
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_View_Home_Fragment

/**
 * Created by Nitesh Android on 11-08-2017.
 */


class ASM_Home_Adapter(activity: Activity, private val body: List<GM_AsmView>) : RecyclerView.Adapter<ASM_Home_Adapter.ViewHolder>() {

    private val mContext: Context


    init {
        this.mContext = activity
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.bindItems(body[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_gm_home, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return body.size
    }

    inner class ViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {


        fun bindItems(gM_AsmView: GM_AsmView) {
            val textViewName = itemView.findViewById(R.id.tv_View) as Button

            textViewName.text = gM_AsmView.Name

            textViewName.setOnClickListener {

                var mFragment = Manager_View_Home_Fragment.newInstance("", "" + gM_AsmView.UserId)
                val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                fragmentTransaction.replace(R.id.content_frame, mFragment)
                fragmentTransaction.commit()
            }
        }


    }


}

