package com.srzone.ritu.Utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale
import androidx.core.content.edit

object LanguageUtils {

    fun setLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun getSavedLanguage(context: Context): String {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
    }

    fun saveLanguage(context: Context, langCode: String) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit { putString("language", langCode) }
    }
}