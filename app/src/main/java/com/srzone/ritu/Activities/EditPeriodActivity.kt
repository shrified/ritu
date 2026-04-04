package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.srzone.ritu.Databases.Entities.DateDetails
import com.srzone.ritu.Databases.OvulationDetailsHandler
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.R
import com.srzone.ritu.Utils.OvulationCalculations
import com.srzone.ritu.Utils.SharedPreferenceUtils
import com.srzone.ritu.databinding.ActivityEditPeriodBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditPeriodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPeriodBinding
    private lateinit var handler: OvulationDetailsHandler
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPeriodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = OvulationDetailsHandler(this)

        // 1. Initialize CalendarView with existing saved date
        val savedDateStr = SharedPreferenceUtils.getDate(this)
        if (savedDateStr.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(savedDateStr)
                if (date != null) {
                    selectedCalendar.time = date
                    binding.calendarView.date = selectedCalendar.timeInMillis
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedCalendar.set(year, month, dayOfMonth)
        }

        binding.cancelBtn.setOnClickListener { finish() }

        binding.saveBtn.setOnClickListener { saveData() }
    }

    private fun saveData() {
        binding.cancelBtn.visibility = View.GONE
        binding.saveBtn.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val newDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selectedCalendar.time)
        
        // Get existing settings
        val cycles = SharedPreferenceUtils.getCycles(this).ifEmpty { "28" }.toInt()
        val cycleLength = SharedPreferenceUtils.getCycleLength(this).ifEmpty { "5" }.toInt()

        // 2. Update SharedPreferences - This is the "base" for all logic
        SharedPreferenceUtils.saveData(
            cycles.toString(),
            newDateStr,
            cycleLength.toString(),
            this
        )

        // 3. Clear old database entries to avoid duplicate indicators on calendar/home
        deleteDatabase(Params.DB_NAME_DETAILS)
        
        // 4. Re-initialize the handler since the database was just deleted
        handler = OvulationDetailsHandler(this)

        // 5. Repopulate database with new cycles (matching onboarding logic)
        saveCyclesToDatabase(newDateStr, cycles, cycleLength)

        Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
        
        // 6. Return to MainActivity and clear top to force refresh of all fragments
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }

    private fun saveCyclesToDatabase(dateStr: String, cycles: Int, periodLength: Int) {
        val ovulation = OvulationCalculations.getOvulation(dateStr, cycles)
        val nextPeriod = OvulationCalculations.getNextPeriod(dateStr, cycles)
        val fertileWindow = OvulationCalculations.getFertileWindow(dateStr, cycles)
        val safeDays = OvulationCalculations.getSafeDays(dateStr, cycles, periodLength)

        // Home Screen Logic (Pre-calculating multiple cycles forward and backward)
        for (i in 0..11) {
            val offset = cycles * i
            
            // Forward cycles
            handler.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(fertileWindow, offset),
                    OvulationCalculations.addDays(safeDays, offset),
                    OvulationCalculations.addDays(nextPeriod, offset),
                    OvulationCalculations.addDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_HOME
            )
            
            // Backward cycles
            handler.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.minusDays(fertileWindow, offset),
                    OvulationCalculations.minusDays(safeDays, offset),
                    OvulationCalculations.minusDays(nextPeriod, offset),
                    OvulationCalculations.minusDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_HOME
            )
        }

        // Calendar View Logic
        for (i in 0..5) {
            val offset = cycles * i
            handler.addOvulationDetail(
                DateDetails(
                    OvulationCalculations.addDays(fertileWindow, offset),
                    OvulationCalculations.addDays(safeDays, offset),
                    OvulationCalculations.addDays(nextPeriod, offset),
                    OvulationCalculations.addDays(ovulation, offset)
                ), Params.OVULATION_DETAILS_TABLE_CALENDAR
            )
        }
        
        // Small backward window for calendar
        for (i in 0..1) {
            val offset = cycles * i
            handler.addOvulationDetail(
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
