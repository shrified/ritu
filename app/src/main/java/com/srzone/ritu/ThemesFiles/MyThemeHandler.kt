package com.srzone.ritu.ThemesFiles

import android.app.Activity
import com.srzone.ritu.R

class MyThemeHandler {
    private val THEME_DATA = "theme_data"

    fun setAppTheme(myCustomTheme: MyCustomTheme?, activity: Activity) {
        var i = 0
        while (true) {
            val myCustomThemeArr: Array<MyCustomTheme?> = CUSTOM_THEMES
            if (i >= myCustomThemeArr.size) {
                i = 0
                break
            } else if (myCustomThemeArr[i] === myCustomTheme) {
                break
            } else {
                i++
            }
        }
        val edit = activity.getSharedPreferences("theme_data", 0).edit()
        edit.putInt("themeNo", i)
        edit.apply()
    }

    fun getAppTheme(activity: Activity): MyCustomTheme? {
        return CUSTOM_THEMES[activity.getSharedPreferences("theme_data", 0).getInt("themeNo", 0)]
    }

    fun getAppThemeIndex(activity: Activity): Int {
        return activity.getSharedPreferences("theme_data", 0).getInt("themeNo", 0)
    }

    companion object {
        val CUSTOM_THEMES: Array<MyCustomTheme?> = arrayOf<MyCustomTheme?>(
            MyCustomTheme(
                R.drawable.theme10_bg,
                R.color.theme10,
                R.color.theme_7_bg,
                false
            ),
            MyCustomTheme(R.drawable.theme8_bg, R.color.theme8, R.color.theme_7_bg, false),
            MyCustomTheme(R.drawable.theme9_bg, R.color.theme9, R.color.theme_7_bg, false),
            MyCustomTheme(R.drawable.theme1_bg, R.color.theme1, R.color.theme_1_bg, true),
            MyCustomTheme(R.drawable.theme2_bg, R.color.theme2, R.color.theme_2_bg, true),
            MyCustomTheme(R.drawable.theme3_bg, R.color.theme3, R.color.theme_3_bg, false),
            MyCustomTheme(R.drawable.theme4_bg, R.color.theme4, R.color.theme_4_bg, false),
            MyCustomTheme(R.drawable.theme5_bg, R.color.theme5, R.color.theme_5_bg, false),
            MyCustomTheme(R.drawable.theme6_bg, R.color.theme6, R.color.theme_6_bg, true),
            MyCustomTheme(R.drawable.theme7_bg, R.color.theme7, R.color.theme_7_bg, false)
        )
    }
}
