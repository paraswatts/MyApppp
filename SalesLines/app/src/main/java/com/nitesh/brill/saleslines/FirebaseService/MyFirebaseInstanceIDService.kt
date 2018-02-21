package com.nitesh.brill.saleslines.FirebaseService

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.nitesh.brill.saleslines.Common_Files.ConstantValue
import com.nitesh.brill.saleslines.Common_Files.SaveData

/**
 * Created by Paras-Android on 11-09-2017.
 */

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    lateinit var objSaveData: SaveData
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken)

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken)

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        val registrationComplete = Intent(Config.REGISTRATION_COMPLETE)
        registrationComplete.putExtra("token", refreshedToken)
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }

    private fun sendRegistrationToServer(token: String?) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token!!)
    }

    private fun storeRegIdInPref(token: String?) {
        objSaveData = SaveData(this)
        objSaveData.save(ConstantValue.FIREBASEID, "" + token)
    }

    companion object {

        private val TAG = MyFirebaseInstanceIDService::class.java.simpleName
    }
}
