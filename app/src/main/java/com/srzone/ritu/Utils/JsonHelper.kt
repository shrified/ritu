package com.srzone.ritu.Utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader

object JsonHelper {

    @Throws(IOException::class)
    fun getJsonData(context: Context, str: String): MutableList<HashMap<String?, Any?>?>? {
        val assets = context.assets
        val open = assets.open(str)
        val inputStreamReader = InputStreamReader(open)
        
        val typeToken = object : TypeToken<MutableList<HashMap<String?, Any?>?>?>() {}.type
        val list: MutableList<HashMap<String?, Any?>?>? = Gson().fromJson(
            inputStreamReader,
            typeToken
        )
        
        inputStreamReader.close()
        open.close()
        return list
    }
}
