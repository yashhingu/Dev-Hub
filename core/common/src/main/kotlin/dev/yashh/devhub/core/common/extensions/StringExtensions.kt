package dev.yashh.devhub.core.common.extensions

import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Formats a raw count into a human-readable string (e.g., 1200 -> "1.2K", 1500000 -> "1.5M").
 */
fun Int.formatCount(): String {
    if (this < 1000) return this.toString()
    val df = DecimalFormat("#.#")
    return when {
        this < 1_000_000 -> "${df.format(this / 1000.0)}K"
        this < 1_000_000_000 -> "${df.format(this / 1_000_000.0)}M"
        else -> "${df.format(this / 1_000_000_000.0)}B"
    }
}

/**
 * Converts an ISO-8601 date string to a relative time format (e.g., "2 hours ago").
 * NOTE: If the date format is completely bogus, we just return the raw string or a fallback.
 */
fun String.toRelativeTimeString(): String {
    return try {
        val instant = Instant.parse(this)
        val now = Instant.now()
        
        val seconds = ChronoUnit.SECONDS.between(instant, now)
        val minutes = ChronoUnit.MINUTES.between(instant, now)
        val hours = ChronoUnit.HOURS.between(instant, now)
        val days = ChronoUnit.DAYS.between(instant, now)
        
        when {
            seconds < 60 -> "just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hours ago"
            days < 30 -> "$days days ago"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                    .withZone(ZoneId.systemDefault())
                formatter.format(instant)
            }
        }
    } catch (e: Exception) {
        this // Fallback to raw string if parsing fails
    }
}
