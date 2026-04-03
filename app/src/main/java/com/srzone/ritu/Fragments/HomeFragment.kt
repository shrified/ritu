package com.srzone.ritu.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.EditPeriodActivity
import com.srzone.ritu.Activities.NotesActivity
import com.srzone.ritu.Adapters.CalendarSliderAdapter
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var cycleLength = 0
    private var fertileDays: String? = null
    private var handler: OvulationDetailsHandler? = null
    private var mAdapter: CalendarSliderAdapter? = null
    private var safeDays: String? = null
    private var detailsList: MutableList<DateDetails?> = mutableListOf()
    private var dates: MutableList<Date?> = mutableListOf()
    private var currentCalSliderPos = 0

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false)
        
        binding?.editPeriodBtn?.setOnClickListener {
            startActivity(Intent(activity, EditPeriodActivity::class.java))
        }
        
        binding?.notesTv?.setOnClickListener {
            startActivity(Intent(activity, NotesActivity::class.java))
        }
        
        loadHorizontalDates()
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
            it.dateTv.setTextColor(ContextCompat.getColor(activity, themeColor))
            it.bannerLayout.setImageResource(appTheme.bgImg)
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity ?: return
        val ovulationDetailsHandler = OvulationDetailsHandler(activity)
        this.handler = ovulationDetailsHandler
        this.detailsList = ovulationDetailsHandler.getAllOvulationDetails(Params.OVULATION_DETAILS_TABLE_HOME)
        
        handleRecyclerEvents(this.currentCalSliderPos)
        
        val date = SharedPreferenceUtils.getDate(activity)
        this.cycleLength = SharedPreferenceUtils.getCycleLength(activity).toInt()
        
        try {
            mentionResult(
                date,
                SharedPreferenceUtils.getCycles(activity).toInt(),
                this.cycleLength
            )
        } catch (e: ParseException) {
            Log.d("DatesMatching", "error --> " + e.message)
        }
        showFeatureBlogs()
    }

    private fun showFeatureBlogs() {
        val context = context ?: return
        val blogList = mutableListOf<FeaturedBlog>()
        val lang = Locale.getDefault().language
        val readAssetFile = Utils.readAssetFile(context, "$lang.json")
        val readAssetFile2 = Utils.readAssetFile(context, "en.json")
        
        if (readAssetFile != null && readAssetFile2 != null) {
            val minSize = minOf(readAssetFile.size, readAssetFile2.size)
            for (i in 0 until minSize) {
                val hashMap = readAssetFile[i]
                val enMap = readAssetFile2[i]
                if (hashMap != null && enMap != null) {
                    blogList.add(
                        FeaturedBlog(
                            hashMap["heading"]?.toString(),
                            hashMap["body"]?.toString(),
                            Utils.lowerUnder(enMap["title"]?.toString() ?: ""),
                            hashMap["title"]?.toString(),
                            enMap["color"]?.toString(),
                            enMap["dark"] as? Boolean ?: false
                        )
                    )
                }
            }
        }

        blogList.shuffle()
        binding?.articlesRecycler?.adapter = FeaturedBlogAdapter(blogList, requireActivity())
    }

    private fun loadHorizontalDates() {
        val calendar = Calendar.getInstance()
        val i = calendar.get(Calendar.DAY_OF_YEAR) - 1
        val i2 = calendar.get(Calendar.YEAR)
        calendar.set(Calendar.YEAR, i2)
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        
        while (calendar.get(Calendar.YEAR) == i2) {
            this.dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        
        this.mAdapter = object : CalendarSliderAdapter(this.dates, i, binding!!.calendarRecycler) {
            override fun onDateClicked(i3: Int) {
                handleRecyclerEvents(i3)
            }
        }
        
        binding?.calendarRecycler?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mAdapter
            if (i > 1) {
                smoothScrollToPosition(i + 2)
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                    linearLayoutManager?.let {
                        handleRecyclerEvents(it.findFirstVisibleItemPosition() + (it.childCount / 2))
                    }
                }
            })
        }
    }

    private fun handleRecyclerEvents(i: Int) {
        if (i < 0 || i >= dates.size) return
        this.currentCalSliderPos = i
        val date = this.dates[i] ?: return
        
        binding?.dateTv?.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        
        var conditionFound = false
        for (next in detailsList) {
            if (next == null || next.nextPeriod == null) continue
            try {
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                if (MyDateUtils.checkDate(
                        formattedDate,
                        next.nextPeriod!!,
                        OvulationCalculations.addDays(next.nextPeriod!!, this.cycleLength),
                        "yyyy-MM-dd"
                    ) || next.nextPeriod == formattedDate
                ) {
                    mentionCondition(R.string.low) // Corrected from break logic in original
                    conditionFound = true
                    break
                } else {
                    val fertileParts = next.fertileDays?.split("---") ?: emptyList()
                    if (fertileParts.size >= 2) {
                        if (MyDateUtils.checkDate(
                                formattedDate,
                                OvulationCalculations.minusDays(fertileParts[0].trim(), 1),
                                OvulationCalculations.addDays(fertileParts[1].trim(), 1),
                                "yyyy-MM-dd"
                            )
                        ) {
                            if (next.ovulationPeriod == formattedDate) {
                                mentionCondition(R.string.high)
                            } else {
                                mentionCondition(R.string.medium)
                            }
                            conditionFound = true
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        if (!conditionFound) {
            mentionCondition(R.string.very_low)
        }
        this.mAdapter?.setSelectedPosition(i, binding!!.calendarRecycler)
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

    @Throws(ParseException::class)
    private fun mentionResult(str: String, i: Int, i2: Int) {
        var ovulation = OvulationCalculations.getOvulation(str, i) ?: ""
        var nextPeriod = OvulationCalculations.getNextPeriod(str, i) ?: ""
        this.fertileDays = OvulationCalculations.getFertileWindow(str, i)
        val time = Calendar.getInstance().time
        
        for (dateDetails in detailsList) {
            if (dateDetails == null || dateDetails.ovulationPeriod == null || dateDetails.nextPeriod == null) continue
            if (MyDateUtils.getDateFromString(dateDetails.ovulationPeriod!!, "-").after(time) && 
                MyDateUtils.getDateFromString(dateDetails.nextPeriod!!, "-").after(time)) {
                ovulation = dateDetails.ovulationPeriod!!
                break
            }
        }
        
        this.safeDays = OvulationCalculations.getSafeDays(OvulationCalculations.addDays(str, i), i, i2)
        
        for (next in detailsList) {
            if (next == null || next.nextPeriod == null) continue
            if (MyDateUtils.getDateFromString(next.nextPeriod!!, "-").after(time) && 
                MyDateUtils.checkDate(
                    next.nextPeriod!!,
                    MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                    OvulationCalculations.addDays(MyDateUtils.getCurrentDate("yyyy-MM-dd"), i),
                    "yyyy-MM-dd"
                )) {
                nextPeriod = next.nextPeriod!!
                break
            }
        }

        for (next2 in detailsList) {
            if (next2 == null) continue
            val safeParts = next2.safeDays?.split(" --- ") ?: emptyList()
            if (safeParts.size >= 2) {
                if (MyDateUtils.getDateFromString(safeParts[1], "-").after(time) && 
                    MyDateUtils.checkDate(
                        safeParts[1],
                        MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                        OvulationCalculations.addDays(MyDateUtils.getCurrentDate("yyyy-MM-dd"), i),
                        "yyyy-MM-dd"
                    )) {
                    this.safeDays = next2.safeDays
                    break
                }
            }
        }

        for (next3 in detailsList) {
            if (next3 == null || next3.ovulationPeriod == null) continue
            if (MyDateUtils.getDateFromString(next3.ovulationPeriod!!, "-").after(time) && 
                MyDateUtils.checkDate(
                    next3.ovulationPeriod!!,
                    MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                    OvulationCalculations.addDays(MyDateUtils.getCurrentDate("yyyy-MM-dd"), i),
                    "yyyy-MM-dd"
                )) {
                ovulation = next3.ovulationPeriod!!
                break
            }
        }

        for (next4 in detailsList) {
            if (next4 == null) continue
            val fertileParts = next4.fertileDays?.split(" --- ") ?: emptyList()
            if (fertileParts.isNotEmpty()) {
                if (MyDateUtils.getDateFromString(fertileParts[0], "-").after(time) && 
                    MyDateUtils.checkDate(
                        fertileParts[0],
                        MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                        OvulationCalculations.addDays(MyDateUtils.getCurrentDate("yyyy-MM-dd"), i),
                        "yyyy-MM-dd"
                    )) {
                    this.fertileDays = next4.fertileDays
                    break
                }
            }
        }

        val arrayList = mutableListOf<OvulationData>()
        arrayList.add(
            OvulationData(
                R.drawable.ic_next_period,
                getString(R.string.next_period),
                MyDateUtils.convertInto_yyyy_MMM_dd(nextPeriod, "yyyy-MM-dd", "MMM dd"),
                OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), nextPeriod).toString(),
                "0",
                R.color.next_period_bg_color,
                R.color.next_period_front_color
            )
        )
        arrayList.add(
            OvulationData(
                R.drawable.ic_next_ovulation,
                getString(R.string.next_ovulation),
                MyDateUtils.convertInto_yyyy_MMM_dd(ovulation, "yyyy-MM-dd", "MMM dd"),
                OvulationCalculations.daysBetweenTwoDates(MyDateUtils.getCurrentDate("yyyy-MM-dd"), ovulation).toString(),
                "0",
                R.color.next_ovulation_bg_color,
                R.color.next_ovulation_front_color
            )
        )
        arrayList.add(
            OvulationData(
                R.drawable.ic_fertile_days,
                getString(R.string.fertile_days),
                MyDateUtils.convertInto_yyyy_MMM_dd(this.fertileDays ?: "", "yyyy-MM-dd", "dd MMM"),
                OvulationCalculations.daysBetweenTwoDates(
                    MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                    this.fertileDays?.split(" --- ")?.get(0) ?: ""
                ).toString(),
                "0",
                R.color.fertile_days_bg_color,
                R.color.fertile_days_front_color
            )
        )
        arrayList.add(
            OvulationData(
                R.drawable.ic_safe_days,
                getString(R.string.safe_days),
                MyDateUtils.convertInto_yyyy_MMM_dd(this.safeDays ?: "", "yyyy-MM-dd", "dd MMM"),
                OvulationCalculations.daysBetweenTwoDates(
                    MyDateUtils.getCurrentDate("yyyy-MM-dd"),
                    this.safeDays?.split(" --- ")?.get(0) ?: ""
                ).toString(),
                "0",
                R.color.safe_days_bg_color,
                R.color.safe_days_front_color
            )
        )

        binding?.homeRecyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = HomeRecyclerAdapter(arrayList, requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
