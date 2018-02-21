package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_TotalSalesDetails

class Manager_TotalSales_Adapter(activity: Activity, val userList: List<Manager_TotalSalesDetails>) : RecyclerView.Adapter<Manager_TotalSales_Adapter.ViewHolder>() {

    private val mContext: Context
    private var currentPosition = 0
    private var filterList: List<Manager_TotalSalesDetails>? = null
    var flag = false

    init {

        this.mContext = activity as Context
        this.filterList = userList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Manager_TotalSales_Adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_totalsales, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: Manager_TotalSales_Adapter.ViewHolder, position: Int) {
        //holder.bindItems(userList[position])
        var list = filterList!![position]

        holder.tv_Enq.text = list.EnquiryNum
        holder.textViewName.text = list.leadname
        holder.tv_Close.text = list.ClosedDate
        holder.tv_ProductSold.text = list.ProductSold
        holder.tv_Salevalue.text = list.Salevalue
        holder.tv_Sman.text = list.sman
        holder.linearLayout.setVisibility(View.GONE);
        holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropuparrow, 0);
        if (currentPosition == position) {
            //creating an animation
            var slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
            holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdownarrow, 0);
        }
        holder.textViewName.setOnClickListener {
            //getting the position of the item to expand it
            currentPosition = position
            holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropuparrow, 0);

            //reloding the list
            notifyDataSetChanged()
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        fun bindItems(gM_AsmView: Manager_TotalSalesDetails) {
//
//            val textViewName = itemView.findViewById(R.id.textViewName) as TextView
//            val tv_Enq = itemView.findViewById(R.id.tv_Enq) as TextView
//            val tv_Name = itemView.findViewById(R.id.tv_Name) as TextView
//            val tv_Close = itemView.findViewById(R.id.tv_Close) as TextView
//            val tv_ProductSold = itemView.findViewById(R.id.tv_ProductSold) as TextView
//            val tv_Salevalue = itemView.findViewById(R.id.tv_Salevalue) as TextView
//            val tv_Sman = itemView.findViewById(R.id.tv_Sman) as TextView
//
//
//
//        }


        val textViewName: TextView
        val tv_Enq: TextView

        val tv_Close: TextView
        val tv_ProductSold: TextView
        val tv_Salevalue: TextView
        val tv_Sman: TextView
        val linearLayout: LinearLayout


        init {
            linearLayout = itemView.findViewById(R.id.linearLayout) as LinearLayout
            textViewName = itemView.findViewById(R.id.textViewName) as TextView
            tv_Enq = itemView.findViewById(R.id.tv_Enq) as TextView

            tv_Close = itemView.findViewById(R.id.tv_Close) as TextView
            tv_ProductSold = itemView.findViewById(R.id.tv_ProductSold) as TextView
            tv_Salevalue = itemView.findViewById(R.id.tv_Salevalue) as TextView
            tv_Sman = itemView.findViewById(R.id.tv_Sman) as TextView


            /*

             */


        }
    }
}