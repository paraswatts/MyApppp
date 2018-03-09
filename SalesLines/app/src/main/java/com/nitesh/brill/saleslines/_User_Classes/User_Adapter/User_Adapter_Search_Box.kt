package com.nitesh.brill.saleslines._User_Classes.User_Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._User_Classes.User_Fragment.User_Update_Lead_Details
import com.nitesh.brill.saleslines._User_Classes.User_PojoClass.GetAllLeadsData
import java.util.ArrayList


/**
 * Created by Web Designing Brill on 04-07-2017.
 */

class User_Adapter_Search_Box(mContext: FragmentActivity, private val albumList:  List<GetAllLeadsData>,private val selectedList:  List<GetAllLeadsData>) : RecyclerView.Adapter<User_Adapter_Search_Box.ViewHolder>(),Filterable {


    public val mContext: Context
    public var filterAlbumbList:  List<GetAllLeadsData>? = null
    public var filterSelectedList:  List<GetAllLeadsData>? = null
    var lastPosition = -1
    init {

        this.mContext = mContext
        this.filterAlbumbList = albumList
        this.filterSelectedList = selectedList

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                UsefullData.Log("charString  "+charString)

                if (charString.isEmpty()) {
                    filterAlbumbList = albumList
                } else {
                    val filteredList = ArrayList<GetAllLeadsData>()
                    for ( row in albumList) {
                        UsefullData.Log("=======  "+row.leadname)
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.Phone.contains(charSequence) || row.leadname.contains(charString)) {
                            UsefullData.Log("==== leadname ===  "+row.leadname)
                            UsefullData.Log("=== Phone ====  "+row.Phone)

                            filteredList.add(row)
                        }
                    }

                    filterAlbumbList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filterAlbumbList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filterAlbumbList = filterResults.values as ArrayList<GetAllLeadsData>
                notifyDataSetChanged()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.adapter_search_box, parent, false)

        return ViewHolder(layout)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list = filterAlbumbList!![position]
        holder.tv_Date.setText(list.EnquiryDate)
        holder.tv_Name.setText(Html.fromHtml("<u>" + list.leadname + "</u>"))
        holder.tv_Mobile.setText(list.Phone)
        if (list.type.equals("null")) {
            holder.tv_H.setText("-")
        } else {
            if (list.type.equals("H")) {

                holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.orange))
                holder.tv_H.setText(list.type)
            } else if (list.type.equals("W")) {
                holder.tv_H.setText(list.type)

                holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.yello))
            } else if (list.type.equals("C")) {
                holder.tv_H.setText(list.type)
                holder.tv_H.setBackgroundColor(mContext.resources.getColor(R.color.turquish))
            }

        }
        holder.rt_Rating.rating = list.rating.toFloat()

        Log.d("==", "====" + position)

        if (list.Data_PC.equals("")) {

            //=======nothing do =======
            holder.iv_ImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.profile_pic))

        } else {

            val decodedString = Base64.decode(list.Data_PC, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.iv_ImageView.setImageBitmap(decodedByte)
        }

        val animation: Animation = AnimationUtils.loadAnimation(mContext,if(position>lastPosition){ R.anim.up_from_bottom}else {R.anim.down_from_top})

             holder.itemView.startAnimation(animation);
             lastPosition = position;

        holder.rl_RootLayput.setOnClickListener {

            Log.e("Lead details", list.EnquiryId + "====" + list.LeadId + "=====" + list.DemoId + "====" + "" + list.rating + "=====" + list.ManagerId + "===" + list.UserId)

            var mFragment = User_Update_Lead_Details.newInstance(list.EnquiryId, list.LeadId, list.DemoId, "" + list.rating, list.ManagerId, list.UserId)
            val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
            fragmentTransaction.replace(R.id.content_frame, mFragment)
            fragmentTransaction.commit()
        }

        if(filterSelectedList!!.contains(filterAlbumbList!!.get(position)))
        {
            holder.rl_RootLayput.setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPrimary))
        }
        else {
            holder.rl_RootLayput.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white))
        }


    }

    override fun getItemCount(): Int {

        return filterAlbumbList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var iv_ImageView: ImageView
        var tv_Date: TextView
        var tv_Name: TextView
        var tv_Mobile: TextView
        var tv_H: TextView
        var rt_Rating: RatingBar


        var rl_RootLayput: LinearLayout


        init {
            iv_ImageView = itemView.findViewById(R.id.iv_ImageView) as ImageView

            tv_Date = itemView.findViewById(R.id.tv_Date) as TextView
            tv_Name = itemView.findViewById(R.id.tv_Name) as TextView
            tv_Mobile = itemView.findViewById(R.id.tv_Mobile) as TextView
            tv_H = itemView.findViewById(R.id.tv_H) as TextView
            rt_Rating = itemView.findViewById(R.id.rt_Rating) as RatingBar
            rl_RootLayput = itemView.findViewById(R.id.rl_RootLayput) as LinearLayout


        }
    }




}
