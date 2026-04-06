package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
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

class EditPeriodActivity : BaseActivity() {
    private lateinit var binding: ActivityEditPeriodBinding
    private lateinit var handler: OvulationDetailsHandler
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        binding = ActivityEditPeriodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootView = findViewById<View>(R.id.rootLayout)
        val statusBarBackground = findViewById<View>(R.id.statusBarBackground)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { rootView, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            rootView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            statusBarBackground.updateLayoutParams {
                height = systemBars.top
            }
            insets
        }

        handler = OvulationDetailsHandler(this)

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
        
        // Use consistent variable names
        val cycleLength = SharedPreferenceUtils.getCycleLength(this).toInt()
        val periodLength = SharedPreferenceUtils.getPeriodLength(this).toInt()

        SharedPreferenceUtils.saveData(
            cycleLength.toString(),
            newDateStr,
            periodLength.toString(),
            this
        )

        deleteDatabase(Params.DB_NAME_DETAILS)
        handler = OvulationDetailsHandler(this)

        saveCyclesToDatabase(newDateStr, cycleLength, periodLength)

        Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
        
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
    }

    private fun saveCyclesToDatabase(dateStr: String, cycleLength: Int, periodLength: Int) {
        // Correct offsets: i * cycleLength
        for (i in 0..11) {
            val offset = cycleLength * i
            
            // Re-calculate base for each cycle to ensure correct relative windows
            val cycleDate = OvulationCalculations.addDays(dateStr, offset)
            addCycleToDb(cycleDate, cycleLength, periodLength, Params.OVULATION_DETAILS_TABLE_HOME)
            
            if (i > 0) {
                val pastCycleDate = OvulationCalculations.minusDays(dateStr, offset)
                addCycleToDb(pastCycleDate, cycleLength, periodLength, Params.OVULATION_DETAILS_TABLE_HOME)
            }
        }

        for (i in 0..5) {
            val offset = cycleLength * i
            val cycleDate = OvulationCalculations.addDays(dateStr, offset)
            addCycleToDb(cycleDate, cycleLength, periodLength, Params.OVULATION_DETAILS_TABLE_CALENDAR)
        }
        
        for (i in 1..2) {
            val offset = cycleLength * i
            val pastCycleDate = OvulationCalculations.minusDays(dateStr, offset)
            addCycleToDb(pastCycleDate, cycleLength, periodLength, Params.OVULATION_DETAILS_TABLE_CALENDAR)
        }
    }

    private fun addCycleToDb(date: String, cycleLength: Int, periodLength: Int, table: String) {
        val ovulation = OvulationCalculations.getOvulation(date, cycleLength)
        val nextPeriod = OvulationCalculations.getNextPeriod(date, cycleLength)
        val fertileWindow = OvulationCalculations.getFertileWindow(date, cycleLength)
        val safeDays = OvulationCalculations.getSafeDays(date, cycleLength, periodLength)

        handler.addOvulationDetail(
            DateDetails(fertileWindow, safeDays, nextPeriod, ovulation),
            table
        )
    }
}
