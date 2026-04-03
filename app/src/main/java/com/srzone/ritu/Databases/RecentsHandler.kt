package com.srzone.ritu.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.srzone.ritu.Databases.Entities.Recents

class RecentsHandler(context: Context?) : DbHandler(context, Params.DB_NAME_RECENTS) {
    override fun onCreate(sQLiteDatabase: SQLiteDatabase?) {
        sQLiteDatabase?.execSQL("CREATE TABLE ${Params.RECENTS_TABLE}(${Params.KEY_RECENTS_ID} INTEGER PRIMARY KEY,${Params.KEY_RECENTS_TITLE} TEXT, ${Params.KEY_RECENTS_HEADING} TEXT)")
    }

    val allRecents: MutableList<Recents?>
        get() {
            val arrayList = ArrayList<Recents?>()
            val rawQuery = readableDatabase.rawQuery("SELECT * FROM ${Params.RECENTS_TABLE}", null)
            rawQuery.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val recents = Recents().apply {
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_ID))
                            title = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_TITLE))
                            heading = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_HEADING))
                        }
                        arrayList.add(recents)
                    } while (cursor.moveToNext())
                }
            }
            return arrayList
        }

    fun getRecentByParam(str: String, str2: String?): Recents? {
        val readableDatabase = readableDatabase
        var recents: Recents? = null
        val replace = str.replace("'", "''")
        val rawQuery = readableDatabase.rawQuery(
            "select * from ${Params.RECENTS_TABLE} where $str2='$replace'",
            null
        )
        rawQuery?.use { cursor ->
            if (cursor.moveToFirst()) {
                recents = Recents().apply {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_ID))
                    title = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_TITLE))
                    heading = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_RECENTS_HEADING))
                }
            }
        }
        return recents
    }

    fun addRecent(recents: Recents) {
        val writableDatabase = writableDatabase
        val contentValues = ContentValues().apply {
            put(Params.KEY_RECENTS_TITLE, recents.title)
            put(Params.KEY_RECENTS_HEADING, recents.heading)
        }
        writableDatabase.insert(Params.RECENTS_TABLE, null, contentValues)
    }

    fun updateRecent(recents: Recents, str: String?) {
        val writableDatabase = writableDatabase
        val contentValues = ContentValues().apply {
            put(Params.KEY_RECENTS_TITLE, recents.title)
            put(Params.KEY_RECENTS_HEADING, recents.heading)
        }
        writableDatabase.update(
            Params.RECENTS_TABLE,
            contentValues,
            "${Params.KEY_RECENTS_ID}= ?",
            arrayOf(str)
        )
    }

    fun deleteRecent(str: String?) {
        val writableDatabase = writableDatabase
        writableDatabase.delete(Params.RECENTS_TABLE, "${Params.KEY_RECENTS_ID}=$str", null)
    }
}
