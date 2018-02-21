package com.nitesh.brill.saleslines.Common_Files


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.CountDownTimer
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.github.mrengineer13.snackbar.SnackBar
import com.nitesh.brill.saleslines.R
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UsefullData(private val _context: Context) : ConstantValue {
    private var pDialog: ProgressDialog? = null
    private var countDownTimer: CountDownTimer? = null

    var time = ""
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mAMPM: Int = 0
    var objSaveData: SaveData = SaveData(_context)

    // ==================================//
    val deviceId: String
        @SuppressLint("MissingPermission")
        get() {

            var deviceId = ""

            val telephonyManager = _context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager != null) {
                deviceId = telephonyManager.deviceId
            } else {
                deviceId = Settings.Secure.getString(_context.contentResolver,
                        Settings.Secure.ANDROID_ID)
            }
            Log("Your Device Id :" + deviceId)
            return deviceId
        }


    // ================== CREATE FILE AND RELATED ACTION ============//

    val rootFile: File
        get() {

            val f = File(Environment.getExternalStorageDirectory(), "PunjabPool")
            if (!f.isDirectory) {
                f.mkdirs()
            }

            return f
        }

    fun deleteRootDir(root: File) {

        if (root.isDirectory) {
            val children = root.list()
            for (i in children!!.indices) {
                val f = File(root, children[i])
                Log("file name:" + f.name)
                if (f.isDirectory) {
                    deleteRootDir(f)
                } else {
                    f.delete()
                }
            }
        }
    }

    fun createFile(fileName: String): File {
        var f: File? = null
        try {
            f = File(rootFile, fileName)
            if (f.exists()) {
                f.delete()
            }

            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return f!!
    }


    //==========================================================\\

    fun getImagePathFromDrawble(imageView: ImageView): String {

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        //always save as
        val fileName = System.currentTimeMillis().toString() + ".jpg"

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes)

        val ExternalStorageDirectory = Environment.getExternalStorageDirectory()
        val file = File(ExternalStorageDirectory.toString() + File.separator + fileName)

        var fileOutputStream: FileOutputStream? = null
        try {
            file.createNewFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())

        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            }
        }


        return ""

    }


    //====================================================\\

    fun getNameFromURL(url: String?): String {

        var fileName = "item_image.jpg"
        if (url != null) {
            fileName = url.substring(url.lastIndexOf('/') + 1, url.length)
        }
        return fileName
    }

    fun showMsgOnUI(msg: String) {
        (_context as Activity).runOnUiThread { Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show() }

    }


    val isNetworkConnected: Boolean
        get() {
            val cm = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            if (ni == null && !ni!!.isConnected) {
                Toast.makeText(_context, "No Internet Connection.", Toast.LENGTH_LONG).show()
                return false
            } else
                return true

        }


    // ==================== PROGRESS DIALOG ==================//

    fun showProgress(msg: String, title: String) {


        (_context as Activity).runOnUiThread {
            if (pDialog != null) {
                if (pDialog!!.isShowing) {
                    pDialog!!.cancel()
                    pDialog = null
                }
            }
            //       pDialog = ProgressDialog.show(_context, title, msg, true)
            pDialog = ProgressDialog.show(_context, title, msg, true, false)

        }


    }

    fun dismissProgress() {
        try {
            if (pDialog != null) {
                if (pDialog!!.isShowing) {
                    pDialog!!.cancel()

                }
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

    }

    fun showSnackBar(msg: String) {
        SnackBar.Builder((_context as Activity))

                .withMessage(msg) // OR

                .withTextColorId(R.color.white)
                .withBackgroundColorId((R.color.red_))


                .withDuration(SnackBar.MED_SNACK)
                .show();
    }


    // ==================== timer countdown  ==================//
    fun count_down_Timer(startTime: Long, interval: Long) {
        countDownTimer = MyCountDownTimer(startTime, interval)

        countDownTimer!!.start()
    }

    inner class MyCountDownTimer(startTime: Long, interval: Long) : CountDownTimer(startTime, interval) {


        override fun onFinish() {


        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    // ==================== HIDE KEYBOARED ==================//
    fun hideKeyBoared() {

        val imm = _context
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

    }

    // ====================SET FONT SIZE==================//
    val usedFontLucida: Typeface
        get() {
            val typeFace = Typeface.createFromAsset(_context.assets,
                    "fonts/lucida_hand_writting.ttf")
            return typeFace
        }

    val usedFontArial: Typeface
        get() {
            val typeFace = Typeface.createFromAsset(_context.assets,
                    "fonts/arial.ttf")
            return typeFace
        }


    val wifiName: String
        get() {

            var ssid: String? = null
            val connManager = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (networkInfo.isConnected) {
                val wifiManager = _context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val connectionInfo = wifiManager.connectionInfo
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.ssid)) {
                    ssid = connectionInfo.ssid
                }
            }
            return ssid!!

        }


    fun get_Time(h: Int, m: Int): String {
        var status = "AM"

        if (h > 11) {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status = "PM"
        }

        // Initialize a new variable to hold 12 hour format hour value
        val hour_of_12_hour_format: Int

        if (h > 11) {

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format = h - 12


        } else {
            hour_of_12_hour_format = h
        }
        if (hour_of_12_hour_format < 10) {

        }


        val time: String;
        if (hour_of_12_hour_format < 10) {


            if (m < 9) {
                time = "0" + hour_of_12_hour_format + ":0" + m + " " + status
            } else {
                time = "0" + hour_of_12_hour_format + ":" + m + " " + status
            }

        } else {
            if (m < 9) {
                time = "" + hour_of_12_hour_format + ":0" + m + " " + status
            } else {
                time = "" + hour_of_12_hour_format + ":" + m + " " + status
            }

        }
        return time;

        //tv.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
    }


    fun getTimediffrence(string1: String, string2: String): String {
        var diffMinutes: Long = 0
        var diffHours = 0f

        var time1: Date? = null
        try {
            time1 = SimpleDateFormat("HH:mm aa").parse(string1)
            val calendar1 = Calendar.getInstance()
            calendar1.time = time1


            val time2 = SimpleDateFormat("HH:mm aa").parse(string2)
            val calendar2 = Calendar.getInstance()
            calendar2.time = time2
            calendar2.add(Calendar.DATE, 1)

            val x = calendar1.time
            val xy = calendar2.time
            val diff = x.time - xy.time
            diffMinutes = diff / (60 * 1000)
            diffHours = (diffMinutes / 60).toFloat()
            println("diff hours" + diffHours)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        return diffHours.toString() + "" + diffMinutes


    }


    //====================================\\
    fun getFileType(name: String, bitmap: Bitmap): File {


        val imageFile = File(name + ".png")

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, "Error writing bitmap", e)
        }

        return imageFile
    }

    fun getBytesFromBitmap(uri: Uri): ByteArray {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(_context.contentResolver.openInputStream(uri))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val stream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 70, stream)
        return stream.toByteArray()
    }


    companion object {

        // ================== DEVICE INFORMATION ============//

        val countryCodeFromDevice: String
            get() {
                var countryCode = Locale.getDefault().country
                if (countryCode == "") {
                    countryCode = "IN"
                }
                return countryCode
            }

        // ================== GET TIME AND DATE ============//

        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        // sdf.applyPattern("dd MMM yyyy");
        val dateTime: String
            @SuppressLint("SimpleDateFormat")
            get() {
                val sdf = SimpleDateFormat("dd MMM yyyy hh:mm aa")
                val cal = Calendar.getInstance()
                val strDate = sdf.format(cal.time)
                return strDate
            }

        @SuppressLint("SimpleDateFormat")
        fun getDate(time: String): String {

            val sdf = SimpleDateFormat("HH:mm:ss")

            val startDate = Date(time)
            val strDate = sdf.format(startDate.time)
            return strDate
        }


        // sdf.applyPattern("dd MMM yyyy");
        val currentTime: String
            @SuppressLint("SimpleDateFormat")
            get() {
                val sdf = SimpleDateFormat("HH:mm aa")

                val cal = Calendar.getInstance()
                val strTime = sdf.format(cal.time)
                return strTime

            }

        // ================== LOG AND TOAST ====================//

        fun Log(veryLongString: String) {

            if (ConstantValue.SHOW_LOG) {

                val maxLogSize = 1000
                for (i in 0..veryLongString.length / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > veryLongString.length) veryLongString.length else end
                    Log.e("SaleLine", veryLongString.substring(start, end))
                }

            }

        }


        fun printValues(veryLongString: String) {
            if (ConstantValue.SHOW_LOG) {

                val maxLogSize = 1000
                for (i in 0..veryLongString.length / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > veryLongString.length) veryLongString.length else end
                    Log.e("SaleLine", veryLongString.substring(start, end))
                }

            }
        }


        fun takeScreenShot(activity: Activity): Bitmap {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val b1 = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top


            val width = activity.windowManager.defaultDisplay.width
            val height = activity.windowManager.defaultDisplay.height


            val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
            view.destroyDrawingCache()
            return b
        }


    }


    fun getNextInteractionDatePickerOn(et_NextInteractionDate: EditText): String {
        var time: String = "h"
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(_context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    //txtDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)

                    // Get Current Time
                    val c = Calendar.getInstance()
                    mHour = c.get(Calendar.HOUR_OF_DAY)
                    mMinute = c.get(Calendar.MINUTE)
                    mAMPM = c.get(Calendar.AM_PM)
                    // Launch Time Picker Dialog
                    val timePickerDialog = TimePickerDialog(_context,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                et_NextInteractionDate.setError(null)

                                et_NextInteractionDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year + " " + get_Time(hourOfDay, minute))


                                et_NextInteractionDate.setText(setFormat((monthOfYear + 1).toString()) + "/" + setFormat(dayOfMonth.toString()) + "/" + year + " " + get_Time(hourOfDay, minute))

                            }, mHour, mMinute, false)


                    timePickerDialog.show()


                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()



        return time

    }

    private fun setFormat(mString: String): String {

        var correctData = ""

        if (mString.length < 2) {

            correctData = "0" + mString
        } else {
            correctData = mString
        }

        return correctData
    }


    fun getDatePicker(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            et_DemoDate.setText(sdf.format(myCalendar.time))
        }
        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        et_DemoDate.setError(null)
        return time


    }

    fun getDatePickerTo(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                objSaveData.save("toDateFilter", sdf.format(myCalendar.time))
                et_DemoDate.setText(sdf.format(myCalendar.time))


        }
        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        et_DemoDate.setError(null)
        return time


    }

    fun getDatePickerDemo(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                objSaveData.save("demoDateFilter", sdf.format(myCalendar.time))
                et_DemoDate.setText(sdf.format(myCalendar.time))

        }
        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        et_DemoDate.setError(null)
        return time


    }

    fun getDatePickerFrom(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                objSaveData.save("fromDateFilter", sdf.format(myCalendar.time))
                et_DemoDate.setText(sdf.format(myCalendar.time))

        }
        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        et_DemoDate.setError(null)
        return time


    }


    fun getDatePickerUpdate(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            et_DemoDate.setText(sdf.format(myCalendar.time))
        }

        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        et_DemoDate.setError(null)


        return time


    }

    fun getDemoDatePicker(et_DemoDate: EditText): String {

        val myCalendar = Calendar.getInstance()


        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            if (isPastDay((myCalendar.time)) as Boolean) {
                et_DemoDate.setText(sdf.format(myCalendar.time))

            }
        }

        DatePickerDialog(_context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

        et_DemoDate.setError(null)


        return time


    }


    private fun isPastDay(date: Date?): Any {

        val c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        val today = c.getTime();

        // test your condition, if Date specified is before today
        if (date != null) {
            if (date.before(today)) {

                showMsgOnUI("Please select correct date")
                return false;
            }
        }
        UsefullData.Log("==========" + date)
        return true;


    }

    fun dpToPx(dp: Int): Int {
        val density = _context.getResources()
                .getDisplayMetrics()
                .density
        return Math.round(dp.toFloat() * density)
    }


    fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0

        for (i in 0..spinner.count) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }

        }
        return index

    }

    fun getError(mString: String) {


        UsefullData.Log("====getException====" + mString)
        //showMsgOnUI(_context.resources.getString(R.string.server_not_working))

//        if (mString.equals("Software caused connection abort")) {
//
//            UsefullData.Log("Software caused connection abort")
//
//            showMsgOnUI(_context.resources.getString(R.string.internet_error))
//
//        } else if (mString.equals("Failed to connect to /console.salelinecrm.com:80")) {
//
//            UsefullData.Log("Failed to connect to /console.salelinecrm.com:80")
//
//            showMsgOnUI(_context.resources.getString(R.string.internet_error))
//
//        } else {
//            showMsgOnUI(_context.resources.getString(R.string.server_error))
//        }

    }

    fun getException(mException: Exception) {

       // showMsgOnUI(_context.resources.getString(R.string.server_not_working))

        UsefullData.Log("====getException====" + mException)


    }
    fun getThrowableException(mException: Throwable?) {

        //showMsgOnUI(_context.resources.getString(R.string.server_not_working))

        UsefullData.Log("====getException====" + mException)



    }



}
