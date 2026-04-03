package com.srzone.ritu.Activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.srzone.ritu.Databases.Entities.Note
import com.srzone.ritu.Databases.NoteHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.R
import com.srzone.ritu.databinding.ActivityDiaryNotesPreviewBinding

class DiaryNotesPreviewActivity : AppCompatActivity() {

    private var binding: ActivityDiaryNotesPreviewBinding? = null
    private var handler: NoteHandler? = null
    private var singleNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryNotesPreviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val noteId = intent.getIntExtra(Params.KEY_NOTE_ID, 0)
        handler = NoteHandler(this)

        binding!!.backBtn.setOnClickListener { onBackPressed() }

        if (noteId != 0) {
            loadNote(noteId)
        }
    }

    // ─── Load Note ────────────────────────────────────────────────────────────

    private fun loadNote(noteId: Int) {
        singleNote = handler!!.getNoteById(noteId)
        val note = singleNote ?: return

        binding!!.dateTv.text = note.date
        binding!!.notesDescTv.text = note.note
        binding!!.dairyNoteInp.setText(note.note)

        binding!!.deleteIcon.setOnClickListener { showDeleteDialog(noteId) }
        binding!!.editIcon.setOnClickListener { showEditMode() }
        binding!!.cancelBtn.setOnClickListener { hideEditMode() }
        binding!!.saveBtn.setOnClickListener { saveNote(noteId) }
    }

    // ─── Edit Mode ────────────────────────────────────────────────────────────

    private fun showEditMode() {
        binding!!.notesDescTv.visibility = View.GONE
        binding!!.editIcon.visibility = View.GONE
        binding!!.inputArea.visibility = View.VISIBLE
    }

    private fun hideEditMode() {
        binding!!.notesDescTv.visibility = View.VISIBLE
        binding!!.editIcon.visibility = View.VISIBLE
        binding!!.inputArea.visibility = View.GONE
    }

    // ─── Save Note ────────────────────────────────────────────────────────────

    private fun saveNote(noteId: Int) {
        val note = singleNote ?: return
        note.note = binding!!.dairyNoteInp.text.toString()
        handler!!.updateNotes(note, noteId.toString())
        Toast.makeText(this, getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show()
        finish()
    }

    // ─── Delete Note ──────────────────────────────────────────────────────────

    private fun showDeleteDialog(noteId: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_history))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote(noteId)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNote(noteId: Int) {
        handler!!.deleteNotes(noteId.toString())
        Toast.makeText(this, getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    // ─── Back Press ───────────────────────────────────────────────────────────

    @SuppressLint("ResourceType")
    override fun onBackPressed() {
        if (binding!!.inputArea.visibility == View.VISIBLE) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                .setIcon(android.R.drawable.ic_dialog_alert) // ✅ proper resource instead of raw int
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}