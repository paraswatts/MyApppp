package com.nitesh.brill.saleslines.Common_Files

/**
 * Created by Nitesh Android on 05-07-2017.
 */

import android.content.Context
import android.content.SharedPreferences
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.support.annotation.StringRes


import com.nitesh.brill.saleslines.R

object Preferences {

    private val PREF_MIGRATION = arrayOf(BoolToStringPref(R.string.pref_dark_theme, false,
            R.string.pref_theme, R.string.pref_theme_value_red))

    fun sync(preferenceManager: PreferenceManager) {
        val map = preferenceManager.sharedPreferences.all
        for (key in map.keys) {
            sync(preferenceManager, key)
        }
    }

    fun sync(preferenceManager: PreferenceManager, key: String) {
        val pref = preferenceManager.findPreference(key)
        if (pref is ListPreference) {
            pref.summary = pref.entry
        }
    }

    /**
     * Migrate from boolean preferences to string preferences. Should be called only once
     * when application is relaunched.
     * If boolean preference has been set before, and value is not default, migrate to the new
     * corresponding string value
     * If boolean preference has been set before, but value is default, simply remove it
     * @param context   application context
     * * TODO remove once all users migrated
     */
    fun migrate(context: Context) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        for (pref in PREF_MIGRATION) {
            if (pref.isChanged(context, sp)) {
                editor.putString(context.getString(pref.newKey), context.getString(pref.newValue))
            }

            if (pref.hasOldValue(context, sp)) {
                editor.remove(context.getString(pref.oldKey))
            }
        }

        editor.apply()
    }


    private fun darkThemeEnabled(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_red)) == context.getString(R.string.pref_theme_value_blue)
    }

    private class BoolToStringPref (@param:StringRes val oldKey: Int, private val oldDefault: Boolean,
                                    @param:StringRes val newKey: Int, @param:StringRes val newValue: Int) {

        public fun isChanged(context: Context, sp: SharedPreferences): Boolean {
            return hasOldValue(context, sp) && sp.getBoolean(context.getString(oldKey), oldDefault) != oldDefault
        }

        public fun hasOldValue(context: Context, sp: SharedPreferences): Boolean {
            return sp.contains(context.getString(oldKey))
        }
    }
}
