package com.nitesh.brill.saleslines.Splash

import android.app.Application
import android.os.StrictMode
import com.nitesh.brill.saleslines.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig





/**
 * Created by Nitesh Android on 25-07-2017.
 */

class App : Application() {

    private var mInstance: App? = null
    override fun onCreate() {
        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        mInstance = this

    }




}
