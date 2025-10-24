package com.example.githubuser.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun getTime(timestamp: String): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val parsedTime = Instant.parse(timestamp).atZone(ZoneId.systemDefault())
        val duration = Duration.between(parsedTime, now)

        return when {
            duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
            duration.toHours() < 24 -> "${duration.toHours()} hours ago"
            duration.toDays() < 7 -> "${duration.toDays()} days ago"
            duration.toDays() < 30 -> "${duration.toDays() / 7} weeks ago"
            duration.toDays() > 30 && duration.toDays() < 365 -> "${duration.toDays() / 30} months ago"
            else -> {
                parseDateLatestApi(timestamp)
            }
        }
    } else {
        return "parseDateOldApi(timestamp)"
    }
}

fun hasLatestUpdateInAMonth(timestamp: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val parsedTime = Instant.parse(timestamp).atZone(ZoneId.systemDefault())
        val duration = Duration.between(parsedTime, now)
        return duration.toDays() < 30
    } else {
        return false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun parseDateLatestApi(timestamp: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
    val dateInput = LocalDate.parse(timestamp, inputFormatter)
    val dateOutput = dateInput.format(outputFormatter)
    return dateOutput
}

@Composable
fun parseDateOldApi(isoString: String): String {
    var date: Date?
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        date = simpleDateFormat.parse(isoString)
    } catch (e: Exception) {
        date = null
    }

    if (date == null) return "Invalid Date"
    val outputFrmt = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return outputFrmt.format(date)
}
