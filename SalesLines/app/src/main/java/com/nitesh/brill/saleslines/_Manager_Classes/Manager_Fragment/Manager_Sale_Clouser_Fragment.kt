package com.nitesh.brill.saleslines._Manager_Classes.Manager_Fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_user_sale_clouser.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Manager_Sale_Clouser_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mParam3: String? = null
    private var mParam4: String? = null
    private var mParam5: String? = null
    var ImageName_PC: String? = null
    var Img_PC: String = ""
    private var userfile_profile: File? = null

    private val GALLERY_REQUEST = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
            mParam3 = arguments.getString(ARG_PARAM3)
            mParam4 = arguments.getString(ARG_PARAM4)
            mParam5 = arguments.getString(ARG_PARAM5)
        }
    }

    // ==============================================================//

    internal var PERMISSION_ALL = 1
    internal var PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    // ==============================================================//
    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_sale_clouser, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_AddEnqNo.setText(mParam4)
        et_Products_sold.setText(mParam5)





        et_EmiDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if( !et_EmiDate.text.isEmpty())
                {
                    if (Integer.parseInt("" + s) > 240) {
                        et_EmiDate.setError("EMI value can not exceed 240")
                    }
                }
            }


        })

        //===============================\\
        et_PaymentMode.setOnClickListener {

            val List = activity.resources.getStringArray(R.array.payment_mode)
            mShowAlertDialog(et_PaymentMode, List, "PaymentMode")


        }

        //===============================\\
        et_PaymentType.setOnClickListener {
            val List = activity.resources.getStringArray(R.array.OTP)
            mShowAlertDialog(et_PaymentType, List, "Payment Type")

        }

        //===============================\\
        et_Delivery_Status.setOnClickListener {
            val List = activity.resources.getStringArray(R.array.del_awt)
            mShowAlertDialog(et_Delivery_Status, List, "Delivery Status")

        }

        //===============================\\
        btn_Upload.setOnClickListener {


        }

        //===============================\\
        btn_Closec.setOnClickListener {

            if (checkValidation()) {
                if (isNetworkConnected)
                mSaveSaleClosedDeatails()
            }
        }


        //===================================\\

        iv_Document.setOnClickListener {

            if (!hasPermissions(activity, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
                userfile_profile = objUsefullData
                        .createFile("userfile.png")
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, GALLERY_REQUEST)
            } else {
                userfile_profile = objUsefullData
                        .createFile("userfile.png")
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, GALLERY_REQUEST)
            }


        }
    }


    private fun mSaveSaleClosedDeatails() {


        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("LeadId", mParam1)
        paramObject.put("DemoId", mParam2)
        paramObject.put("EnquiryId", mParam3)
        paramObject.put("ProductSold", et_Products_sold.text.toString())
        paramObject.put("PaymentMode", et_PaymentMode.text.toString())
        paramObject.put("PaymentType", et_PaymentType.text.toString())
        paramObject.put("TotalSalesValue", et_TotalSaleValue.text.toString())
        paramObject.put("EmiDates", et_EmiDate.text.toString())
        paramObject.put("ModeOfDelivery", et_Delivery_Status.text.toString())
        paramObject.put("RecieptNo", et_ReciptNumber.text.toString())
        paramObject.put("ImageName_PC", ImageName_PC)
        paramObject.put("ContentType_PC", "jpg"!!)
        paramObject.put("Data_PC", Img_PC)

        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val mCall = apiEndpointInterface!!.mSaveSaleClosedDeatails(body)

        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())

                UsefullData.Log("User_Response    " + response.body().toString())


                var array = JSONArray(response!!.body().toString())


                var success=""
                for (i in 0..(array.length() - 1)) {
                    val item = array.getJSONObject(0)
                    success = item.optString("Success")

                }

                if (success.equals("1")) {
                    //objUsefullData.showMsgOnUI("Successfully Saved")

                    closedLead()

                } else {
                    objUsefullData.showMsgOnUI("Save failed")
                }

            }

        })

    }

    private fun closedLead() {

        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("Status", "Closed")
        paramObject.put("Eid", mParam2)

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val mCall = apiEndpointInterface!!.mUpdateEnqStatusClose(""+mParam2,body)

        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())

                UsefullData.Log("User_Response    " + response.body().toString())
                UsefullData.Log("User_Response    " + response.body().toString())
                var array = JSONArray(response!!.body().toString())

                var success=""

                for (i in 0..(array.length() - 1)) {
                    val item = array.getJSONObject(0)

                    UsefullData.Log("===" + item)
                    success = item.optString("Success")

                }



                if (success.equals("1")) {
                    objUsefullData.showMsgOnUI("Successfully Saved")


                }else{
                    objUsefullData.showMsgOnUI("Save failed")
                }



            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY_REQUEST -> if (Activity.RESULT_OK == resultCode) {
                val picturePath = getPath(data!!.data)

                userfile_profile = File(picturePath)

                val imgBmp = BitmapFactory.decodeFile((picturePath))


                iv_Document!!.setImageBitmap(imgBmp)

                reCreateFile(imgBmp)


            }
        }
    }

    // ==========================================//
    fun getPath(uri: Uri): String {
        val selectedImage = uri
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.contentResolver.query(selectedImage,
                filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        return picturePath
    }

    private fun reCreateFile(_bitmapScaled: Bitmap) {
        val scaled = Bitmap.createScaledBitmap(_bitmapScaled, 100, 100, true)
        try {


            var baos = ByteArrayOutputStream()
            val scaled = Bitmap.createScaledBitmap(_bitmapScaled, 100, 100,
                    true)

            val out = FileOutputStream(userfile_profile)
            scaled.compress(Bitmap.CompressFormat.JPEG, 50, out)


            scaled.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            UsefullData.Log("User File :: " + userfile_profile!!.getPath())

            var path = userfile_profile!!.getPath()
            ImageName_PC = path.substring(path.lastIndexOf("/") + 1);
            Img_PC = Base64.encodeToString(getBytesFromBitmap(_bitmapScaled),
                    Base64.NO_WRAP);

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    //===============================================\\


    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {


        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        return baos.toByteArray();
    }

    private fun mShowAlertDialog(sp_Next_Itrection_By: EditText?, animals: Array<out String>?, s: String) {

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle(s)


        dialogBuilder.setItems(animals, DialogInterface.OnClickListener { dialog, item ->

            sp_Next_Itrection_By!!.setText(animals!![item].toString())
            sp_Next_Itrection_By.setError(null)
            sp_Next_Itrection_By


        })

        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()


    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"
        private val ARG_PARAM4 = "param4"
        private val ARG_PARAM5 = "param5"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, param3: String, s: String, s1: String): Manager_Sale_Clouser_Fragment {
            val fragment = Manager_Sale_Clouser_Fragment()

            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            args.putString(ARG_PARAM4, s)
            args.putString(ARG_PARAM5, s1)
            fragment.arguments = args
            return fragment
        }
    }

    private fun checkValidation(): Boolean {






        if (objValidation.checkEmpty(et_Products_sold, "Products_sold ")) {

            et_Products_sold.requestFocus()
            return false
        }

        if (et_PaymentMode.text.isEmpty()) {

            objUsefullData.showMsgOnUI("Payment Mode should not empty")
            et_PaymentMode.requestFocus()
            mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }
        if (et_PaymentType.text.isEmpty()) {

            objUsefullData.showMsgOnUI("Payment Type should not empty")
            et_PaymentType.requestFocus()
            mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }
        if (et_TotalSaleValue.text.isEmpty()) {

            objUsefullData.showMsgOnUI("TotalSaleValue should not empty")
            et_TotalSaleValue.requestFocus()
            mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }


        if (objValidation.checkEmpty(et_EmiDate, "EmiDate ")) {

            et_EmiDate.requestFocus()
            return false
        }



        if (et_Delivery_Status.text.isEmpty()) {

            objUsefullData.showMsgOnUI("Delivery_Status should not empty")
            et_Delivery_Status.requestFocus()
            mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }


        if (objValidation.checkEmpty(et_ReciptNumber, "ReciptNumber ")) {

            et_ReciptNumber.requestFocus()
            return false
        }

        if (Img_PC.isEmpty()) {

            objUsefullData.showMsgOnUI("Document should not empty")

            return false
        }
        return true
    }
}// Required empty public constructor