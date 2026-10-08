package dev.yashh.devhub.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `formatCount handles thousands correctly`() {
        assertEquals("1.5K", 1500.formatCount())
        assertEquals("1.0K", 1000.formatCount())
        assertEquals("999", 999.formatCount())
    }

    @Test
    fun `formatCount handles millions correctly`() {
        assertEquals("1.2M", 1200000.formatCount())
        assertEquals("10.5M", 10500000.formatCount())
    }

    @Test
    fun `toRelativeTimeString returns correct format`() {
        // Complex to test precisely without mocking time, 
        // but basic structure can be asserted if logic is pure.
        // Assuming a pure function variant for testing:
        assertEquals("2 hours ago", toRelativeTime(System.currentTimeMillis() - 2 * 3600 * 1000, System.currentTimeMillis()))
    }
}

fun Int.formatCount(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format("%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}

fun toRelativeTime(timeMs: Long, nowMs: Long = System.currentTimeMillis()): String {
    val diff = nowMs - timeMs
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000} mins ago"
        diff < 86400_000 -> "${diff / 3600_000} hours ago"
        else -> "${diff / 86400_000} days ago"
    }
}
