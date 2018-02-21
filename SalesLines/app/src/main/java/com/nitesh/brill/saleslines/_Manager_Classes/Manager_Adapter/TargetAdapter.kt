package com.nitesh.brill.saleslines._Manager_Classes.Manager_Adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.SaveData
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtil
import com.nitesh.brill.saleslines._Manager_Classes.Manager_PojoClass.Manager_Total_Target
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class TargetAdapter(activity: Activity, val userList: List<Manager_Total_Target>) : RecyclerView.Adapter<TargetAdapter.ViewHolder>() {

    private val mContext: Context
    private var currentPosition = 0
    private var filterList: List<Manager_Total_Target>? = null
    var objUsefullData: UsefullData
    var objSaveData: SaveData
    var flag = false
    var apiEndpointInterface: ApiEndpointInterface? = null

    init {
        apiEndpointInterface = RetrofitUtil.hostRetrofit.create(ApiEndpointInterface::class.java!!)
        this.mContext = activity as Context
        this.filterList = userList
        this.objUsefullData = UsefullData(activity)
        this.objSaveData = SaveData(activity)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: TargetAdapter.ViewHolder, position: Int) {
        //holder.bindItems(userList[position])
        var list = filterList!![position]
        holder.tv_Targetid.text = list.TargetId
        holder.textViewName.text = list.ProductName
        holder.tv_EName.text = list.EmployeeName
        holder.tv_TotalTarget.text = list.Target

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


        holder.tv_TotalTarget.setOnClickListener {


            var inflater = LayoutInflater.from(mContext);
            val alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null)
            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("Update")
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout)
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false)

            val dialog = alert.create()
            val et_product = alertLayout.findViewById(R.id.et_product) as EditText
            val et_target = alertLayout.findViewById(R.id.et_target) as EditText
            val btn_Cancel = alertLayout.findViewById(R.id.btn_Cancel) as Button

            val btn_Done = alertLayout.findViewById(R.id.btn_Done) as Button

            et_product.setText(list.ProductName)
            et_target.setText(list.Target)
            btn_Done.setOnClickListener {
                dialog.dismiss()
                objUsefullData.showProgress("Please Wait...", "")

                val paramObject = JSONObject()
                paramObject.put("ModifiedUserId", list.`$id`)

                paramObject.put("Target", et_target.text.toString())
                paramObject.put("TargetId", list.TargetId)

                UsefullData.Log("==================" + paramObject.toString())
                objUsefullData.dismissProgress()

                val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

                val call = apiEndpointInterface!!.mUpdateSaleManTargets(list.TargetId, body)
                call.enqueue(object : retrofit2.Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        objUsefullData.dismissProgress()
                        Log.d("URL", "=====" + response!!.raw().request().url())
                        try {
                            if (response.isSuccessful) {

                                try {
                                    UsefullData.Log("=============" + response!!.body())


                                    var array = JSONArray(response!!.body().toString())
                                    for (i in 0..(array.length() - 1)) {
                                        val item_ = array.getJSONObject(0)
                                        var success = item_.optString("Success")

                                        if (success.equals("1")) {
                                            objUsefullData.showMsgOnUI("Successfully Updated")

                                            holder.tv_TotalTarget.text = et_target.text.toString()

                                        } else {
                                            objUsefullData.showMsgOnUI("Failed Update")
                                        }

                                    }

                                } catch (e: Exception) {
                                    objUsefullData.getException(e)
                                }
                            } else {
                                UsefullData.Log("========" + response.code())
                                objUsefullData.getError("" + response.code())
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                        objUsefullData.dismissProgress()
                        objUsefullData.showMsgOnUI("Failed To fetch data")
                        UsefullData.Log("onFailure ===" + t)
                    }

                })
            }




            btn_Cancel.setOnClickListener {


                dialog.dismiss()


            }
            dialog.show()

        }


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }


    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView
        val tv_Targetid: TextView

        val tv_EName: TextView
        val tv_TotalTarget: TextView

        val linearLayout: LinearLayout


        init {
            linearLayout = itemView.findViewById(R.id.linearLayout) as LinearLayout
            textViewName = itemView.findViewById(R.id.textViewName) as TextView
            tv_Targetid = itemView.findViewById(R.id.tv_Targetid) as TextView

            tv_EName = itemView.findViewById(R.id.tv_EName) as TextView
            tv_TotalTarget = itemView.findViewById(R.id.tv_TotalTarget) as TextView


        }
    }
}