package com.nitesh.brill.saleslines.Authenticate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.multidex.MultiDex
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.brill.nitesh.punjabpool.Common.BaseActivity
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines.Ritrofit.ApiEndpointInterface
import com.nitesh.brill.saleslines.Ritrofit.RetrofitUtil
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 *
 * Created by Paras Android on 15-06-2017.
 *
 */


class Login_Activity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiEndpointInterface = RetrofitUtil.hostRetrofit.create(ApiEndpointInterface::class.java)
        //==================================================\\


        forgotPassword.setOnClickListener {

            callForgotPassword()

        }


        btn_LoginButton!!.setOnClickListener {
            if (checkValidation() && isNetworkConnected) {

                objUsefullData.showProgress("Login Please Wait...", "")

                loginFromServer()

            }
        }

    }

    private fun callForgotPassword() {


        val alertDialog = AlertDialog.Builder(
                this@Login_Activity).create()

        // Setting Dialog Title
        alertDialog.setTitle("Forgot Password")


        // Setting Icon to Dialog
        //  alertDialog.setIcon(R.drawable.saleslinelogo)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog, null)
        alertDialog.setView(dialogView)


        val edt = dialogView.findViewById(R.id.et_Email) as EditText
        val btn_Done = dialogView.findViewById(R.id.btn_Done) as Button

        btn_Done.setOnClickListener {

            if (checkEmail(edt)) {


                objUsefullData.showProgress("Please Wait...", "")
                val mCall = apiEndpointInterface!!.mGetuserid(edt.text.toString().trim())
                mCall.enqueue(object : Callback<JsonElement> {


                    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                        try{
                        //  objUsefullData.dismissProgress()
                        Log.d("URL", "===" + response!!.raw().request().url())
                        if (response.isSuccessful) {


                            UsefullData.Log("User_Response    " + response.body())
                            objUsefullData.showMsgOnUI(" Please check your email for the reset password instructions")

                            // alertDialog.dismiss()

                        } else {
                            UsefullData.Log("========" + response.code())
                            objUsefullData.getError("" + response.code())
                        }
                        } catch (e: Exception) {
                            objUsefullData.getException(e)
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                        // objUsefullData.dismissProgress()
                        objUsefullData. getThrowableException(t)
                        UsefullData.Log("onFailure ===" + t)

                        // alertDialog.dismiss()
                    }
                })
            }

        }


        // Showing Alert Message
        alertDialog.show()


    }

    private fun checkEmail(email: EditText): Boolean {


        if (objValidation.checkEmpty(email, "Email ")) {

            email.requestFocus()

            return false
        }
        if (!objValidation.checkForEmail(email, "Email ")) {

            email.requestFocus()

            return false
        }


        UsefullData.Log("=========================")
        return true


    }

    //===============================\\

    private fun loginFromServer() {
        objUsefullData.showProgress("Get Ready to Experience Best CRM..", "")

        val call = apiEndpointInterface!!.loginUser(et_Email!!.text.toString().trim(), et_Password!!.text.toString().trim())
        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===Login==" + response!!.raw().request().url())
                try {
                    if (response.isSuccessful) {


                        UsefullData.Log("======Login=======" + response!!.body().toString())
                        //[{"Success":"0"},{"Message":" This User Already Loggedin"}]

                        var array = JSONArray(response.body().toString())

                        var Success: String? = null
                        var Message: String? = null
                        var item: JSONObject? = null
                        for (i in 0..(array.length() - 1)) {

                            item = array.getJSONObject(i)

                            if (item.has("Success")) {
                                val item_ = array.getJSONObject(0)
                                val item__ = array.getJSONObject(1)
                                Success = item_.optString("Success")
                                Message = item__.optString("Message")
                            }
                        }


                        //==================== Message for Login========================\\

                        if (Success.equals("0")) {

                            objUsefullData.showMsgOnUI(Message.toString())


                        } else {

                            objSaveData.saveBoolean("loginState", true);

                            objSaveData.save("LoginId", et_Email.text.toString().trim())

                            UsefullData.Log("Login id of the user is" + objSaveData.getString("LoginId"))

                            objSaveData.save(ConstantValue.USER_LOGIN, "1")

                            if (!item!!.optString("Name").equals(null) && !item!!.optString("Name").equals("null")) {
                                objSaveData.save(ConstantValue.NAME, item!!.optString("Name")!!.toString())
                            }
                            objSaveData.save(ConstantValue.MANAGER_ID, item!!.optString("ManagerId")!!.toString())
                            if (item!!.optString("CompanyName") != null) {
                                objSaveData.save(ConstantValue.COMPANY_NAME, item!!.optString("CompanyName")!!.toString())
                            }

                            objSaveData.save(ConstantValue.USER_ID, item!!.optString("UserId")!!.toString())
                            objSaveData.save(ConstantValue.CLIENT_ID, item!!.optString("ClientId")!!.toString())
//                        objSaveData.save(ConstantValue.ID, item!!.optString("$id")!!.toString())
                            objSaveData.save(ConstantValue.ROLE_ID, item!!.optString("RoleId")!!.toString())
                            objSaveData.save(ConstantValue.EMAIL, et_Email.text.toString())

                            UsefullData.Log(item!!.optString("ClientId")!!.toString() + "===========" + item!!.optString("RoleId").toString())


                            Log.d("", objSaveData.getString(ConstantValue.USER_ID) + "=========" + UsefullData.Log(objSaveData.getString(ConstantValue.CLIENT_ID)))
                            var intent: Intent? = null

                            //=====================================\\

                            if (item!!.optString("RoleId").equals("5", ignoreCase = true)) {

                                //================ Used to Check FIrebase regID===================\\

                                objSaveData.save("send_ID", "1")





                                objUsefullData.showMsgOnUI("Login Successful")
                                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                                        .setDefaultFontPath(objSaveData.getString("mFontPathUser"))
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                                )
                                objSaveData.save("userRole", "user")
                                intent = Intent(this@Login_Activity, User_Home_Activity::class.java)

                            } else if (item!!.optString("RoleId").equals("4", ignoreCase = true)) {
                                objUsefullData.showMsgOnUI("Login Successful")
                                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                                        .setDefaultFontPath(objSaveData.getString("mFontPathManager"))
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                                )

                                objSaveData.save("userRole", "manager")
                                intent = Intent(this@Login_Activity, Manager_Home_Activity::class.java)

                            } else if (item!!.optString("RoleId").equals("3", ignoreCase = true)) {
                                objUsefullData.showMsgOnUI("Login Successful")
                                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                                        .setDefaultFontPath(objSaveData.getString("mFontPathASM"))
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                                )
                                objSaveData.save("userRole", "asm")
                                intent = Intent(this@Login_Activity, ASM_Home_Activity::class.java)

                            } else if (item!!.optString("RoleId").equals("2", ignoreCase = true)) {
                                objUsefullData.showMsgOnUI("Login Successful")
                                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                                        .setDefaultFontPath(objSaveData.getString("mFontPathAGM"))
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                                )

                                objSaveData.save("userRole", "agm")
                                intent = Intent(this@Login_Activity, AGM_Home_Activity::class.java)

                            } else if (item!!.optString("RoleId").equals("1", ignoreCase = true)) {
                                objUsefullData.showMsgOnUI("Login Successful")
                                CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                                        .setDefaultFontPath(objSaveData.getString("mFontPathGM"))
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                                )

                                objSaveData.save("userRole", "gm")
                                intent = Intent(this@Login_Activity, GM_Home_Activity::class.java)


                            } else {
                                objUsefullData.showMsgOnUI("You are not authorized to access")

                            }

                            if (intent != null) {
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }


                        }

                    } else {

                        UsefullData.Log("========" + response.code())
                        objUsefullData.showMsgOnUI(resources.getString(R.string.server_error))

                    }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                objUsefullData.dismissProgress()
                UsefullData.Log("===" + t)
                objUsefullData. getThrowableException(t)
            }


        })
    }

    private fun sendToken() {


    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    private fun checkValidation(): Boolean {
        if (objValidation.checkEmpty(et_Email, "Email ")) {

            et_Email.requestFocus()

            return false
        }
        if (!objValidation.checkForEmail(et_Email, "Email ")) {

            et_Email.requestFocus()

            return false
        }
        if (objValidation.checkEmpty(et_Password, "Password ")) {
            et_Password.requestFocus()
            return false
        }


        return true
    }
}


