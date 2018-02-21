package com.nitesh.brill.saleslines._GM_Classes.GM_Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.nitesh.brill.saleslines.Common_Fragment.Update_profile_Fragment
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.AGM_HomeFragment
import com.nitesh.brill.saleslines._GM_Classes.GM_Fragment.ASM_HomeFragment
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment.Manager_View_Home_Fragment
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.AllUserList


/**
 * Created by Nitesh Android on 11-08-2017.
 */

class GM_All_User_Adapter(activity: Activity, val userList: List<AllUserList>) : RecyclerView.Adapter<GM_All_User_Adapter.ViewHolder>() {

    private val mContext: Context

    init {
        this.mContext = activity as Context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GM_All_User_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_gm_home, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: GM_All_User_Adapter.ViewHolder, position: Int) {
        holder.bindItems(userList.get(position))
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(gM_AsmView: AllUserList) {

            val textViewName = itemView.findViewById(R.id.tv_View) as Button

            textViewName.text = gM_AsmView.Name

            //==============================================\\

            textViewName.setOnClickListener {

                val alertLayout = LayoutInflater.from(mContext).inflate(R.layout.dialog_all_user, null)
                val simpleAlert = AlertDialog.Builder(mContext).create()
                simpleAlert.setTitle("User Details")
                simpleAlert.setView(alertLayout)
                // simpleAlert.setMessage("Show simple Alert")
                val textViewName = alertLayout.findViewById(R.id.tv_Name) as TextView
                val tv_Email = alertLayout.findViewById(R.id.tv_Email) as TextView
                val tv_Phone = alertLayout.findViewById(R.id.tv_Phone) as TextView
                val tv_Role = alertLayout.findViewById(R.id.tv_Role) as TextView

                textViewName.setText(gM_AsmView.Name)
                tv_Email.setText(gM_AsmView.EmailId)
                tv_Phone.setText(gM_AsmView.Phone)
                tv_Role.setText(gM_AsmView.RoleId)


                if (gM_AsmView.RoleId.equals("AGM")) {

                    var mFragment = AGM_HomeFragment.newInstance("", "" + gM_AsmView.UserId)
                    //===== Call Fragment =====\\
                    callFragment(mFragment)

                }
               else if (gM_AsmView.RoleId.equals("ASM")) {
                    var mFragment =  ASM_HomeFragment.newInstance("", ""+gM_AsmView.UserId)
                    //===== Call Fragment =====\\
                    callFragment(mFragment)
                }
                else if (gM_AsmView.RoleId.equals("Mgr")) {
                    var mFragment = Manager_View_Home_Fragment.newInstance("", "" + gM_AsmView.UserId)
                    //===== Call Fragment =====\\
                    callFragment(mFragment)
                }

                else  if (gM_AsmView.RoleId.equals("SaleMan")) {
                    simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "EDIT", {
                        dialogInterface, i ->
                        if (gM_AsmView.RoleId.equals("SaleMan")) {
                            var mFragment = Update_profile_Fragment.newInstance("", "" + gM_AsmView.UserId)
                            //===== Call Fragment =====\\
                            callFragment(mFragment)
                        }
                    })
                    simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL", { dialogInterface, i ->
                        //Toast.makeText(mContext, "You clicked on OK", Toast.LENGTH_SHORT).show()
                    })

                    simpleAlert.show()

                }
            }

        }
    }

    private fun callFragment(mFragment: Fragment) {
        val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
        fragmentTransaction.replace(R.id.content_frame, mFragment)
        fragmentTransaction.commit()


    }


}