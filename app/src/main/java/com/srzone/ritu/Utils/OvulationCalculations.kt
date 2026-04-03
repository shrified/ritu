package com.srzone.ritu.Utils

import com.srzone.ritu.Databases.Entities.DateDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object OvulationCalculations {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun calculateDueDate(str: String?, i: Int): String {
        if (str == null) return ""
        val date = LocalDate.parse(str, formatter)
        val cycleDiff = (i - 28).toLong()
        return date.plusDays(cycleDiff).plusDays(7).plusMonths(9).format(formatter)
    }

    fun addDays(str: String, i: Int): String {
        if (str.contains("---")) {
            val parts = str.split(" --- ")
            if (parts.size >= 2) {
                return "${addDays(parts[0], i)} --- ${addDays(parts[1], i)}"
            }
        }
        return try {
            LocalDate.parse(str, formatter).plusDays(i.toLong()).format(formatter)
        } catch (e: Exception) {
            str
        }
    }

    fun minusDays(str: String?, i: Int): String {
        if (str == null) return ""
        if (str.contains("---")) {
            val parts = str.split(" --- ")
            if (parts.size >= 2) {
                return "${minusDays(parts[0], i)} --- ${minusDays(parts[1], i)}"
            }
        }
        return try {
            LocalDate.parse(str, formatter).minusDays(i.toLong()).format(formatter)
        } catch (e: Exception) {
            str
        }
    }

    fun minusDays(dateDetails: DateDetails, i: Int): DateDetails {
        dateDetails.fertileDays = minusDays(dateDetails.fertileDays, i)
        dateDetails.safeDays = minusDays(dateDetails.safeDays, i)
        dateDetails.ovulationPeriod = minusDays(dateDetails.ovulationPeriod, i)
        dateDetails.nextPeriod = minusDays(dateDetails.nextPeriod, i)
        return dateDetails
    }

    fun getFertileWindow(str: String?, i: Int): String {
        val ovulation1 = getOvulation(str, i - 2)
        val ovulation2 = getOvulation(str, i + 2)
        return "$ovulation1 --- $ovulation2"
    }

    fun getSafeDays(str: String?, i: Int, i2: Int): String {
        if (str == null) return ""
        val localDate = LocalDate.parse(str, formatter).plusDays(i2.toLong()).format(formatter)
        val fertileStart = getFertileWindow(str, i - 1).split(" --- ")[0]
        return "$localDate --- $fertileStart"
    }

    fun getOvulation(str: String?, i: Int): String {
        if (str == null) return ""
        return try {
            LocalDate.parse(str, formatter).plusDays((i - 14).toLong()).format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    fun getPregnancyTest(str: String?, i: Int): String {
        val ovulation = getOvulation(str, i)
        return if (ovulation.isNotEmpty()) {
            LocalDate.parse(ovulation, formatter).plusDays(9).format(formatter)
        } else {
            ""
        }
    }

    fun getNextPeriod(str: String?, i: Int): String {
        if (str == null) return ""
        return try {
            LocalDate.parse(str, formatter).plusDays(i.toLong()).format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    fun daysBetweenTwoDates(str: String, str2: String): Long {
        return try {
            val date1 = LocalDate.parse(str, formatter)
            val date2 = LocalDate.parse(str2, formatter)
            val between = ChronoUnit.DAYS.between(date1, date2)
            if (between < 0) 0L else between
        } catch (e: Exception) {
            0L
        }
    }
}
