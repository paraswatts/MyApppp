package com.nitesh.brill.saleslines.Common_Files

import android.content.Context
import android.graphics.Typeface
import android.util.Log

/**
 * Created by Nitesh Android on 25-07-2017.
 */

object TypefaceUtil {
    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * *
     * @param defaultFontNameToOverride for example "monospace"
     * *
     * @param customFontFileNameInAssets file name of the font from assets
     */
    fun overrideFont(context: Context, defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
        try {
            val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

            val defaultFontTypefaceField = Typeface::class.java!!.getDeclaredField(defaultFontNameToOverride)
            defaultFontTypefaceField.setAccessible(true)
            defaultFontTypefaceField.set(null, customFontTypeface)
        } catch (e: Exception) {
           Log.d("", "Can not set custom font $customFontFileNameInAssets instead of $defaultFontNameToOverride")
        }

    }
}
