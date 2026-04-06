package com.srzone.ritu.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.srzone.ritu.Utils.LanguageUtils

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageUtils.getSavedLanguage(newBase)
        super.attachBaseContext(LanguageUtils.setLocale(newBase, lang))
    }
}