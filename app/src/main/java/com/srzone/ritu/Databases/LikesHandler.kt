package com.srzone.ritu.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.srzone.ritu.Databases.Entities.Likes

class LikesHandler(context: Context?) : DbHandler(context, Params.DB_NAME_LIKES) {
    override fun onCreate(sQLiteDatabase: SQLiteDatabase?) {
        sQLiteDatabase?.execSQL("CREATE TABLE likes_table(likes_Id INTEGER PRIMARY KEY,likes_title TEXT, likes_heading TEXT)")
    }

    val allLikes: MutableList<Likes?>
        get() {
            val arrayList = ArrayList<Likes?>()
            val rawQuery = readableDatabase.rawQuery("SELECT * FROM likes_table", null)
            if (rawQuery.moveToFirst()) {
                do {
                    val likes = Likes()
                    likes.id = rawQuery.getString(0).toInt()
                    likes.title = rawQuery.getString(1)
                    likes.heading = rawQuery.getString(2)
                    arrayList.add(likes)
                } while (rawQuery.moveToNext())
                rawQuery.close()
            } else {
                rawQuery.close()
            }
            return arrayList
        }

    fun getLikeByParam(str: String, str2: String?): Likes? {
        val readableDatabase = readableDatabase
        val replace = str.replace("'", "''")
        var likes: Likes? = null
        val rawQuery = readableDatabase.rawQuery(
            "select * from likes_table where $str2='$replace'",
            null
        )
        if (rawQuery != null) {
            if (rawQuery.moveToFirst()) {
                likes = Likes()
                likes.id = rawQuery.getString(0).toInt()
                likes.title = rawQuery.getString(1)
                likes.heading = rawQuery.getString(2)
            }
            rawQuery.close()
        }
        return likes
    }

    fun addLike(likes: Likes) {
        val writableDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Params.KEY_LIKES_TITLE, likes.title)
        contentValues.put(Params.KEY_LIKES_HEADING, likes.heading)
        writableDatabase.insert(Params.LIKES_TABLE, null, contentValues)
        writableDatabase.close()
    }

    fun updateLike(likes: Likes, str: String?) {
        val contentValues = ContentValues()
        val writableDatabase = writableDatabase
        contentValues.put(Params.KEY_LIKES_TITLE, likes.title)
        contentValues.put(Params.KEY_LIKES_HEADING, likes.heading)
        writableDatabase.update(
            Params.LIKES_TABLE,
            contentValues,
            "likes_Id= ?",
            arrayOf(str)
        )
        writableDatabase.close()
    }

    fun deleteLike(str: String?) {
        val writableDatabase = writableDatabase
        writableDatabase.delete(Params.LIKES_TABLE, "likes_Id=$str", null)
        writableDatabase.close()
    }
}
