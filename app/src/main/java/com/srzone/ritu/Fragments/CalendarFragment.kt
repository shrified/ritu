package com.srzone.ritu.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.srzone.ritu.Activities.EditPeriodActivity
import com.srzone.ritu.Databases.Entities.DateDetails
import com.srzone.ritu.Databases.OvulationDetailsHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.R
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarFragment : Fragment() {

    private var binding: FragmentCalendarBinding? = null
    private var handler: OvulationDetailsHandler? = null
    private var detailsList: MutableList<DateDetails?> = ArrayList()

    private val periodDates = mutableSetOf<LocalDate>()
    private val ovulationDates = mutableSetOf<LocalDate>()
    private val fertileDates = mutableSetOf<LocalDate>()

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)

        handler = OvulationDetailsHandler(activity)
        detailsList = handler!!.getAllOvulationDetails(Params.OVULATION_DETAILS_TABLE_CALENDAR)

        setupAllEvents()
        setupCalendar()
        mentionDataWithSelectedDate(LocalDate.now())

        binding!!.topCurrentDateTv.text = YearMonth.now()
            .format(DateTimeFormatter.ofPattern("MMM yyyy"))

        binding!!.editPeriodBtn.setOnClickListener {
            startActivity(Intent(activity, EditPeriodActivity::class.java))
        }

        return binding!!.root
    }

    // ─── Calendar Setup ───────────────────────────────────────────────────────

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)

        binding!!.calendarView.setup(startMonth, endMonth, DayOfWeek.MONDAY)
        binding!!.calendarView.scrollToMonth(currentMonth)

        binding!!.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val date = data.date
                container.textView.text = date.dayOfMonth.toString()

                // Grey out days not in current month
                if (data.position != DayPosition.MonthDate) {
                    container.textView.alpha = 0.3f
                    container.textView.setBackgroundResource(0)
                    container.textView.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                    return
                }

                container.textView.alpha = 1f

                when {
                    date == selectedDate -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_btn_bg)
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white)
                        )
                    }
                    periodDates.contains(date) -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_btn_bg)
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white)
                        )
                    }
                    ovulationDates.contains(date) -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_shape_bg)
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.app_primary_color)
                        )
                    }
                    fertileDates.contains(date) -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_shape_bg)
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.black)
                        )
                    }
                    else -> {
                        container.textView.setBackgroundResource(0)
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.black)
                        )
                    }
                }

                container.textView.setOnClickListener {
                    if (data.position == DayPosition.MonthDate) {
                        selectedDate = date
                        mentionDataWithSelectedDate(date)
                        binding!!.topCurrentDateTv.text = YearMonth.of(date.year, date.month)
                            .format(DateTimeFormatter.ofPattern("MMM yyyy"))
                        binding!!.calendarView.notifyCalendarChanged()
                    }
                }
            }
        }

        binding!!.calendarView.monthScrollListener = { month ->
            binding!!.topCurrentDateTv.text = month.yearMonth
                .format(DateTimeFormatter.ofPattern("MMM yyyy"))
        }
    }

    // ─── ViewContainer ────────────────────────────────────────────────────────

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.dayText)
    }

    // ─── Events Setup ─────────────────────────────────────────────────────────

    private fun setupAllEvents() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for (dateDetails in detailsList) {
            dateDetails ?: continue

            try {
                // Period dates
                val nextPeriod = dateDetails.nextPeriod ?: continue
                val periodStart = LocalDate.parse(nextPeriod, formatter)
                val periodEnd = periodStart.plusDays(
                    SharedPreferenceUtils.getCycleLength(requireActivity()).toLong()
                )
                addDateRange(periodDates, periodStart, periodEnd)

                // Ovulation date
                val ovulationPeriod = dateDetails.ovulationPeriod ?: continue
                val ovulationDate = LocalDate.parse(ovulationPeriod, formatter)
                ovulationDates.add(ovulationDate)

                // Fertile days
                val fertileDays = dateDetails.fertileDays ?: continue
                val fertileParts = fertileDays.split("---")
                if (fertileParts.size == 2) {
                    val fertileStart = LocalDate.parse(fertileParts[0].trim(), formatter)
                    val fertileEnd = LocalDate.parse(fertileParts[1].trim(), formatter)
                    addDateRange(fertileDates, fertileStart, fertileEnd)
                }

            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error parsing date: ${e.message}")
            }
        }
    }

    private fun addDateRange(set: MutableSet<LocalDate>, start: LocalDate, end: LocalDate) {
        var current = start
        while (!current.isAfter(end)) {
            set.add(current)
            current = current.plusDays(1)
        }
    }

    // ─── Date Info Display ────────────────────────────────────────────────────

    private fun mentionDataWithSelectedDate(date: LocalDate) {
        val displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)

        binding!!.currentDateTv.text = date.format(displayFormatter)
        binding!!.currentDayTv.text = date.format(dayFormatter)

        // Default if no data
        if (detailsList.isEmpty()) {
            binding!!.chancesTv.text = getString(R.string.low)
            return
        }

        for (dateDetails in detailsList) {
            dateDetails ?: continue

            val chance = when {
                date == ovulationDates.firstOrNull { it == date } ->
                    R.string.high
                fertileDates.contains(date) ->
                    R.string.medium
                periodDates.contains(date) ->
                    R.string.very_low
                else ->
                    R.string.low
            }

            binding!!.chancesTv.text = getString(chance)
            return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TAG = "CalendarTag"
    }
}