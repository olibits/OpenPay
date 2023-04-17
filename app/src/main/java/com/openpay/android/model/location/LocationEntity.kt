package com.openpay.android.model.location

import java.text.DateFormat
import java.util.*

/**
 * Data class for Location related data (only takes what's needed from
 * {@link android.location.Location} class).
 */

data class LocationEntity(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val foreground: Boolean = true,
    val date: Date = Date()
) {

    fun getStringDate(): String {
        return DateFormat.getDateTimeInstance().format(date)
    }

    override fun toString(): String {
        val appState = if (foreground) {
            "in app"
        } else {
            "in BG"
        }

        return "$latitude, $longitude $appState on " +
                "${DateFormat.getDateTimeInstance().format(date)}.\n"
    }
}
