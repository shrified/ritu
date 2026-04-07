package com.srzone.ritu.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.srzone.ritu.Activities.NotesActivity
import com.srzone.ritu.R // Make sure this matches your package name
import com.google.android.material.button.MaterialButton

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Manually inflate the layout
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button using its ID from the XML
        val btnOpenNotes = view.findViewById<MaterialButton>(R.id.btnOpenNotes)

        btnOpenNotes?.setOnClickListener {
            val intent = Intent(requireContext(), NotesActivity::class.java)
            startActivity(intent)

            // Smooth transition to the activity
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}