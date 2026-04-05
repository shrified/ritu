package com.srzone.ritu.Utils

import android.app.Activity

object SharedPreferenceUtils {
    private const val KEY_CYCLE_LENGTH = "O_cycles"   // e.g. 28
    private const val KEY_PERIOD_LENGTH = "O_length"  // e.g. 5
    private const val KEY_DATE = "O_date"             // last period start date
    private const val OVULATION_PREFERENCE_FILE_NAME = "OvulationDetails"

    fun saveData(cycleLength: String?, date: String?, periodLength: String?, activity: Activity) {
        activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0).edit().apply {
            putString(KEY_DATE, date)
            putString(KEY_CYCLE_LENGTH, cycleLength)
            putString(KEY_PERIOD_LENGTH, periodLength)
            apply()
        }
    }

    /**
     * Returns the length of the menstrual cycle (e.g., 28 days).
     */
    fun getCycleLength(activity: Activity): String =
        activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(KEY_CYCLE_LENGTH, "28")!!

    /**
     * Returns how many days the period lasts (e.g., 5 days).
     */
    fun getPeriodLength(activity: Activity): String =
        activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(KEY_PERIOD_LENGTH, "5")!!

    fun getDate(activity: Activity): String =
        activity.getSharedPreferences(OVULATION_PREFERENCE_FILE_NAME, 0)
            .getString(KEY_DATE, "")!!

    // Keep for backward compatibility if needed, but prefer getCycleLength
    fun getCycles(activity: Activity): String = getCycleLength(activity)
}
