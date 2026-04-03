package com.srzone.ritu.Utils

import android.app.Activity

object SharedPreferenceUtils {
    private const val KEY_CYCLE_DAYS = "O_cycles"
    private const val KEY_CYCLE_Length = "O_length"
    private const val KEY_DATE = "O_date"
    private const val OVULATION_PREFERENCE_FILE_NAME = "OvulationDetails"

    fun saveData(str: String?, str2: String?, str3: String?, activity: Activity) {
        val edit = activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0).edit()
        edit.putString(KEY_DATE, str2)
        edit.putString(KEY_CYCLE_DAYS, str)
        edit.putString(KEY_CYCLE_Length, str3)
        edit.apply()
    }

    fun getCycles(activity: Activity): String {
        return activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(SharedPreferenceUtils.KEY_CYCLE_DAYS, "")!!
    }

    fun getDate(activity: Activity): String {
        return activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(SharedPreferenceUtils.KEY_DATE, "")!!
    }

    fun getCycleLength(activity: Activity): String {
        return activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(SharedPreferenceUtils.KEY_CYCLE_Length, "")!!
    }
}
