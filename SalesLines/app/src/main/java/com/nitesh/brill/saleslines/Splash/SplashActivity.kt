package com.nitesh.brill.saleslines.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.brill.nitesh.punjabpool.Common.BaseActivity
import com.nitesh.brill.saleslines.Authenticate.Login_Activity
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.R
import com.nitesh.brill.saleslines._AGM_Classes.AGM_Activity.AGM_Home_Activity
import com.nitesh.brill.saleslines._ASM_Classes.ASM_Activity.ASM_Home_Activity
import com.nitesh.brill.saleslines._GM_Classes.GM_Activity.GM_Home_Activity
import com.nitesh.brill.saleslines._Manager_Classes.Manager_Activity.Manager_Home_Activity
import com.nitesh.brill.saleslines._User_Classes.User_Activity.User_Home_Activity


class SplashActivity : BaseActivity() {
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        objSaveData.saveBoolean(ConstantValue.CHECK_FLAG, true)
        objSaveData.save("checkProfileImage","yes")

        // Define the code block to be executed
//
//        var runnableCode = object : Runnable {
//
//
//            override fun run() {
//                // Do something here on the main thread
//
//                // Repeat this the same runnable code block again another 2 seconds
//                // 'this' is referencing the Runnable object
//                handler.postDelayed(this, 1000)
//                Log.d("Handlers", "Called on main thread")
//
//                if (isNetworkCheck) {
////                    objUsefullData
////                            .showMsgOnUI("No Internet Connection")
//                    Log.d("Handlers", "Connected")
//
//                     callMethod()
//                }
//
//            }
//
//            private fun callMethod() {
//                if (isNetworkCheck) {
//
//                    handler.removeCallbacks(runnableCode);
//
//                }
//            }
//
//        }
//        // Start the initial runnable task by posting through the handler
//        handler.post(runnableCode)


        callHomeActivity()


        // UsefullData.Log("=========regId===========" + objSaveData.getString(ConstantValue.FIREBASEID))


    }


    private fun callHomeActivity() {

        //==========Check network connect ===========\\

        if (isNetworkCheck) {

            // var intent: Intent? = null

            //================Check User already login===============\\

            if (objSaveData.getString(ConstantValue.USER_LOGIN).equals("1", ignoreCase = true)) {

                //===============if already salesman login==============\\

                if (objSaveData.getString(ConstantValue.ROLE_ID).equals("5", ignoreCase = true)) {
                    objSaveData.save("userRole", "user")
                    var intent = Intent(this@SplashActivity, User_Home_Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else if (objSaveData.getString(ConstantValue.ROLE_ID).equals("4", ignoreCase = true)) {
                    objSaveData.save("userRole", "manager")
                    var intent = Intent(this@SplashActivity, Manager_Home_Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else if (objSaveData.getString(ConstantValue.ROLE_ID).equals("3", ignoreCase = true)) {
                    objSaveData.save("userRole", "asm")
                    var intent = Intent(this@SplashActivity, ASM_Home_Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else if (objSaveData.getString(ConstantValue.ROLE_ID).equals("2", ignoreCase = true)) {
                    objSaveData.save("userRole", "agm")
                    var intent = Intent(this@SplashActivity, AGM_Home_Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else if (objSaveData.getString(ConstantValue.ROLE_ID).equals("1", ignoreCase = true)) {
                    objSaveData.save("userRole", "gm")
                    var intent = Intent(this@SplashActivity, GM_Home_Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else if (objSaveData.getString(ConstantValue.ROLE_ID).equals("0", ignoreCase = true)) {
                    // intent = Intent(this@SplashActivity, ASM_Home_Activity::class.java)
                }
            } else {

                var intent = Intent(this@SplashActivity, Login_Activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            finish()


        }

    }

    override fun onRestart() {
        super.onRestart()
        callHomeActivity()
    }

    override fun onResume() {
        super.onResume()
        callHomeActivity()
    }

}



