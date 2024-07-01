package com.sp.uxpulse.analytics

import java.util.Calendar
import java.util.concurrent.TimeUnit

internal object TimeZone {
    /**
     * Retrieves a correctly-formatted timestamp string; this function returns an all 0 string
     * except for the timezoneOffset backend platform only processes timezone offset from this
     * string and it is wasted cycles to provide the rest of the data.
     */
    val TIMESTAMP_TIMEZONE_OFFSET: String

    init {
        val cal: Calendar = Calendar.getInstance()
        TIMESTAMP_TIMEZONE_OFFSET = (
                "00/00/0000 00:00:00 0 "
                        + TimeUnit.MILLISECONDS.toMinutes(
                    (cal.get(Calendar.ZONE_OFFSET) * -1).toLong()
                            - cal.get(Calendar.DST_OFFSET)
                ))
    }
}