package com.nitesh.brill.saleslines.Common_Adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._GM_Classes.GM_PojoClass.GetGraphDay


/**
 * Created by Nitesh Android on 11-08-2017.
 */

class Graph_Adapter_Day(activity: Activity, val userList: List<GetGraphDay>) : RecyclerView.Adapter<Graph_Adapter_Day.ViewHolder>() {

    private val mContext: Context


    init {

        this.mContext = activity as Context

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_graph_pie_chart, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(GetGraph: GetGraphDay) {


            //======================================================================================\\

            var barChart = itemView.findViewById(R.id.barchart) as BarChart
            var mName = itemView.findViewById(R.id.mName) as TextView
            mName.setText("Day" + GetGraph.Day!!)

            if (userList.size > 0) {
                UsefullData.Log("call 1")
                barChart.axisLeft.setDrawGridLines(false)
                barChart.axisRight.setDrawGridLines(false)
                val xAxis = barChart.getXAxis()
                val rightAxis = barChart.axisRight
                val leftAxis = barChart.axisLeft
                leftAxis.setAxisMinValue(0f);
                rightAxis.setAxisMinValue(0f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
                xAxis.textSize = 15f
                xAxis.xOffset
                barChart.getLegend().setEnabled(false);

                val entries: MutableList<BarEntry> = ArrayList()
                entries.add(BarEntry(GetGraph.Newleads!!.toFloat(), 0))
                entries.add(BarEntry(GetGraph.FollowedUps!!.toFloat(), 1))
                entries.add(BarEntry(GetGraph.Missedfollowedups!!.toFloat(), 2))
                entries.add(BarEntry(GetGraph.salevalue!!.toFloat(), 3))

                val dataSets = BarDataSet(entries, " ")

                val colors = mContext.resources.getIntArray(R.array.mColours)

                dataSets.setColors(colors)
                val labels = ArrayList<String>()
//                labels.add(" Day " + GetGraph.Day)
                labels.add("")
                labels.add("")
                labels.add("")
                labels.add("")


                val data = BarData(labels, dataSets)

                data.setValueFormatter(MyValueFormatter())
                barChart.data = data
                dataSets.setValueTextSize(12f)
                 barChart. setViewPortOffsets(0f, 0f, 0f, 0f);
                barChart.setDescription("")
                barChart.setScaleEnabled(false);
                barChart.setTouchEnabled(false);

                barChart.getAxisRight().setDrawLabels(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawLabels(false);
                barChart.getAxisLeft().setEnabled(false);


            } else {
                UsefullData.Log("call 0")
                barChart.setNoDataText("No data available");
            }

        }

        inner internal class MyValueFormatter : ValueFormatter {
            override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
                return Math.round(value).toString() + ""
            }
        }
    }
}