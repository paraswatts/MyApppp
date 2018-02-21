package com.nitesh.brill.saleslines.Common_Files


import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.util.*

class SaveData(private val _context: Context) : ConstantValue {
    private val shared: SharedPreferences
    private val SHARED_NAME = "MoeApp"
    private val edit: Editor

    init {
        shared = _context.getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE)
        edit = shared.edit()
    }

    // ============================================//
    fun save(key: String, value: String) {
        edit.putString(key, value)
        edit.commit()
    }

    // ============================================//
    fun saveInt(key: String, value: Int) {
        edit.putInt(key, value)
        edit.commit()
    }

    // ============================================//
    fun saveFloat(key: String, value: Float) {
        edit.putFloat(key, value)
        edit.commit()
    }

    // ============================================//
    fun saveBoolean(key: String, value: Boolean) {
        edit.putBoolean(key, value)
        edit.commit()
    }

    // ============================================//
    fun getString(key: String): String {
        return shared.getString(key, key)

    }

    // ============================================//
    fun getInt(key: String): Int {
        return shared.getInt(key, 0)

    }

    // ============================================//
    fun getFloat(key: String): Float {
        return shared.getFloat(key, 0f)

    }

    // ============================================//
    fun getBoolean(key: String): Boolean {
        return shared.getBoolean(key, false)

    }
    fun getBooleanReminder(key: String): Boolean {
        return shared.getBoolean(key, true)

    }

    // ============================================//
    fun isExist(key: String): Boolean {
        return shared.contains(key)

    }

    // ============================================//
    fun remove(key: String) {

        edit.remove(key)
        edit.commit()

    }

    fun save_arraylist(key: String, array_list: ArrayList<String>) {
        for (i in array_list.indices) {
            edit.putString(key, array_list[i])
        }
        edit.putInt("size_array", array_list.size)
        edit.commit()

    }

    fun get_array_lsit(key: String): ArrayList<String> {
        val myAList = ArrayList<String>()
        val size = shared.getInt("size_array", 0)
        for (j in 0..size - 1) {
            myAList.add(shared.getString(key, key))
        }
        return myAList
    }

}
