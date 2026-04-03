package com.srzone.ritu.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
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
    var binding: ActivityEditPeriodBinding? = null
    var handler: OvulationDetailsHandler? = null

    var selectedDate: Calendar? = null
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val inflate = ActivityEditPeriodBinding.inflate(getLayoutInflater())
        this.binding = inflate
        setContentView(inflate.getRoot())

        //        AdsGoogle adsGoogle = new AdsGoogle( this);
//        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
//        adsGoogle.Interstitial_Show_Counter(this);
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
        this.binding!!.cancelBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@EditPeriodActivity.m110x7f7ce216(view)
            }
        })
        this.handler = OvulationDetailsHandler(this)
        this.binding!!.saveBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@EditPeriodActivity.m111x4f3d15b5(view)
            }
        })
    }


    fun m110x7f7ce216(view: View?) {
        finish()
    }

    fun m111x4f3d15b5(view: View?) {
        saveData()
    }

    private fun saveData() {
        this.binding!!.cancelBtn.setVisibility(View.GONE)
        this.binding!!.saveBtn.setVisibility(View.GONE)
        this.binding!!.progressBar.setVisibility(View.VISIBLE)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(selectedDate!!.getTime())
        val parseInt = SharedPreferenceUtils.getCycles(this).toInt()
        val ovulation = OvulationCalculations.getOvulation(format, parseInt)
        val fertileWindow = OvulationCalculations.getFertileWindow(format, parseInt)
        val safeDays = OvulationCalculations.getSafeDays(
            format,
            parseInt,
            SharedPreferenceUtils.getCycleLength(this).toInt()
        )
        val ovulationDetailsHandler = this.handler!!
        ovulationDetailsHandler.addOvulationDetail(
            DateDetails(
                fertileWindow,
                format + " --- " + OvulationCalculations.minusDays(format, 1),
                format,
                ovulation
            ), Params.OVULATION_DETAILS_TABLE_HOME
        )
        this.handler!!.addOvulationDetail(
            DateDetails(fertileWindow, safeDays, format, ovulation),
            Params.OVULATION_DETAILS_TABLE_CALENDAR
        )
        Toast.makeText(
            this,
            getResources().getString(R.string.saved_successfully),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
