package com.brill.nitesh.punjabpool.Common


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.*
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtil
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtils
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


open class BaseActivity : AppCompatActivity(), ConstantValue {


    lateinit var objSaveData: SaveData
    lateinit var objUsefullData: UsefullData
    lateinit var objValidation: Validation

    var apiEndpointInterface: ApiEndpointInterface? = null
    var apiEndpointInterface1: ApiEndpointInterface? = null

    var smallapiEndpointInterface: ApiEndpointInterface? = null
    var currentDate = Date()
    lateinit var dateHelper: DateHelper


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.main);

        objSaveData = SaveData(this)
        objUsefullData = UsefullData(this)
        objValidation = Validation(this)
        dateHelper = DateHelper(currentDate)
        apiEndpointInterface = RetrofitUtil.hostRetrofit.create(ApiEndpointInterface::class.java!!)
        apiEndpointInterface1 = RetrofitUtil.hostRetrofit1.create(ApiEndpointInterface::class.java!!)

        smallapiEndpointInterface = RetrofitUtils.apihostRetrofit.create(ApiEndpointInterface::class.java!!)
        //  notificationInterface  = RetrofitUtilNotification.apihostRetrofit.create(ApiEndpointInterface::class.java!!)

        //======= Register network Reciver ====\\

        //registerReceiver(NetworkConnectivityChange(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        /*
          send regid to server
       */

        sendNotification(objSaveData.getString(ConstantValue.USER_ID))

    }

    private fun sendNotification(userid: String) {


        UsefullData.Log("=========regId===========" + objSaveData.getString(ConstantValue.FIREBASEID))


        val paramObject = JSONObject()

        paramObject.put("UserId", userid)
        paramObject.put("AndriodRegisterid", "" + objSaveData.getString(ConstantValue.FIREBASEID))


        // UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())

        val call = apiEndpointInterface!!.sendAndroidId(userid, body)
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                try {
                    //  Log.d("URL", "===Login==" + response!!.raw().request().url())
                    if (response!!.isSuccessful) {

                        UsefullData.Log("======Login onResponse =======" + response!!.body().toString())
                    } else {

                        //  UsefullData.Log("========" + response.code())
                        //objUsefullData.showMsgOnUI(resources.getString(R.string.server_error))

                    }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                UsefullData.Log("======Login onFailure=======" + t)
            }

        })


    }

    // =================================//

    fun setActionTitle(title: String) {
        actionBar!!.title = title
    }

    // =================== INTERNET ===================//

    val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni == null) {
                objUsefullData
                        .showMsgOnUI("No Internet Connection")
                return false
            } else
                return true

        }


    val isNetworkCheck: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni == null) {

                return false
            } else
                return true

        }

    // ============================================//


}