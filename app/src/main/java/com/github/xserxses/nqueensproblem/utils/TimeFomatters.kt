package com.github.xserxses.nqueensproblem.utils

import java.util.Locale
import kotlin.time.Duration

fun formatGameTime(duration: Duration): String {
    if (duration.isNegative()) {
        return "00:00:00"
    }

    val hours = duration.inWholeHours
    val minutes = duration.inWholeMinutes % 60 // Minutes part of the hour
    val seconds = duration.inWholeSeconds % 60 // Seconds part of the minute

    return String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d",
        hours,
        minutes,
        seconds
    )
}
