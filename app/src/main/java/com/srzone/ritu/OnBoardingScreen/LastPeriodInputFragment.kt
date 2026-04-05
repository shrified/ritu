package com.srzone.ritu.OnBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.srzone.ritu.Activities.InputActivity
import com.srzone.ritu.Activities.MainActivity
import com.srzone.ritu.Databases.Entities.DateDetails
import com.srzone.ritu.Databases.OvulationDetailsHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.R
import com.srzone.ritu.Utils.OvulationCalculations
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.databinding.FragmentLastPeriodInputBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LastPeriodInputFragment : Fragment() {

    private var binding: FragmentLastPeriodInputBinding? = null
    private var handler: OvulationDetailsHandler? = null
    private var selectedDate: Calendar = Calendar.getInstance() // ✅ Non-null, always safe to use

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLastPeriodInputBinding.inflate(inflater, container, false) // ✅ Pass container
        handler = OvulationDetailsHandler(requireContext()) // ✅ requireContext() over getActivity()

        val inputActivity = activity as? InputActivity ?: return null // ✅ Safe cast

        // ✅ Initialize selectedDate to whatever date the CalendarView shows by default
        // so if user never taps a date, we still have a valid selection
        binding!!.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate.set(year, month, dayOfMonth)
        }

        // ✅ Prevent user from selecting a future date — last period can't be in the future
        binding!!.calendarView.maxDate = System.currentTimeMillis()

        binding!!.backSessionBtn.setOnClickListener {
            inputActivity.findViewById<ViewPager2>(R.id.viewPager)?.setCurrentItem(1, true) // ✅ Safe call
        }

        binding!!.getStartedBtn.setOnClickListener {
            onGetStarted(inputActivity)
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ✅ Prevent memory leak
    }

    private fun onGetStarted(inputActivity: InputActivity) {
        // ✅ Validate that cyclesLength and periodLength were actually set
        if (inputActivity.cyclesLength <= 0 || inputActivity.periodLength <= 0) {
            return
        }

        binding?.progressBar?.visibility = View.VISIBLE
        binding?.backSessionBtn?.visibility = View.GONE
        binding?.getStartedBtn?.visibility = View.GONE

        requireActivity().deleteDatabase(Params.DB_NAME_DETAILS)
        requireActivity().deleteDatabase(Params.DB_NAME_NOTES)

        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            .format(selectedDate.time) // ✅ No !! needed — selectedDate is non-null

        SharedPreferenceUtils.saveData(
            inputActivity.cyclesLength.toString(),
            formattedDate,
            inputActivity.periodLength.toString(),
            inputActivity
        )

        saveDateToDatabase(formattedDate, inputActivity)

        startActivity(Intent(inputActivity, MainActivity::class.java))
        inputActivity.finishAffinity()
    }

    private fun saveDateToDatabase(dateStr: String?, inputActivity: InputActivity) {
        // ✅ Guard against null/blank date string — would silently corrupt all DB entries
        if (dateStr.isNullOrBlank()) return

        val cycleLen = inputActivity.cyclesLength
        val periodLen = inputActivity.periodLength

        // ✅ Guard against zero cycle length — would cause infinite/broken loop offsets
        if (cycleLen <= 0) return

        val ovulation = OvulationCalculations.getOvulation(dateStr, cycleLen)
        val nextPeriod = OvulationCalculations.getNextPeriod(dateStr, cycleLen)
        val fertileWindow = OvulationCalculations.getFertileWindow(dateStr, cycleLen)
        val safeDays = OvulationCalculations.getSafeDays(dateStr, cycleLen, periodLen)

        // ✅ HOME table: 12 future cycles + 2 past cycles (i7 loop was 0..1 = only 2, kept as-is)
        for (i in 0..11) {
            val offset = cycleLen * i
            handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(fertileWindow, offset),
                    OvulationCalculations.addDays(safeDays, offset),
                    OvulationCalculations.addDays(nextPeriod, offset),
                    OvulationCalculations.addDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_HOME
            )
            // ✅ Skip i=0 for past entries — minusDays(x, 0) duplicates the base entry
            if (i > 0) {
                handler!!.addOvulationDetail(
                    DateDetails(
                        OvulationCalculations.minusDays(fertileWindow, offset),
                        OvulationCalculations.minusDays(safeDays, offset),
                        OvulationCalculations.minusDays(nextPeriod, offset),
                        OvulationCalculations.minusDays(ovulation, offset)
                    ), Params.OVULATION_DETAILS_TABLE_HOME
                )
            }
        }

        // ✅ CALENDAR table: 6 future cycles
        for (i in 0..5) {
            val offset = cycleLen * i
            handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(fertileWindow, offset),
                    OvulationCalculations.addDays(safeDays, offset),
                    OvulationCalculations.addDays(nextPeriod, offset),
                    OvulationCalculations.addDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_CALENDAR
            )
        }

        // ✅ CALENDAR table: 2 past cycles, skip i=0 to avoid duplicate base entry
        for (i in 1..2) {
            val offset = cycleLen * i
            handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.minusDays(fertileWindow, offset),
                    OvulationCalculations.minusDays(safeDays, offset),
                    OvulationCalculations.minusDays(nextPeriod, offset),
                    OvulationCalculations.minusDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_CALENDAR
            )
        }
    }
}