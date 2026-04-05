package com.srzone.ritu.Fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.srzone.ritu.Activities.EditPeriodActivity
import com.srzone.ritu.Activities.NotesActivity
import com.srzone.ritu.Adapters.FeaturedBlogAdapter
import com.srzone.ritu.Adapters.HomeRecyclerAdapter
import com.srzone.ritu.Databases.Entities.DateDetails
import com.srzone.ritu.Databases.OvulationDetailsHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.Model.FeaturedBlog
import com.srzone.ritu.Model.OvulationData
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.MyDateUtils
import com.srzone.ritu.Utils.OvulationCalculations
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.FragmentHomeBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var cycleLength = 28
    private var periodLength = 5
    private var fertileDays: String? = null
    private var handler: OvulationDetailsHandler? = null
    private var safeDays: String? = null
    private var detailsList: MutableList<DateDetails?> = mutableListOf()
    
    private val periodDates = mutableSetOf<LocalDate>()
    private val ovulationDates = mutableSetOf<LocalDate>()
    private val fertileDates = mutableSetOf<LocalDate>()
    private var selectedDate: LocalDate = LocalDate.now()
    private val today = LocalDate.now()

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false)
        
        setupCalendar()
        
        binding?.editPeriodBtn?.setOnClickListener {
            startActivity(Intent(activity, EditPeriodActivity::class.java))
        }
        
        binding?.notesTv?.setOnClickListener {
            startActivity(Intent(activity, NotesActivity::class.java))
        }
        
        binding?.articlesRecycler?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        setUpTheme()
        
        return binding!!.root
    }

    private fun setUpTheme() {
        val activity = activity ?: return
        val appTheme = MyThemeHandler().getAppTheme(activity) ?: return
        val themeColor = appTheme.themeColor
        
        binding?.let {
            Utils.setButtonTint(it.editPeriodBtn, themeColor)
            it.bannerLayout.setImageResource(appTheme.bgImg)
        }
    }

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)

        binding?.calendarView?.setup(startMonth, endMonth, DayOfWeek.SUNDAY)
        binding?.calendarView?.scrollToMonth(currentMonth)

        binding?.calendarView?.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val date = data.date
                container.textView.text = date.dayOfMonth.toString()
                
                container.textView.alpha = 1f
                container.textView.setTypeface(null, Typeface.NORMAL)
                container.textView.setBackgroundResource(0)
                container.textView.backgroundTintList = null
                container.dotView.visibility = View.GONE

                if (data.position != DayPosition.MonthDate) {
                    container.textView.alpha = 0.3f
                    container.textView.setTextColor(Color.GRAY)
                    return
                }

                val isSelected = date == selectedDate
                val isToday = date == today
                val isPeriod = periodDates.contains(date)
                val isOvulation = ovulationDates.contains(date)
                val isFertile = fertileDates.contains(date)

                when {
                    // 1. Selected date always wins visually
                    isSelected -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_btn_bg)
                        container.textView.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.theme9)
                        container.textView.setTextColor(Color.WHITE)
                        container.textView.setTypeface(null, Typeface.BOLD)
                    }
                    // 2. Period days (red/pink)
                    isPeriod -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_btn_bg)
                        container.textView.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.next_period_front_color)
                        container.textView.setTextColor(Color.WHITE)
                    }
                    // 3. Ovulation day
                    isOvulation -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_shape_bg)
                        container.textView.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.next_ovulation_bg_color)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.next_ovulation_front_color))
                        container.dotView.visibility = View.VISIBLE
                        container.dotView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.next_ovulation_front_color))
                    }
                    // 4. Fertile days
                    isFertile -> {
                        container.textView.setBackgroundResource(R.drawable.rounded_shape_bg)
                        container.textView.backgroundTintList =
                            ContextCompat.getColorStateList(requireContext(), R.color.fertile_days_bg_color)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.fertile_days_front_color))
                    }
                    // 5. Today (only if not in any special category)
                    isToday -> {
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.theme9))
                        container.textView.setTypeface(null, Typeface.BOLD)
                        container.dotView.visibility = View.VISIBLE
                        container.dotView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.theme9))
                    }
                    // 6. Normal day
                    else -> {
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                    }
                }

                container.textView.setOnClickListener {
                    if (data.position == DayPosition.MonthDate) {
                        val oldDate = selectedDate
                        selectedDate = date
                        handleDateSelection(date)
                        binding?.calendarView?.notifyDateChanged(oldDate)
                        binding?.calendarView?.notifyDateChanged(date)
                    }
                }
            }
        }

        binding?.calendarView?.monthScrollListener = { month ->
            binding?.topCurrentDateTv?.text = month.yearMonth
                .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.dayText)
        val dotView: View = view.findViewById(R.id.dotView)
    }

    override fun onResume() {
        super.onResume()
        val activity = activity ?: return
        val ovulationDetailsHandler = OvulationDetailsHandler(activity)
        this.handler = ovulationDetailsHandler
        this.detailsList = ovulationDetailsHandler.getAllOvulationDetails(Params.OVULATION_DETAILS_TABLE_HOME)
        
        this.cycleLength = SharedPreferenceUtils.getCycleLength(activity).toInt()
        this.periodLength = SharedPreferenceUtils.getPeriodLength(activity).toInt()
        
        setupAllEvents()
        binding?.calendarView?.notifyCalendarChanged()
        
        handleDateSelection(selectedDate)
        
        val lastPeriodDate = SharedPreferenceUtils.getDate(activity)
        try {
            mentionResult(lastPeriodDate, this.cycleLength, this.periodLength)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        showFeatureBlogs()
    }

    private fun setupAllEvents() {
        periodDates.clear()
        ovulationDates.clear()
        fertileDates.clear()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for (dateDetails in detailsList) {
            dateDetails ?: continue
            try {
                // Derive actual period start by subtracting cycleLength from nextPeriod
                val nextPeriod = dateDetails.nextPeriod ?: continue
                val periodStart = LocalDate.parse(nextPeriod, formatter)
                    .minusDays(cycleLength.toLong())  // ← KEY FIX
                val periodEnd = periodStart.plusDays(periodLength.toLong() - 1)
                addDateRange(periodDates, periodStart, periodEnd)

                val ovulationPeriod = dateDetails.ovulationPeriod ?: continue
                ovulationDates.add(LocalDate.parse(ovulationPeriod, formatter))

                val fertileDays = dateDetails.fertileDays ?: continue
                val rangeRegex = " --- | - ".toRegex()
                val fertileParts = fertileDays.split(rangeRegex)
                if (fertileParts.size >= 2) {
                    val fertileStart = LocalDate.parse(fertileParts[0].trim(), formatter)
                    val fertileEnd = LocalDate.parse(fertileParts[1].trim(), formatter)
                    addDateRange(fertileDates, fertileStart, fertileEnd)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error parsing date: ${e.message}")
            }
        }

        // Debug — remove after confirming colors are correct
        Log.d("CalendarDebug", "Period dates: $periodDates")
        Log.d("CalendarDebug", "Ovulation dates: $ovulationDates")
        Log.d("CalendarDebug", "Fertile dates: $fertileDates")
    }

    private fun addDateRange(set: MutableSet<LocalDate>, start: LocalDate, end: LocalDate) {
        var current = start
        while (!current.isAfter(end)) {
            set.add(current)
            current = current.plusDays(1)
        }
    }

    private fun handleDateSelection(date: LocalDate) {
        val chance = when {
            ovulationDates.contains(date) -> R.string.high
            fertileDates.contains(date) -> R.string.medium
            periodDates.contains(date) -> R.string.very_low
            else -> R.string.low
        }
        mentionCondition(chance)
    }

    private fun mentionCondition(i: Int) {
        binding?.let {
            it.conditionTv.setText(i)
            when (i) {
                R.string.very_low -> it.conditionImg.setImageResource(R.drawable.very_low_chances)
                R.string.high -> it.conditionImg.setImageResource(R.drawable.high_chances)
                R.string.medium -> it.conditionImg.setImageResource(R.drawable.high_chances)
                else -> it.conditionImg.setImageResource(R.drawable.low_chances)
            }
        }
    }

    private fun mentionResult(str: String, cycleLen: Int, periodLen: Int) {
        var ovulation = OvulationCalculations.getOvulation(str, cycleLen) ?: ""
        var nextPeriod = OvulationCalculations.getNextPeriod(str, cycleLen) ?: ""
        this.fertileDays = OvulationCalculations.getFertileWindow(str, cycleLen)
        this.safeDays = OvulationCalculations.getSafeDays(str, cycleLen, periodLen)

        val time = Calendar.getInstance().time
        val rangeRegex = " --- | - ".toRegex()
        
        // Find closest future events
        for (next in detailsList) {
            if (next == null || next.nextPeriod == null) continue
            val nextPeriodDate = MyDateUtils.getDateFromString(next.nextPeriod!!, "-")
            if (nextPeriodDate.after(time)) {
                nextPeriod = next.nextPeriod!!
                this.fertileDays = next.fertileDays
                this.safeDays = next.safeDays
                ovulation = next.ovulationPeriod ?: ovulation
                break
            }
        }

        val arrayList = mutableListOf<OvulationData>()
        arrayList.add(OvulationData(R.drawable.ic_next_period, getString(R.string.next_period), 
            MyDateUtils.convertInto_yyyy_MMM_dd(nextPeriod, "yyyy-MM-dd", "MMM dd"), 
            OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), nextPeriod).toString(), 
            "0", R.color.next_period_bg_color, R.color.next_period_front_color))

        arrayList.add(OvulationData(R.drawable.ic_next_ovulation, getString(R.string.next_ovulation), 
            MyDateUtils.convertInto_yyyy_MMM_dd(ovulation, "yyyy-MM-dd", "MMM dd"), 
            OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), ovulation).toString(), 
            "0", R.color.next_ovulation_bg_color, R.color.next_ovulation_front_color))

        val fertileDisplay = formatRange(this.fertileDays, rangeRegex)
        arrayList.add(OvulationData(R.drawable.ic_fertile_days, getString(R.string.fertile_days), fertileDisplay, 
            OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), 
            this.fertileDays?.split(rangeRegex)?.get(0) ?: "").toString(), 
            "0", R.color.fertile_days_bg_color, R.color.fertile_days_front_color))
        
        val safeDisplay = formatRange(this.safeDays, rangeRegex)
        arrayList.add(OvulationData(R.drawable.ic_safe_days, getString(R.string.safe_days), safeDisplay, 
            OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), 
            this.safeDays?.split(rangeRegex)?.get(0) ?: "").toString(), 
            "0", R.color.safe_days_bg_color, R.color.safe_days_front_color))

        binding?.homeRecyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = HomeRecyclerAdapter(arrayList, requireActivity())
        }
    }

    private fun formatRange(range: String?, regex: Regex): String {
        if (range.isNullOrEmpty()) return ""

        // Handle dual safe windows separated by " | "
        if (range.contains(" | ")) {
            return range.split(" | ").joinToString("  |  ") { window ->
                formatSingleRange(window.trim())
            }
        }

        return formatSingleRange(range.trim())
    }

    private fun formatSingleRange(range: String): String {
        val separator = " --- "
        return when {
            range.contains(separator) -> {
                val parts = range.split(separator)
                if (parts.size >= 2) {
                    val start = MyDateUtils.convertInto_yyyy_MMM_dd(parts[0].trim(), "yyyy-MM-dd", "dd MMM") ?: ""
                    val end = MyDateUtils.convertInto_yyyy_MMM_dd(parts[1].trim(), "yyyy-MM-dd", "dd MMM") ?: ""
                    "$start - $end"
                } else {
                    MyDateUtils.convertInto_yyyy_MMM_dd(range, "yyyy-MM-dd", "dd MMM") ?: ""
                }
            }
            range.contains(" - ") -> {
                val parts = range.split(" - ")
                if (parts.size >= 2) {
                    val start = MyDateUtils.convertInto_yyyy_MMM_dd(parts[0].trim(), "yyyy-MM-dd", "dd MMM") ?: ""
                    val end = MyDateUtils.convertInto_yyyy_MMM_dd(parts[1].trim(), "yyyy-MM-dd", "dd MMM") ?: ""
                    "$start - $end"
                } else {
                    MyDateUtils.convertInto_yyyy_MMM_dd(range, "yyyy-MM-dd", "dd MMM") ?: ""
                }
            }
            else -> MyDateUtils.convertInto_yyyy_MMM_dd(range, "yyyy-MM-dd", "dd MMM") ?: ""
        }
    }

    private fun showFeatureBlogs() {
        val context = context ?: return
        val blogList = mutableListOf<FeaturedBlog>()
        val readAssetFile = Utils.readAssetFile(context, "${Locale.getDefault().language}.json")
        val readAssetFile2 = Utils.readAssetFile(context, "en.json")
        
        if (readAssetFile != null && readAssetFile2 != null) {
            val minSize = minOf(readAssetFile.size, readAssetFile2.size)
            for (i in 0 until minSize) {
                val hashMap = readAssetFile[i] ?: continue
                val enMap = readAssetFile2[i] ?: continue
                blogList.add(FeaturedBlog(hashMap["heading"]?.toString(), hashMap["body"]?.toString(), 
                    Utils.lowerUnder(enMap["title"]?.toString() ?: ""), hashMap["title"]?.toString(), 
                    enMap["color"]?.toString(), enMap["dark"] as? Boolean ?: false))
            }
        }
        blogList.shuffle()
        binding?.articlesRecycler?.adapter = FeaturedBlogAdapter(blogList, requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
