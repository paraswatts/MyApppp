package com.nitesh.brill.saleslines.Common_Files

/**
 * Created by Paras Android on 04-09-2017.
 */

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import android.widget.ImageView
import com.brill.nitesh.punjabpool.Common.BaseActivity
import com.jsibbold.zoomage.ZoomageView
import com.nitesh.brill.saleslines.R
import kotlinx.android.synthetic.main.customdialogimageview.*


class CustomDialogImageView : BaseActivity() {


    internal lateinit var saveData: SaveData
    private var imageView: ImageView? = null
    private val matrix = Matrix()
    private var scaleGestureDetector: ScaleGestureDetector? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.customdialogimageview)
            saveData = SaveData(this)
            imageView = findViewById(R.id.mImageView) as ImageView
            val byte_array = intent.getByteArrayExtra("byteArray")

            val bitmap =  BitmapFactory.decodeByteArray(byte_array, 0/* starting index*/, byte_array.size/*no of byte to read*/)
//            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//

          mImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 700, 700, false));
          // mImageView.setImageBitmap(bitmap);

            //scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            e.printStackTrace()
        }


    }

//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//        scaleGestureDetector!!.onTouchEvent(ev)
//        return true
//    }
//
//    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            var scaleFactor = detector.scaleFactor
//            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f))
//            matrix.setScale(scaleFactor, scaleFactor)
//            imageView!!.setImageMatrix(matrix)
//            return true
//        }
//    }

}