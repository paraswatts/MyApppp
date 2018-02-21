package com.nitesh.brill.saleslines.Common_Fragment

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brill.nitesh.punjabpool.Common.BaseFragment
import com.google.gson.JsonElement
import com.intentfilter.androidpermissions.PermissionManager
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.UpdateProfilePicture
import com.nitesh.brill.saleslines.Common_Files.UsefullData
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.fragment_user_profile_pic_upload.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class ProfilePic_Upload_Fragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var userfile_profile: File? = null
    var ImageName_PC: String? = null
    var ContentType_PC: String? = "jpg"
    var Img_PC: String = ""
    var Image_Path: String = ""

    var UPDATE_IMAGE:String="1"

    var selectedUri: Uri? = null
    private var userfile: File? = null

    // ==============================================================//
    val INTENT_FILTER = "INTENT_PROFILE"

    internal var PERMISSION_ALL = 1
    internal var PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

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
        val view = inflater!!.inflate(R.layout.fragment_user_profile_pic_upload, container, false)
        return view

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionManager = PermissionManager.getInstance(activity)

        //================================\\
        btn_Camera.setOnClickListener {

            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {

                    openCamera()


                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })



        }

        //================================\\
        btn_Galley.setOnClickListener {

            permissionManager.checkPermissions(mPERMISSIONS, object : PermissionManager.PermissionRequestListener {
                override fun onPermissionGranted() {

                    openImageGallery()


                }

                override fun onPermissionDenied() {
                    Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
                }
            })

        }


        //======================================\\

        btn_Upload.setOnClickListener {

            if (Img_PC.isEmpty()) {

                objUsefullData.showMsgOnUI("Document should not empty")

            } else {
                if (isNetworkConnected) {
                    objUsefullData.showProgress("Please Wait...", "")
                    val paramObject = JSONObject()
                    paramObject.put("ImageName", ImageName_PC)
                    paramObject.put("ContentType", ContentType_PC!!)

                    paramObject.put("Data", Img_PC)
                    paramObject.put("UserId", objSaveData.getString(ConstantValue.USER_ID))
                    UsefullData.Log("========paramObject==========" + paramObject.toString())


                    val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), paramObject.toString())
                    val mCall = apiEndpointInterface!!.mUploadUserProfilePic(objSaveData.getString(ConstantValue.USER_ID), body)
                    mCall.enqueue(object : Callback<JsonElement> {
                        override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {

                            objUsefullData.dismissProgress()
                            Log.d("URL", "===updatePreviousDetails==" + response!!.raw().request().url())
                            try {
                            if (response.isSuccessful) {

                                    UsefullData.Log("=============" + response!!.body().toString())

                                    var array = JSONArray(response!!.body().toString())

                                   val updateDp = activity as UpdateProfilePicture
                                   updateDp.updateDp(Image_Path)
//                                val broadCast = Intent(ConstantValue.INTENT_FILTER)
//                                broadCast.putExtra("imagePath",Image_Path)
//                                context.sendBroadcast(broadCast)


//
//                                contextsendBroadcast(broadCast)
//                                    img_Profile_Pic.setImageResource(R.drawable.ic_user)
//                        for (i in 0..(array.length() - 1)) {
//                            val item = array.getJSONArray(i)
//
//                            for (i in 0..(item.length() - 1)) {
//                                val item_ = item.getJSONObject(i)
//
//                                UsefullData.Log("===" + item_)
//                                var success = item_.optString("Success")
//
//                                if (success.equals("1")) {
//                                    objUsefullData.showMsgOnUI("Successfully Uploaded")
//                                }
//
//
//                            }
//                        }


                                    var success = ""
                                    for (i in 0..(array.length() - 1)) {
                                        val item = array.getJSONObject(0)

                                        UsefullData.Log("===" + item)
                                        success = item.optString("Success")

                                    }

                                    if (success.equals("1")) {
                                        objUsefullData.showMsgOnUI("Successfully Uploaded")
                                        objSaveData.save("checkProfileImage","yes")

                                    } else {
                                        objUsefullData.showMsgOnUI("Upload failed")
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

                            objUsefullData.dismissProgress()
                            UsefullData.Log("=============" + t)
                            objUsefullData. getThrowableException(t)

                        }

                    })


                }
            }
        }
    }


    private fun openCamera() {

        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        userfile = objUsefullData.createFile("userfile.png")
        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(userfile))
        startActivityForResult(photoCaptureIntent, CAMERA_REQUEST)
    }

    private fun openImageGallery() {
        userfile = objUsefullData
                .createFile("userfile.png")
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                selectedUri = data!!.data
                if (null != selectedUri) {
                    val path = selectedUri!!.path
                    Log.e("image path", path + "")
                    img_Profile_Pic.setImageURI(selectedUri)

                    val picturePath = getPath(selectedUri!!)
                    userfile = File(picturePath)
                    val imgBmp = BitmapFactory.decodeFile((picturePath))
                    img_Profile_Pic!!.setImageBitmap(imgBmp)
                    reCreateFile(imgBmp)

                }
            } else if (requestCode == CAMERA_REQUEST) {
                var bitmap = BitmapFactory.decodeFile(
                        userfile!!.path,
                        BitmapFactory.Options())

                var orientation = 0
                try {
                    val ei = ExifInterface(userfile!!.path)
                    orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> bitmap = rotate(bitmap!!, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> bitmap = rotate(bitmap!!, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> bitmap = rotate(bitmap!!, 270f)
                    else -> {
                        println("orientation*****case****default****" + orientation)
                        bitmap = rotate(bitmap!!, orientation.toFloat())
                    }
                }


                img_Profile_Pic!!.setImageBitmap(bitmap)
                reCreateFile(bitmap)

            }
        }

    }

    // ==========================================//
    fun rotate(src: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    // ==========================================//
    fun getPath(uri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }


    private fun reCreateFile(bitmap: Bitmap) {

        var path = userfile!!.getPath()
        Log.e("Image path",path)
        Image_Path = path
        var baos = ByteArrayOutputStream()
        val imgBmp = Bitmap.createScaledBitmap(bitmap, 400, 400,
                true)
        imgBmp.compress(Bitmap.CompressFormat.PNG, 90, baos)
        ImageName_PC = path.substring(path.lastIndexOf("/") + 1);
        Img_PC = Base64.encodeToString(getBytesFromBitmap(imgBmp!!),
                Base64.NO_WRAP);


    }

    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {


        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
        return baos.toByteArray();
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val CAMERA_REQUEST = 1
        private val GALLERY_REQUEST = 2

        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ProfilePic_Upload_Fragment {
            val fragment = ProfilePic_Upload_Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
