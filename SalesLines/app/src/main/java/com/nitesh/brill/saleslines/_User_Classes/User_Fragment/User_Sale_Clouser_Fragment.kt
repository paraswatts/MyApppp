package com.nitesh.brill.saleslines._User_Classes.User_Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.intentfilter.androidpermissions.PermissionManager
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
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
import java.io.IOException


class User_Sale_Clouser_Fragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mParam3: String? = null
    private var mParam4: String? = null
    private var mParam5: String? = null
    var ImageName_PC: String? = null
    var Img_PC: String = ""
    private var userfile_profile: File? = null

    var emi = 0
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


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user_sale_clouser, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissionManager = PermissionManager.getInstance(activity)

        et_EmiDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {

                    if (!et_EmiDate.text.isEmpty()) {
                        if (Integer.parseInt("" + s) > 240) {
                            et_EmiDate.setError("EMI value can not exceed 240")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }


        })

        et_Products_sold.getBackground().clearColorFilter();
        et_AddEnqNo.setText(mParam4)
        et_Products_sold.setText(mParam5)
        et_AddEnqNo.isEnabled = false
        et_Products_sold.isEnabled = false


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

            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {

                    userfile_profile = objUsefullData
                            .createFile("userfile.png")
                    val i = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(i, GALLERY_REQUEST)
                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }


    private fun mSaveSaleClosedDeatails() {


        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("LeadId", mParam1)
        paramObject.put("DemoId", mParam3)
        paramObject.put("EnquiryId", mParam2)
        paramObject.put("ProductSold", et_Products_sold.text.toString())
        paramObject.put("PaymentMode", et_PaymentMode.text.toString())
        paramObject.put("PaymentType", et_PaymentType.text.toString())
        paramObject.put("TotalSalesValue", et_TotalSaleValue.text.toString())
        paramObject.put("EmiDates", et_EmiDate.text.toString())
        paramObject.put("ModeOfDelivery", et_Delivery_Status.text.toString())
        paramObject.put("RecieptNo", et_ReciptNumber.text.toString())
        paramObject.put("ImageName", ImageName_PC)
        paramObject.put("ContentType", "jpg"!!)
        paramObject.put("Data", Img_PC)
        paramObject.put("UserId", objSaveData.getString(ConstantValue.USER_ID))
        paramObject.put("ClientId", objSaveData.getString(ConstantValue.CLIENT_ID))


        UsefullData.Log("==================" + paramObject.toString())


        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
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

                try {

                if (response.isSuccessful) {


                        UsefullData.Log("User_Response    " + response.body().toString())
                        var array = JSONArray(response!!.body().toString())

                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                        }
                        if (success.equals("1")) {
                            // objUsefullData.showMsgOnUI("Successfully Saved")
                            closedLead()

                        } else {
                            objUsefullData.showMsgOnUI("Save failed")
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

    }


    private fun closedLead() {

        objUsefullData.showProgress("Please Wait...", "")
        val paramObject = JSONObject()
        paramObject.put("Status", "Closed")
        paramObject.put("Eid", mParam2)
        UsefullData.Log("==========closedLead========" + paramObject.toString())
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
        val mCall = apiEndpointInterface!!.mUpdateEnqStatusClose("" + mParam2, body)

        mCall.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                objUsefullData.dismissProgress()
                objUsefullData. getThrowableException(t)
                UsefullData.Log("onFailure ===" + t)
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                objUsefullData.dismissProgress()
                Log.d("URL", "===" + response!!.raw().request().url())
                try {
                if (response.isSuccessful) {

                        UsefullData.Log("User_Response    " + response.body().toString())
                        var array = JSONArray(response!!.body().toString())

                        var success = ""
                        for (i in 0..(array.length() - 1)) {
                            val item = array.getJSONObject(0)

                            UsefullData.Log("===" + item)
                            success = item.optString("Success")

                            //===================================\\
                            et_AddEnqNo.setText("")
                            et_Products_sold.setText("")
                            et_PaymentMode.setText("")
                            et_PaymentType.setText("")
                            et_TotalSaleValue.setText("")
                            et_EmiDate.setText("")
                            et_Delivery_Status.setText("")
                            et_ReciptNumber.setText("")

                            var fragment = User_Home_Fragment.newInstance("", "" + objSaveData.getString(ConstantValue.USER_ID))
                            val fragmentManager = activity.supportFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right).addToBackStack(null)
                            fragmentTransaction.replace(R.id.content_frame, fragment)
                            fragmentTransaction.commit()

                            // iv_Document.background = null
                            //btn_Upload.visibility = View.INVISIBLE


                        }

                        if (success.equals("1")) {
                            objUsefullData.showMsgOnUI("Successfully Sale Closed")


                        } else {
                            objUsefullData.showMsgOnUI("Sale Closed failed")
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

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GALLERY_REQUEST -> if (Activity.RESULT_OK == resultCode) {

                try {
                    // When an Image is picked

                    val picturePath = getPath(data!!.data)

                    userfile_profile = File(picturePath)

                    var length = userfile_profile!!.length()
                    length = length/1024

//                    if(length>200)
//                    {
//                        objUsefullData.showMsgOnUI("Document size exceeds 200KB")
//
//                    }
//                    else {
//

                        val imgBmp = BitmapFactory.decodeFile((picturePath))


                        iv_Document!!.setImageBitmap(imgBmp)

                        reCreateFile(imgBmp)
                   // }

                } catch (e: Exception) {

                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                    UsefullData.Log("================" + e.printStackTrace())
                }
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

        try {


            var baos = ByteArrayOutputStream()
            val scaled = Bitmap.createScaledBitmap(_bitmapScaled, 400, 400,
                    true)



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
        fun newInstance(param1: String, param2: String, param3: String, s: String, s1: String): User_Sale_Clouser_Fragment {
            val fragment = User_Sale_Clouser_Fragment()

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


        if (objValidation.checkEmpty(et_Products_sold, "Products Sold ")) {

            //et_Products_sold.requestFocus()
            return false
        }



        if (objValidation.checkEmpty(et_PaymentMode, "Payment Mode ")) {

            objUsefullData.showMsgOnUI("Payment Mode should not empty")
            et_PaymentMode.requestFocus()
            // mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }

        if (objValidation.checkEmpty(et_PaymentType, "Payment Type ")) {

            objUsefullData.showMsgOnUI("Payment Type should not empty")
            et_PaymentType.requestFocus()
            //   mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }
        if (objValidation.checkEmpty(et_TotalSaleValue, "Total Sale Value ")) {

            objUsefullData.showMsgOnUI("Total Sale Value should not empty")
            et_TotalSaleValue.requestFocus()
            //  mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }


        if (objValidation.checkEmpty(et_EmiDate, "Emi Date ")) {

            objUsefullData.showMsgOnUI("Emi Date  should not empty")
            et_EmiDate.requestFocus()
            return false
        }



        if (objValidation.checkEmpty(et_Delivery_Status, " Delivery Status")) {

            objUsefullData.showMsgOnUI("Delivery Status should not empty")
            et_Delivery_Status.requestFocus()
            //  mScrollView.fullScroll(ScrollView.FOCUS_UP)
            return false
        }


        if (objValidation.checkEmpty(et_ReciptNumber, "Receipt Number ")) {

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
