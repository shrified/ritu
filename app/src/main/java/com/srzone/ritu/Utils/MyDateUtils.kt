package com.srzone.ritu.Utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MyDateUtils {
    fun getCurrentDate(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(Calendar.getInstance().time)
    }

    fun getDateFromString(str: String?, separator: String): Date {
        if (str == null) return Date()
        try {
            val split = str.split(separator.toRegex()).dropLastWhile { it.isEmpty() }
            if (split.size < 3) return Date()
            
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, split[0].toInt())
            calendar.set(Calendar.MONTH, split[1].toInt() - 1)
            calendar.set(Calendar.DAY_OF_MONTH, split[2].toInt())
            return calendar.time
        } catch (e: Exception) {
            return Date()
        }
    }

    fun getAllDatesStrInRange(date: Date, endDate: Date?): MutableList<String?> {
        val dateList = mutableListOf<String?>()
        if (endDate == null) return dateList
        
        val calendar = Calendar.getInstance()
        calendar.time = date
        
        while (calendar.time.before(endDate) || isSameDay(calendar.time, endDate)) {
            dateList.add(SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dateList
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun convertInto_yyyy_MMM_dd(str: String, inputPattern: String?, outputPattern: String?): String? {
        if (inputPattern == null || outputPattern == null) return null
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
        
        try {
            if (str.contains(" --- ")) {
                val parts = str.split(" --- ".toRegex()).dropLastWhile { it.isEmpty() }
                if (parts.size >= 2) {
                    val part1 = convertInto_yyyy_MMM_dd(parts[0], inputPattern, outputPattern)
                    val part2 = convertInto_yyyy_MMM_dd(parts[1], inputPattern, outputPattern)
                    return "$part1 --- $part2"
                }
            }
            val date = inputFormat.parse(str)
            return if (date != null) outputFormat.format(date) else null
        } catch (unused: ParseException) {
            return null
        }
    }

    @Throws(ParseException::class)
    fun checkDate(dateToCheck: String, startDate: String, endDate: String, pattern: String?): Boolean {
        val format = SimpleDateFormat(pattern ?: "yyyy-MM-dd", Locale.ENGLISH)
        val start = format.parse(startDate) ?: return false
        val end = format.parse(endDate) ?: return false
        val target = format.parse(dateToCheck) ?: return false
        
        return (target.after(start) || target == start) && (target.before(end) || target == end)
    }
}
