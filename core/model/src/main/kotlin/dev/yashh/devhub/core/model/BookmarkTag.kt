package dev.yashh.devhub.core.model

import androidx.compose.runtime.Immutable

/**
 * Custom tags applied to bookmarked repositories to organize them.
 */
@Immutable
data class BookmarkTag(
    val id: Long = 0,
    val name: String,
    val color: Long  // ARGB color for rendering chip backgrounds
)
