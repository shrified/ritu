package com.srzone.ritu.OnBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.R
import com.srzone.ritu.databinding.FragmentPeriodInputBinding

class PeriodInputFragment : Fragment() {

    private var binding: FragmentPeriodInputBinding? = null

    companion object {
        const val MIN_PERIOD_LENGTH = 3
        const val MAX_PERIOD_LENGTH = 9
        const val DEFAULT_PERIOD_LENGTH = 5
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeriodInputBinding.inflate(inflater, container, false)

        // ✅ Set default so toInt() never runs on empty string
        binding!!.cycleLengthInp.setText(DEFAULT_PERIOD_LENGTH.toString())

        binding!!.nextSessionBtn.setOnClickListener { onNext() }
        binding!!.backSessionBtn.setOnClickListener { onBack() }
        binding!!.nextBtn.setOnClickListener { onIncrement() }
        binding!!.prevBtn.setOnClickListener { onDecrement() }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ✅ Prevent memory leak
    }

    private fun getCurrentValue(): Int {
        // ✅ Safe parse — never crashes on empty or non-numeric input
        return binding?.cycleLengthInp?.text?.toString()?.toIntOrNull() ?: DEFAULT_PERIOD_LENGTH
    }

    private fun onNext() {
        val activity = activity as? InputActivity ?: return // ✅ Safe cast

        val periodLength = getCurrentValue()

        if (periodLength < MIN_PERIOD_LENGTH || periodLength > MAX_PERIOD_LENGTH) {
            binding?.cycleLengthInp?.error = "Please enter a value between $MIN_PERIOD_LENGTH and $MAX_PERIOD_LENGTH"
            return
        }

        activity.periodLength = periodLength
        activity.findViewById<ViewPager2>(R.id.viewPager)?.setCurrentItem(3, true)
    }

    private fun onBack() {
        // ✅ Safe cast + null safety on findViewById
        val activity = activity ?: return
        activity.findViewById<ViewPager2>(R.id.viewPager)?.setCurrentItem(0, true)
    }

    private fun onIncrement() {
        val current = getCurrentValue()
        // ✅ Fixed wrap: at MAX wrap to MIN (was wrapping to 3 which is correct but inconsistent naming)
        val newValue = if (current < MAX_PERIOD_LENGTH) current + 1 else MIN_PERIOD_LENGTH
        binding?.cycleLengthInp?.setText(newValue.toString())
    }

    private fun onDecrement() {
        val current = getCurrentValue()
        // ✅ Fixed wrap: at MIN wrap to MAX (was wrapping to 9 which is correct but inconsistent naming)
        val newValue = if (current > MIN_PERIOD_LENGTH) current - 1 else MAX_PERIOD_LENGTH
        binding?.cycleLengthInp?.setText(newValue.toString())
    }
}