package com.srzone.ritu.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.srzone.ritu.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class CalendarSliderAdapter(
    private val mDates: MutableList<Date?>,
    initialPosition: Int,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<CalendarSliderAdapter.DateViewHolder>() {
    private var mSelectedPosition: Int = initialPosition
    private val themeColor: Int = R.color.app_primary_color

    protected abstract fun onDateClicked(position: Int)

    init {
        recyclerView.post {
            recyclerView.smoothScrollToPosition(initialPosition)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.calendar_slider_layout, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = mDates[position] ?: return
        holder.bind(date, position == mSelectedPosition)
        holder.itemView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDateClicked(pos)
            }
        }
    }

    override fun getItemCount(): Int = mDates.size

    fun setSelectedPosition(position: Int, recyclerView: RecyclerView?) {
        val oldPosition = mSelectedPosition
        if (position != oldPosition) {
            mSelectedPosition = position
            if (recyclerView == null || recyclerView.isComputingLayout) return
            notifyItemChanged(oldPosition)
            notifyItemChanged(mSelectedPosition)
        }
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cardView: MaterialCardView = view.findViewById(R.id.cardView)
        private val dateTv: TextView = view.findViewById(R.id.dateTv)
        private val dateTvBg: MaterialCardView = view.findViewById(R.id.dateTvBg)
        private val dayTv: TextView = view.findViewById(R.id.dayTv)

        fun bind(date: Date, isSelected: Boolean) {
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            
            dayTv.text = dayFormat
            dateTv.text = dateFormat

            if (isSelected) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.app_primary_color))
                dateTvBg.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                dayTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                dateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.app_primary_color))
                cardView.cardElevation = 8f
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                dateTvBg.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.surface_background))
                dayTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                dateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_primary))
                cardView.cardElevation = 0f
            }
        }
    }
}
