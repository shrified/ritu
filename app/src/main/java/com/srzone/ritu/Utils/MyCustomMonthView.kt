package com.srzone.ritu.Utils

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer
import com.srzone.ritu.R

/**
 * This class was originally written for a different calendar library.
 * With com.kizitonwose.calendar, we use MonthDayBinder and ViewContainer instead.
 * Custom day logic is now handled in CalendarFragment.
 */
class DayViewContainer(view: View) : ViewContainer(view) {
    val textView: TextView = view.findViewById(R.id.dayText)
}
