package com.sp.uxpulse.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

internal object TimeZone {
    /**
     * Retrieves a correctly-formatted timestamp string; this function returns an all 0 string
     * except for the timezoneOffset backend platform only processes timezone offset from this
     * string and it is wasted cycles to provide the rest of the data.
     */
    val TIMESTAMP_TIMEZONE_OFFSET: String

    init {
        TIMESTAMP_TIMEZONE_OFFSET = getCurrentTimestampWithTimezoneOffset()
    }

    private fun getCurrentTimestampWithTimezoneOffset(): String {
        val cal: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

        // Get the current date and time
        val currentDateTime = dateFormat.format(cal.time)

        // Calculate the timezone offset in minutes
        val offsetInMillis = (cal.get(Calendar.ZONE_OFFSET) * -1).toLong() - cal.get(Calendar.DST_OFFSET)
        val offsetInMinutes = TimeUnit.MILLISECONDS.toMinutes(offsetInMillis)

        // Format the current date and time with the timezone offset
        return "$currentDateTime $offsetInMinutes"
    }
}