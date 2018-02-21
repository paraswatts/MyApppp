package com.nitesh.brill.saleslines.Common_Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UsefullData

import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_recommend_.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream






class Recommend_Fragment : BaseFragment() {

 val Message="I Recommend you to try Worlds first no-nonse SalesCRM CRM https://www.salelinecrm.com/try-for-free.php"
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_recommend_, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //=========================\\
        btn_Submit.setOnClickListener {
            if (checkValidation()) {

                if (isNetworkConnected)
                    sendDataToServer()

            }
        }


        fb.setOnClickListener {
            if (isNetworkConnected) {


                val fbIntent = Intent(Intent.ACTION_SEND)
                fbIntent.type = "text/plain"
                fbIntent.`package` = "com.facebook.katana"
                fbIntent.putExtra(Intent.EXTRA_TEXT, Message)
                try {
                    startActivity(fbIntent)


                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "Facebook is not installed on your phone", Toast.LENGTH_SHORT)
                    objUsefullData.showMsgOnUI("Facebook is not installed on your phone");

                }

            }
        }

        twitter.setOnClickListener {

            if (isNetworkConnected) {


                val twitterIntent = Intent(Intent.ACTION_SEND)
                twitterIntent.type = "text/plain"
                twitterIntent.`package` = "com.twitter.android"
                twitterIntent.putExtra(Intent.EXTRA_TEXT, Message)
                try {
                    startActivity(twitterIntent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "Twitter is not installed on your phone", Toast.LENGTH_SHORT)
                    objUsefullData.showMsgOnUI("Twitter is not installed on your phone");

                }
            }
        }

        linkedin.setOnClickListener {
            if (isNetworkConnected) {
                val linkedinIntent = Intent(Intent.ACTION_SEND)
                linkedinIntent.type = "text/plain"
                linkedinIntent.`package` = "com.linkedin.android"
                linkedinIntent.putExtra(Intent.EXTRA_TEXT, Message)
                try {
                    startActivity(linkedinIntent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "LinkedIn is not installed on your phone", Toast.LENGTH_SHORT)
                    objUsefullData.showMsgOnUI("LinkedIn is not installed on your phone");

                }
            }
        }

        whatsapp.setOnClickListener {
            if (isNetworkConnected) {
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.`package` = "com.whatsapp"
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, Message)
                try {
                    startActivity(whatsappIntent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "Whatsapp is not installed on your phone", Toast.LENGTH_SHORT)
                    objUsefullData.showMsgOnUI("Whatsapp is not installed on your phone");

                }
            }
        }

        googleplus.setOnClickListener {
            if (isNetworkConnected) {
                val googlePlusIntent = Intent(Intent.ACTION_SEND)
                googlePlusIntent.type = "text/plain"
                googlePlusIntent.`package` = "com.google.android.apps.plus"
                googlePlusIntent.putExtra(Intent.EXTRA_TEXT, Message)
                try {
                    startActivity(googlePlusIntent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "Google Plus is not installed on your phone", Toast.LENGTH_LONG)
                    objUsefullData.showMsgOnUI("Google Plus is not installed on your phone");
                }
            }

        }

        instagram.setOnClickListener {
            if (isNetworkConnected) {
                //objUsefullData.showMsgOnUI("Instagram")
                val instagramIntent = Intent(Intent.ACTION_SEND)


                val f = File(Environment.getExternalStorageDirectory() , "saleslinelogo.png")
                if (!f.exists()) {
                    try {


                        val fis = context.getAssets().open("saleslinelogo.png")
                        val buffer = ByteArray(1024)
                        //fis.read(buffer)
                        //fis.close()
                        var read = 0
                        val fos = FileOutputStream(f)

                        while (read != -1) {
                            read = fis.read(buffer)
                            if (read != -1)
                                fos.write(buffer, 0, read)
                        }
                        //fos.write(buffer)
                        fos.close()

                        Log.e("File written","Written")
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                }
                val uri = Uri.fromFile(f)

                instagramIntent.type = "image/*"
                instagramIntent.`package` = "com.instagram.android"
                instagramIntent.putExtra(Intent.EXTRA_STREAM, uri)


                try {
                    startActivity(instagramIntent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, " not installed.", Toast.LENGTH_SHORT)
                    objUsefullData.showMsgOnUI("Instagram is not installed on your phone");

                }
            }

        }


    }


    private fun sendDataToServer() {


        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("UserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))
        paramObject.put("ClientId", Integer.parseInt(objSaveData.getString(ConstantValue.CLIENT_ID)))

        paramObject.put("Name", et_Name.text.toString())
        paramObject.put("Email", et_Email.text.toString())
        paramObject.put("Phone", et_Phone.text.toString())
        paramObject.put("Company", et_Company.text.toString())

        paramObject.put("CreatedUserId", Integer.parseInt(objSaveData.getString(ConstantValue.USER_ID)))

        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val call = apiEndpointInterface!!.mSaveRecommendAppDetails(body)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                objUsefullData.dismissProgress()
                try {


                    Log.d("URL", "=====" + response!!.raw().request().url())
                    if (response.isSuccessful) {

                        UsefullData.Log("=============" + response!!.body())


                        var array = JSONArray(response!!.body().toString())

                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }

                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Inserted")


                        } else {
                            objUsefullData.showMsgOnUI("Insert failed")
                        }

                    } else {

                        UsefullData.Log("========" + response!!.code())
                        objUsefullData.showMsgOnUI(activity.resources.getString(R.string.server_error))

                    }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {

                objUsefullData.dismissProgress()
                UsefullData.Log("=============" + t)
                objUsefullData. getThrowableException(t)

            }

        })


    }


    private fun checkValidation(): Boolean {
        if (objValidation.checkEmpty(et_Name, "Name ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Email, "Email ")) {
            return false
        }
        if (!objValidation.checkForEmail(et_Email, "Email ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Phone, "Phone ")) {
            return false
        }

        if (!objValidation.checkForPhone(et_Phone, "Phone ")) {
            return false
        }
        if (objValidation.checkEmpty(et_Company, "Company ")) {
            return false
        }

        return true
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Recommend_Fragment {
            val fragment = Recommend_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
