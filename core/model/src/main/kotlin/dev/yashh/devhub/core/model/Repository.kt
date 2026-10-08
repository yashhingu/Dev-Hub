package dev.yashh.devhub.core.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/**
 * Domain representation of a Repository.
 * Using @Immutable to guarantee stable state for Compose rendering.
 */
@Immutable
data class Repository(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val owner: Owner,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?,
    val languageColor: String?,
    val openIssuesCount: Int,
    val topics: ImmutableList<String>,
    val htmlUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val isBookmarked: Boolean = false
)
