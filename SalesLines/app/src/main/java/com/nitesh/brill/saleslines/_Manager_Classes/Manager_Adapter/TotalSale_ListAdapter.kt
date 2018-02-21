package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtil

class TotalSale_ListAdapter(private val mContext: Activity, private val name: List<String>,
                            private val AllName: Map<String, List<String>>) : BaseExpandableListAdapter() {
    private val objUsefullData: UsefullData


    init {
        objUsefullData = UsefullData(mContext)


        UsefullData.Log("=========" +AllName.size)
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return AllName[name[groupPosition]]!!.get(childPosition)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }


    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val laptop = getChild(groupPosition, childPosition) as String



        val inflater = mContext.layoutInflater

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.all_sales, null)
        }

        val item = convertView!!.findViewById(R.id.name) as TextView

        UsefullData.Log("=====item====" +item)

      //  val edit = convertView.findViewById(R.id.edit) as ImageView

//        if (childPosition == 2) {
//            edit.visibility = View.VISIBLE
//
//        } else {
//
//            edit.visibility = View.GONE
//        }
        item.text = laptop
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return AllName[name[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return name[groupPosition]
    }

    override fun getGroupCount(): Int {
        return name.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val leadName = getGroup(groupPosition) as String
        if (convertView == null) {
            val infalInflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.all_sales, null)
        }
        val item = convertView!!.findViewById(R.id.name) as TextView
        item.setTypeface(null, Typeface.BOLD)
        item.text = leadName
        UsefullData.Log("=====leadName====" +leadName)

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}