package com.brill.nitesh.punjabpool.Common


import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Spinner
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.SaveData
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.Common_Files.Validation
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtil
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtils
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtilsTesting
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class BaseFragment : Fragment(), ConstantValue {


    lateinit var objSaveData: SaveData
    lateinit var objUsefullData: UsefullData
    lateinit var objValidation: Validation
    var apiEndpointInterface: ApiEndpointInterface? = null

    var smallapiEndpointInterface: ApiEndpointInterface? = null

    var apiEndpointInterfaceTesting: ApiEndpointInterface? = null

    var mPERMISSIONS: MutableList<String> = ArrayList()
    var permissionsRequired: MutableList<String> = ArrayList()
    var accessStorage: MutableList<String> = ArrayList()


    var mArrayBindProduct: MutableList<String> = ArrayList()
    var mArrayBindSources: MutableList<String> = ArrayList()
    var mArrayBindStages: MutableList<String> = ArrayList()
    var mArrayBindResponse: MutableList<String> = ArrayList()

    var mArrayStateName: MutableList<String> = java.util.ArrayList()
    var mArrayStateId: MutableList<String> = java.util.ArrayList()

    /**
     *  Padding for all view
     *
     */

    var paddingBottom = 8
    var paddingLeft = 12
    var paddingRight = 8
    var paddingTop = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPERMISSIONS.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        mPERMISSIONS.add(Manifest.permission.CAMERA)
        mPERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)


        permissionsRequired.add(Manifest.permission.RECORD_AUDIO)
        permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionsRequired.add(Manifest.permission.READ_PHONE_STATE)
        permissionsRequired.add(Manifest.permission.PROCESS_OUTGOING_CALLS)

        accessStorage.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        accessStorage.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)



        objSaveData = SaveData(activity)
        objUsefullData = UsefullData(activity)
        objValidation = Validation(activity)
        apiEndpointInterface = RetrofitUtil.hostRetrofit.create(ApiEndpointInterface::class.java!!)
        smallapiEndpointInterface = RetrofitUtils.apihostRetrofit.create(ApiEndpointInterface::class.java!!)
        // notificationInterface = RetrofitUtilNotification.apihostRetrofit.create(ApiEndpointInterface::class.java!!)
        apiEndpointInterfaceTesting = RetrofitUtilsTesting.apihostRetrofitttt.create(ApiEndpointInterface::class.java!!)


        /*
           send regid to server

        */

        UsefullData.Log("====== Boolean===== " + objSaveData.getBoolean(ConstantValue.CHECK_FLAG))


        sendNotification(objSaveData.getString(ConstantValue.USER_ID))



        if (isNetworkConnected) {


            getCompulsoryData()

        }

    }

    private fun getCompulsoryData() {


        //=================== Sources===========================\\

        //objUsefullData.showProgress("getting sources ...", "")
        val mCallSources = apiEndpointInterface!!.getBindSources(objSaveData.getString(ConstantValue.CLIENT_ID))

        mCallSources.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                //     objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())
                        mArrayBindSources.clear()
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            //   UsefullData.Log("==" + item)

                            mArrayBindSources.add("" + array.getJSONObject(i).get("Sources"))


                        }


                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

        })


        //=================== Stages===========================\\

        val mCallStages = apiEndpointInterface!!.getBindStages(objSaveData.getString(ConstantValue.CLIENT_ID))

        mCallStages.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                //            objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                try {
                Log.d("URL", "===" + response!!.raw().request().url())
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())
                        mArrayBindStages.clear()
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            //  UsefullData.Log("==" + item)
                            mArrayBindStages.add("" + array.getJSONObject(i).get("Stages"))
                        }



                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

        })

        //=================== Product===========================\\

        val mCallProduct = apiEndpointInterface!!.getBindProducts(objSaveData.getString(ConstantValue.CLIENT_ID))

        mCallProduct.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                // objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        mArrayBindProduct.clear()
                        UsefullData.Log("User_Response    " + response.body().toString())
                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {

                            //    UsefullData.Log("==" + array.getJSONObject(i).get("Products"))
                            mArrayBindProduct.add("" + array.getJSONObject(i).get("Products"))
                        }


                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

        })


        //=================== Response===========================\\

        val mCallResponse = apiEndpointInterface!!.getBindResponse(objSaveData.getString(ConstantValue.CLIENT_ID))

        mCallResponse.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                objUsefullData.showMsgOnUI("Error :" + t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())

                        mArrayBindResponse.clear()
                        val array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            //    UsefullData.Log("==" + item)
                            mArrayBindResponse.add("" + array.getJSONObject(i).get("Response"))
                        }



                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }
            }

        })


        //=================== States===========================\\

        val mCallStates = apiEndpointInterface!!.mGetStates()
        mCallStates.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {


                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())

                        var array = JSONArray(response!!.body().toString())
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(i)

                            mArrayStateId.add(item.getString("StateId"))
                            mArrayStateName.add(item.getString("StateName"))


                        }

                } else {
                    UsefullData.Log("========" + response.code())
                    objUsefullData.getError("" + response.code())
                }
                } catch (e: Exception) {
                    objUsefullData.getException(e)
                }

            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {


                //   objUsefullData.showMsgOnUI("Error :" + t)

                UsefullData.Log("onFailure ===" + t)


            }

        })


    }


    private fun sendNotification(userid: String) {


        UsefullData.Log("=========regId===========" + objSaveData.getString(ConstantValue.FIREBASEID))
        val paramObject = JSONObject()
        paramObject.put("UserId", userid)
        paramObject.put("AndriodRegisterid", "" + objSaveData.getString(ConstantValue.FIREBASEID))

        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.sendAndroidId(userid, body)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                //  Log.d("URL", "===Login==" + response!!.raw().request().url())
                try {
                    if (response!!.isSuccessful) {

                        UsefullData.Log("======Login onResponse =======" + response!!.body().toString())
                    } else {

                        UsefullData.Log("========" + response.code())
                        //  objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                //    UsefullData.Log("======Login onFailure=======" + t)
            }

        })


    }

    // =================================//

    fun setActionTitle(title: String) {
        activity.actionBar!!.title = title
    }

    // =================== INTERNET ===================//

    val isNetworkConnected: Boolean
        get() {
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni == null) {
                objUsefullData
                        .showMsgOnUI("No Internet Connection.")
                return false
            } else
                return true

        }


    // ============================================//

    fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0

        for (i in 0..spinner.count) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }

        }
        return index

    }

}