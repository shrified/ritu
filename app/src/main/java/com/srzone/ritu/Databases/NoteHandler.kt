package com.srzone.ritu.Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.srzone.ritu.Databases.Entities.Note

class NoteHandler(context: Context?) : DbHandler(context, Params.DB_NAME_NOTES) {

    // ✅ Fix: DbHandler.onCreate takes SQLiteDatabase? (nullable)
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE notes_table(noteId INTEGER PRIMARY KEY, note TEXT, date TEXT)")
    }

    // ─── Get All Notes ────────────────────────────────────────────────────────

    val allNotes: MutableList<Note?>
        get() {
            val arrayList = ArrayList<Note?>()
            val cursor = readableDatabase.rawQuery("SELECT * FROM notes_table", null)
            if (cursor.moveToFirst()) {
                do {
                    val note = Note()
                    note.id = cursor.getString(0).toInt()
                    note.note = cursor.getString(1)
                    note.date = cursor.getString(2)
                    arrayList.add(note)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return arrayList
        }

    // ─── Get Note By ID ───────────────────────────────────────────────────────

    fun getNoteById(id: Int): Note? {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM notes_table WHERE noteId = ?",
            arrayOf(id.toString()) // ✅ use ? binding instead of string concat
        )
        var note: Note? = null
        if (cursor.moveToFirst()) {
            note = Note()
            note.id = cursor.getString(0).toInt()
            note.note = cursor.getString(1)
            note.date = cursor.getString(2)
        }
        cursor.close()
        return note
    }

    // ─── Add Note ─────────────────────────────────────────────────────────────

    fun addNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Params.KEY_NOTE, note.note)   // ✅ Kotlin property access
            put(Params.KEY_DATE, note.date)
        }
        db.insert(Params.NOTES_TABLE, null, values)
        db.close()
    }

    // ─── Update Note ──────────────────────────────────────────────────────────

    fun updateNotes(note: Note, id: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Params.KEY_NOTE, note.note)   // ✅ Kotlin property access
            put(Params.KEY_DATE, note.date)
        }
        db.update(Params.NOTES_TABLE, values, "noteId = ?", arrayOf(id))
        db.close()
    }

    // ─── Delete Note ──────────────────────────────────────────────────────────

    fun deleteNotes(id: String?) {
        val db = writableDatabase
        db.delete(Params.NOTES_TABLE, "noteId = ?", arrayOf(id)) // ✅ use ? binding
        db.close()
    }
}