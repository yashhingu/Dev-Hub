package dev.yashh.devhub.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/**
 * Domain model wrapping a page of repository search results.
 */
@Immutable
data class SearchResult(
    val totalCount: Int,
    val items: ImmutableList<Repository>,
    val isIncomplete: Boolean
)
