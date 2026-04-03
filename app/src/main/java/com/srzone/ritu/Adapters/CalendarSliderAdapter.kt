package com.srzone.ritu.Adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Adapters.CalendarSliderAdapter.DateViewHolder
import com.srzone.ritu.R
import com.srzone.ritu.ThemesFiles.MyThemeHandler
import com.srzone.ritu.Utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class CalendarSliderAdapter(
    private val mDates: MutableList<Date?>,
    i: Int,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<DateViewHolder?>() {
    private val currentDatePos = 0
    private val mMiddlePosition: Int
    private var mSelectedPosition: Int
    private val themeColor: Int

    protected abstract fun onDateClicked(i: Int)

    init {
        this.mSelectedPosition = -1
        this.mMiddlePosition = mDates.size / 2
        this.mSelectedPosition = i
        recyclerView.smoothScrollToPosition(i)
        val context = recyclerView.context
        this.themeColor = R.color.colorPrimary  // just the resource ID //
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.calendar_slider_layout, viewGroup, false)
        )
    }

    override fun onBindViewHolder(dateViewHolder: DateViewHolder, i: Int) {
        dateViewHolder.bind(this.mDates[i]!!, i == this.mSelectedPosition)
        dateViewHolder.itemView.setOnClickListener {
            val pos = dateViewHolder.adapterPosition  // ✅ use this instead of 'i'
            if (pos != RecyclerView.NO_ID.toInt()) {
                onDateClicked(pos)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.mDates.size
    }

    fun setSelectedPosition(i: Int, recyclerView: RecyclerView?) {
        val i2 = this.mSelectedPosition
        if (i != i2) {
            this.mSelectedPosition = i
            if (recyclerView == null || recyclerView.isComputingLayout()) {
                return
            }
            notifyItemChanged(i2)
            notifyItemChanged(this.mSelectedPosition)
        }
    }


    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cardView: CardView
        private val dateTv: TextView
        private val dateTvBg: LinearLayout
        private val dayTv: TextView

        init {
            this.dateTv = view.findViewById<View?>(R.id.dateTv) as TextView
            this.dayTv = view.findViewById<View?>(R.id.dayTv) as TextView
            this.cardView = view.findViewById<View?>(R.id.cardView) as CardView
            this.dateTvBg = view.findViewById<View?>(R.id.dateTvBg) as LinearLayout
        }

        fun bind(date: Date, z: Boolean) {
            val format = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
            val format2 = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            this.dayTv.text = format
            this.dateTv.text = format2
            Utils.setTint(this.dateTvBg, this@CalendarSliderAdapter.themeColor)

            if (z) {
                Utils.setTint(this.dateTvBg, R.color.white)
                this.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(dateTv.context, this@CalendarSliderAdapter.themeColor)
                )
                this.dayTv.setTextColor(ContextCompat.getColor(dayTv.context, R.color.white))
                this.dateTv.setTextColor(
                    ContextCompat.getColor(dayTv.context, this@CalendarSliderAdapter.themeColor)
                )
                this.cardView.cardElevation = 0.0f
            } else {
                this.dateTv.setTextColor(ContextCompat.getColor(dayTv.context, R.color.white))
                this.dayTv.setTextColor(ContextCompat.getColor(dayTv.context, R.color.black))
                this.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(dateTv.context, android.R.color.white) // ✅ proper resource
                )
                this.cardView.cardElevation = 10.0f
            }
        }
    }
}
