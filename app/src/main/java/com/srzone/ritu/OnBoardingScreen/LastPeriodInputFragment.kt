package com.srzone.ritu.OnBoardingScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
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
    var binding: FragmentLastPeriodInputBinding? = null
    var handler: OvulationDetailsHandler? = null
    var selectedDate: Calendar? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        this.binding = FragmentLastPeriodInputBinding.inflate(layoutInflater)
        this.handler = OvulationDetailsHandler(getActivity())
        val inputActivity = requireActivity() as InputActivity
        selectedDate = Calendar.getInstance()
        this.binding!!.calendarView.setOnDateChangeListener(object : OnDateChangeListener {
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                selectedDate!!.set(year, month, dayOfMonth)
            }
        })

        this.binding!!.backSessionBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                (inputActivity.findViewById<View?>(R.id.viewPager) as ViewPager2).setCurrentItem(
                    1,
                    true
                )
            }
        })








        this.binding!!.getStartedBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@LastPeriodInputFragment.m142x23ec2a7(inputActivity, view)
            }
        })
        return this.binding!!.getRoot()
    }


    fun m142x23ec2a7(inputActivity: InputActivity, view: View?) {
        this.binding!!.progressBar.setVisibility(View.VISIBLE)
        this.binding!!.backSessionBtn.setVisibility(View.GONE)
        this.binding!!.getStartedBtn.setVisibility(View.GONE)
        requireActivity().deleteDatabase(Params.DB_NAME_DETAILS)
        requireActivity().deleteDatabase(Params.DB_NAME_NOTES)


        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selectedDate!!.getTime())
        SharedPreferenceUtils.saveData(
            inputActivity.cyclesLength.toString(),
            format,
            inputActivity.periodLength.toString(),
            inputActivity
        )
        saveDateToDatabase(format, inputActivity)
        startActivity(Intent(inputActivity, MainActivity::class.java))
        inputActivity.finishAffinity()
    }

    private fun saveDateToDatabase(str: String?, inputActivity: InputActivity) {
        val i = inputActivity.cyclesLength
        val i2 = inputActivity.periodLength
        val ovulation = OvulationCalculations.getOvulation(str, i)
        val nextPeriod = OvulationCalculations.getNextPeriod(str, i)
        val fertileWindow = OvulationCalculations.getFertileWindow(str, i)
        val safeDays = OvulationCalculations.getSafeDays(str, i, i2)
        for (i3 in 0..11) {
            val i4 = i * i3
            this.handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(
                        fertileWindow,
                        i4
                    ),
                    OvulationCalculations.addDays(safeDays, i4),
                    OvulationCalculations.addDays(nextPeriod, i4),
                    OvulationCalculations.addDays(ovulation, i4)
                ), Params.OVULATION_DETAILS_TABLE_HOME
            )
            this.handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.minusDays(
                        fertileWindow,
                        i4
                    ),
                    OvulationCalculations.minusDays(safeDays, i4),
                    OvulationCalculations.minusDays(nextPeriod, i4),
                    OvulationCalculations.minusDays(ovulation, i4)
                ), Params.OVULATION_DETAILS_TABLE_HOME
            )
        }
        for (i5 in 0..5) {
            val i6 = i * i5
            this.handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(
                        fertileWindow,
                        i6
                    ),
                    OvulationCalculations.addDays(safeDays, i6),
                    OvulationCalculations.addDays(nextPeriod, i6),
                    OvulationCalculations.addDays(ovulation, i6)
                ), Params.OVULATION_DETAILS_TABLE_CALENDAR
            )
        }
        for (i7 in 0..1) {
            val i8 = i * i7
            this.handler!!.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.minusDays(
                        fertileWindow,
                        i8
                    ),
                    OvulationCalculations.minusDays(safeDays, i8),
                    OvulationCalculations.minusDays(nextPeriod, i8),
                    OvulationCalculations.minusDays(ovulation, i8)
                ), Params.OVULATION_DETAILS_TABLE_CALENDAR
            )
        }
    }
}
