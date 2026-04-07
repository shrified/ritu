package com.srzone.ritu.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.srzone.ritu.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter.OnLongItemCLickedListener
import com.srzone.ritu.Databases.Entities.Note
import com.srzone.ritu.Databases.NoteHandler
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.FragmentNotesBinding
import java.util.Calendar
import java.util.Locale

class NotesFragment : Fragment(R.layout.fragment_notes), OnLongItemCLickedListener {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: NoteHandler
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = NoteHandler(requireContext())
        setupToolbar()
        setCurrentDate()

        binding.saveBtn.setOnClickListener { onSaveClicked() }
        binding.cancelBtn.setOnClickListener {
            Utils.hideKeyboard(requireActivity())
            showNotesList()
        }
        binding.addBtn.setOnClickListener { showInputArea() }
        binding.dateTv.setOnClickListener { showDatePicker() }

        showNotesList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (binding.inputArea.visibility == View.VISIBLE && handler.allNotes.isNotEmpty()) {
                showNotesList()
            } else {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun onSaveClicked() {
        Utils.hideKeyboard(requireActivity())
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
            requireContext(),
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
        Toast.makeText(requireContext(), getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
    }

    private fun showNotesList() {
        val notes = handler.allNotes.filterNotNull().toMutableList()
        notes.reverse()

        binding.toolbar.title = getString(R.string.notes)

        if (notes.isNotEmpty()) {
            binding.diaryNotesRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.diaryNotesRecycler.adapter = DiaryNotesRecyclerAdapter(notes.toMutableList(), this, requireActivity())

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
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_history))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                handler.deleteNotes(id.toString())
                showNotesList()
                Toast.makeText(requireContext(), getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}