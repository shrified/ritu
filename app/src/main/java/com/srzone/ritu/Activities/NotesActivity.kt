package com.srzone.ritu.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter.OnLongItemCLickedListener
import com.srzone.ritu.Databases.Entities.Note
import com.srzone.ritu.Databases.NoteHandler
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.ActivityNotesBinding
import java.util.Calendar
import java.util.Locale

class NotesActivity : AppCompatActivity(), OnLongItemCLickedListener {
    private lateinit var binding: ActivityNotesBinding
    private lateinit var handler: NoteHandler
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // set the status bar icon colors to white
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // This is the cleanest, most "Senior" way
        val rootView = findViewById<View>(R.id.rootLayout)
        val statusBarBackground = findViewById<View>(R.id.statusBarBackground)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { rootView, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // 1. PROTECT THE SIDES: Apply left/right padding to the root layout.
            // This prevents content from hiding under the notch in landscape.
            // We leave top as 0 because we handle it manually below.
            // No Bottom Navigation, so directly pad on the rootView layout
            rootView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            // 2. Top STATUS BAR: Match the fake status bar height to the top inset
            statusBarBackground.updateLayoutParams {
                height = systemBars.top
            }

            insets
        }

        handler = NoteHandler(this)
        
        setupToolbar()
        setCurrentDate()
        
        binding.saveBtn.setOnClickListener {
            onSaveClicked()
        }
        
        binding.cancelBtn.setOnClickListener {
            Utils.hideKeyboard(this)
            showNotesList()
        }
        
        binding.addBtn.setOnClickListener {
            showInputArea()
        }
        
        binding.dateTv.setOnClickListener {
            showDatePicker()
        }
        
        showNotesList()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (binding.inputArea.visibility == View.VISIBLE && handler.allNotes.isNotEmpty()) {
                showNotesList()
            } else {
                finish()
            }
        }
    }

    private fun onSaveClicked() {
        Utils.hideKeyboard(this)
        val noteText = binding.dairyNoteInp.text.toString().trim()
        if (noteText.isEmpty()) {
            binding.noteInputLayout.error = getString(R.string.please_fill_all_fields)
        } else {
            binding.noteInputLayout.error = null
            addNote(noteText)
        }
    }

    private fun showInputArea() {
        binding.inputArea.visibility = View.VISIBLE
        binding.detailArea.visibility = View.GONE
        binding.addBtn.visibility = View.GONE
        binding.toolbar.title = getString(R.string.log_your_symptoms)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                updateSelectedDate(year, month, dayOfMonth)
            },
            selectedCalendar.get(Calendar.YEAR),
            selectedCalendar.get(Calendar.MONTH),
            selectedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        datePickerDialog.show()
    }

    private fun updateSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        selectedCalendar.set(year, month, dayOfMonth)
        binding.dateTv.text = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year)
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        updateSelectedDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun onResume() {
        super.onResume()
        showNotesList()
    }

    private fun addNote(noteText: String) {
        handler.addNote(
            Note(
                binding.dateTv.text.toString(),
                noteText
            )
        )
        binding.dairyNoteInp.setText("")
        showNotesList()
        Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
    }

    private fun showNotesList() {
        val notes = handler.allNotes.filterNotNull().toMutableList()
        notes.reverse()
        
        binding.toolbar.title = getString(R.string.notes)
        
        if (notes.isNotEmpty()) {
            binding.diaryNotesRecycler.layoutManager = LinearLayoutManager(this)
            binding.diaryNotesRecycler.adapter = DiaryNotesRecyclerAdapter(notes.toMutableList(), this, this)
            
            binding.diaryNotesRecycler.visibility = View.VISIBLE
            binding.emptyTv.visibility = View.GONE
            binding.inputArea.visibility = View.GONE
            binding.detailArea.visibility = View.VISIBLE
            binding.addBtn.visibility = View.VISIBLE
        } else {
            showInputArea()
            binding.emptyTv.visibility = View.VISIBLE
            binding.diaryNotesRecycler.visibility = View.GONE
        }
    }

    override fun onItemLongClicked(id: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_history))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                handler.deleteNotes(id.toString())
                showNotesList()
                Toast.makeText(this, getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
