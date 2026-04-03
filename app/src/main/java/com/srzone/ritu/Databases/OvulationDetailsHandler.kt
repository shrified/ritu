package com.srzone.ritu.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.srzone.ritu.Databases.Entities.DateDetails

class OvulationDetailsHandler(context: Context?) : DbHandler(context, Params.DB_NAME_DETAILS) {
    override fun onCreate(sQLiteDatabase: SQLiteDatabase?) {
        sQLiteDatabase?.execSQL("CREATE TABLE ${Params.OVULATION_DETAILS_TABLE_HOME}(${Params.KEY_DETAIL_ID} INTEGER PRIMARY KEY,${Params.KEY_SAFE_DAYS} TEXT, ${Params.KEY_FERTILE_DAYS} TEXT, ${Params.KEY_NEXT_PERIOD} TEXT, ${Params.KEY_NEXT_OVULATION} TEXT)")
        sQLiteDatabase?.execSQL("CREATE TABLE ${Params.OVULATION_DETAILS_TABLE_CALENDAR}(${Params.KEY_DETAIL_ID} INTEGER PRIMARY KEY,${Params.KEY_SAFE_DAYS} TEXT, ${Params.KEY_FERTILE_DAYS} TEXT, ${Params.KEY_NEXT_PERIOD} TEXT, ${Params.KEY_NEXT_OVULATION} TEXT)")
    }

    fun getAllOvulationDetails(tableName: String?): MutableList<DateDetails?> {
        val arrayList = ArrayList<DateDetails?>()
        if (tableName == null) return arrayList
        
        val db = readableDatabase
        db.rawQuery("SELECT * FROM $tableName", null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val dateDetails = DateDetails()
                    dateDetails.id = cursor.getInt(cursor.getColumnIndexOrThrow(Params.KEY_DETAIL_ID))
                    dateDetails.safeDays = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_SAFE_DAYS))
                    dateDetails.fertileDays = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_FERTILE_DAYS))
                    dateDetails.nextPeriod = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_NEXT_PERIOD))
                    dateDetails.ovulationPeriod = cursor.getString(cursor.getColumnIndexOrThrow(Params.KEY_NEXT_OVULATION))
                    arrayList.add(dateDetails)
                } while (cursor.moveToNext())
            }
        }
        return arrayList
    }

    fun addOvulationDetail(dateDetails: DateDetails, tableName: String?): String {
        if (tableName == null) return "-1"
        
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Params.KEY_SAFE_DAYS, dateDetails.safeDays)
        contentValues.put(Params.KEY_FERTILE_DAYS, dateDetails.fertileDays)
        contentValues.put(Params.KEY_NEXT_PERIOD, dateDetails.nextPeriod)
        contentValues.put(Params.KEY_NEXT_OVULATION, dateDetails.ovulationPeriod)
        return db.insert(tableName, null, contentValues).toString()
    }
}
