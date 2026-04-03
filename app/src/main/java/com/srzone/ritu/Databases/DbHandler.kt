package com.srzone.ritu.Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

open class DbHandler(var context: Context?, str: String?) : SQLiteOpenHelper(
    context, str, null as CursorFactory?, 2
) {
    override fun onCreate(sQLiteDatabase: SQLiteDatabase?) {
    }

    override fun onUpgrade(sQLiteDatabase: SQLiteDatabase?, i: Int, i2: Int) {
    }

    fun isTableExists(sQLiteDatabase: SQLiteDatabase?, str: String?, z: Boolean): Boolean {
        var sQLiteDatabase = sQLiteDatabase
        if (z) {
            if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
                sQLiteDatabase = getReadableDatabase()
            }
            if (!sQLiteDatabase.isReadOnly()) {
                sQLiteDatabase.close()
                sQLiteDatabase = getReadableDatabase()
            }
        }
        val rawQuery = sQLiteDatabase!!.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + str + "'",
            null
        )
        if (rawQuery != null) {
            try {
                if (rawQuery.getCount() > 0) {
                    if (rawQuery != null) {
                        rawQuery.close()
                        return true
                    }
                    return true
                }
            } catch (th: Throwable) {
                if (rawQuery != null) {
                    try {
                        rawQuery.close()
                    } catch (th2: Throwable) {
                        th.addSuppressed(th2)
                    }
                }
                throw th
            }
        }
        if (rawQuery != null) {
            rawQuery.close()
            return false
        }
        return false
    }
}
