package dev.yashh.devhub.core.model

import androidx.compose.runtime.Immutable

/**
 * Domain representation of a repository owner/user.
 */
@Immutable
data class Owner(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
)
