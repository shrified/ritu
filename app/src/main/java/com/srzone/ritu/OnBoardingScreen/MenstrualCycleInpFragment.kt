package com.srzone.ritu.OnBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.R
import com.srzone.ritu.databinding.FragmentMenstrualCycleInpBinding

class MenstrualCycleInpFragment : Fragment() {

    private var binding: FragmentMenstrualCycleInpBinding? = null

    companion object {
        const val MIN_CYCLE_LENGTH = 20
        const val MAX_CYCLE_LENGTH = 40
        const val DEFAULT_CYCLE_LENGTH = 28 // Clinically typical default
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenstrualCycleInpBinding.inflate(inflater, container, false)

        // Set a safe default value so toInt() never fails on an empty field
        binding!!.cycleLengthInp.setText(DEFAULT_CYCLE_LENGTH.toString())

        binding!!.nextSessionBtn.setOnClickListener { onNextSession() }
        binding!!.nextBtn.setOnClickListener { onIncrement() }
        binding!!.prevBtn.setOnClickListener { onDecrement() }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Prevent memory leaks
    }

    private fun getCurrentValue(): Int {
        return binding?.cycleLengthInp?.text?.toString()?.toIntOrNull() ?: DEFAULT_CYCLE_LENGTH
    }

    private fun onNextSession() {
        val activity = activity as? InputActivity ?: return // Safe cast, won't crash

        val cycleLength = getCurrentValue()

        // Clamp to valid range before saving
        if (cycleLength < MIN_CYCLE_LENGTH || cycleLength > MAX_CYCLE_LENGTH) {
            binding?.cycleLengthInp?.error = "Please enter a value between $MIN_CYCLE_LENGTH and $MAX_CYCLE_LENGTH"
            return
        }

        activity.cyclesLength = cycleLength
        activity.findViewById<ViewPager2>(R.id.viewPager)?.setCurrentItem(2, true)
    }

    private fun onIncrement() {
        val current = getCurrentValue()
        // If already at max, wrap around to MIN (fix: was incorrectly wrapping to 20 on next)
        val newValue = if (current < MAX_CYCLE_LENGTH) current + 1 else MIN_CYCLE_LENGTH
        binding?.cycleLengthInp?.setText(newValue.toString())
    }

    private fun onDecrement() {
        val current = getCurrentValue()
        // If already at min, wrap around to MAX (fix: was incorrectly wrapping to 40 on prev)
        val newValue = if (current > MIN_CYCLE_LENGTH) current - 1 else MAX_CYCLE_LENGTH
        binding?.cycleLengthInp?.setText(newValue.toString())
    }
}