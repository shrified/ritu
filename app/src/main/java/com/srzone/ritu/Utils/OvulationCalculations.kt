package com.srzone.ritu.Utils

import com.srzone.ritu.Databases.Entities.DateDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object OvulationCalculations {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private const val RANGE_SEPARATOR = " --- "

    fun addDays(str: String, i: Int): String {
        if (str.isEmpty()) return ""
        if (str.contains(RANGE_SEPARATOR)) {
            val parts = str.split(RANGE_SEPARATOR)
            if (parts.size >= 2) {
                return "${addDays(parts[0].trim(), i)}$RANGE_SEPARATOR${addDays(parts[1].trim(), i)}"
            }
        } else if (str.contains(" - ")) {
            val parts = str.split(" - ")
            if (parts.size >= 2) {
                return "${addDays(parts[0].trim(), i)}$RANGE_SEPARATOR${addDays(parts[1].trim(), i)}"
            }
        }
        return try {
            LocalDate.parse(str.trim(), formatter).plusDays(i.toLong()).format(formatter)
        } catch (e: Exception) { str }
    }

    fun minusDays(str: String?, i: Int): String {
        if (str.isNullOrEmpty()) return ""
        if (str.contains(RANGE_SEPARATOR)) {
            val parts = str.split(RANGE_SEPARATOR)
            if (parts.size >= 2) {
                return "${minusDays(parts[0].trim(), i)}$RANGE_SEPARATOR${minusDays(parts[1].trim(), i)}"
            }
        } else if (str.contains(" - ")) {
            val parts = str.split(" - ")
            if (parts.size >= 2) {
                return "${minusDays(parts[0].trim(), i)}$RANGE_SEPARATOR${minusDays(parts[1].trim(), i)}"
            }
        }
        return try {
            LocalDate.parse(str.trim(), formatter).minusDays(i.toLong()).format(formatter)
        } catch (e: Exception) { str }
    }

    fun minusDays(dateDetails: DateDetails, i: Int): DateDetails {
        dateDetails.fertileDays = minusDays(dateDetails.fertileDays, i)
        dateDetails.safeDays = minusDays(dateDetails.safeDays, i)
        dateDetails.ovulationPeriod = minusDays(dateDetails.ovulationPeriod, i)
        dateDetails.nextPeriod = minusDays(dateDetails.nextPeriod, i)
        return dateDetails
    }

    fun getOvulation(lastPeriodStart: String?, cycleLength: Int): String {
        if (lastPeriodStart == null) return ""
        return try {
            LocalDate.parse(lastPeriodStart, formatter)
                .plusDays(cycleLength.toLong())
                .minusDays(14)
                .format(formatter)
        } catch (e: Exception) { "" }
    }

    fun getNextPeriod(lastPeriodStart: String?, cycleLength: Int): String {
        if (lastPeriodStart == null) return ""
        return try {
            LocalDate.parse(lastPeriodStart, formatter)
                .plusDays(cycleLength.toLong())
                .format(formatter)
        } catch (e: Exception) { "" }
    }

    // FIX 1: fertile window now ends on ovulation + 1 day (egg survives 12-24hrs after ovulation)
    fun getFertileWindow(lastPeriodStart: String?, cycleLength: Int): String {
        if (lastPeriodStart == null) return ""
        return try {
            val ovulationDay = LocalDate.parse(getOvulation(lastPeriodStart, cycleLength), formatter)
            val fertileStart = ovulationDay.minusDays(5).format(formatter)
            val fertileEnd = ovulationDay.plusDays(1).format(formatter)  // was: ovulationDay (no +1)
            "$fertileStart$RANGE_SEPARATOR$fertileEnd"
        } catch (e: Exception) { "" }
    }

    // FIX 2: now returns BOTH safe windows (pre-fertile and post-ovulation) separated by " | "
    // Pre-fertile window:  period end → day before fertile window starts
    // Post-ovulation window: ovulation + 2 → day before next period
    fun getSafeDays(lastPeriodStart: String?, cycleLength: Int, periodLength: Int): String {
        if (lastPeriodStart == null) return ""
        return try {
            val periodStart = LocalDate.parse(lastPeriodStart, formatter)
            val fertileWindowStr = getFertileWindow(lastPeriodStart, cycleLength)
            val fertileStart = LocalDate.parse(fertileWindowStr.split(RANGE_SEPARATOR)[0].trim(), formatter)
            val ovulationDay = LocalDate.parse(getOvulation(lastPeriodStart, cycleLength), formatter)
            val nextPeriodStart = periodStart.plusDays(cycleLength.toLong())

            // Pre-fertile safe window: day after period ends → day before fertile window
            val preSafeStart = periodStart.plusDays(periodLength.toLong())
            val preSafeEnd = fertileStart.minusDays(1)

            // Post-ovulation safe window: ovulation + 2 → day before next period
            val postSafeStart = ovulationDay.plusDays(2)
            val postSafeEnd = nextPeriodStart.minusDays(1)

            val hasPreWindow = !preSafeEnd.isBefore(preSafeStart)
            val hasPostWindow = !postSafeEnd.isBefore(postSafeStart)

            when {
                hasPreWindow && hasPostWindow ->
                    "${preSafeStart.format(formatter)}$RANGE_SEPARATOR${preSafeEnd.format(formatter)}" +
                            " | " +
                            "${postSafeStart.format(formatter)}$RANGE_SEPARATOR${postSafeEnd.format(formatter)}"
                hasPreWindow ->
                    "${preSafeStart.format(formatter)}$RANGE_SEPARATOR${preSafeEnd.format(formatter)}"
                hasPostWindow ->
                    "${postSafeStart.format(formatter)}$RANGE_SEPARATOR${postSafeEnd.format(formatter)}"
                else -> ""
            }
        } catch (e: Exception) { "" }
    }

    fun getPregnancyTest(lastPeriodStart: String?, cycleLength: Int): String {
        val ovulation = getOvulation(lastPeriodStart, cycleLength)
        return if (ovulation.isNotEmpty()) {
            LocalDate.parse(ovulation, formatter).plusDays(14).format(formatter)
        } else { "" }
    }

    fun daysBetweenTwoDates(str: String, str2: String): Long {
        if (str.isEmpty() || str2.isEmpty()) return 0L
        return try {
            val date1 = LocalDate.parse(str, formatter)
            val date2 = LocalDate.parse(str2, formatter)
            val between = ChronoUnit.DAYS.between(date1, date2)
            if (between < 0) 0L else between
        } catch (e: Exception) { 0L }
    }

    // FIX 3: due date uses simple day arithmetic to avoid month-length edge cases
    // Naegele's Rule: LMP + 280 days (for 28-day cycle), adjusted for actual cycle length
    fun calculateDueDate(lastPeriodStart: String?, cycleLength: Int): String {
        if (lastPeriodStart == null) return ""
        return try {
            val date = LocalDate.parse(lastPeriodStart, formatter)
            val cycleDiff = (cycleLength - 28).toLong()
            date.plusDays(280 + cycleDiff).format(formatter)  // was: .plusDays(7).plusDays(cycleDiff).minusMonths(3).plusYears(1)
        } catch (e: Exception) { "" }
    }
}