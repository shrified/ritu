package com.srzone.ritu.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.srzone.ritu.Activities.NotesActivity
import com.srzone.ritu.R

class NotesFragment : Fragment() {
    private var hasLaunched = false

    override fun onResume() {
        super.onResume()
        if (!hasLaunched) {
            hasLaunched = true
            startActivity(Intent(requireContext(), NotesActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        hasLaunched = false  // reset so next tab visit relaunches
    }
}