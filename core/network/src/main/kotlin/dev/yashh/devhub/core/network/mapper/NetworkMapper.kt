package dev.yashh.devhub.core.network.mapper

import dev.yashh.devhub.core.model.Owner
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.model.SearchResult
import dev.yashh.devhub.core.network.model.NetworkOwner
import dev.yashh.devhub.core.network.model.NetworkRepository
import dev.yashh.devhub.core.network.model.NetworkSearchResponse
import kotlinx.collections.immutable.toImmutableList

fun NetworkOwner.toDomain(): Owner = Owner(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl
)

// Generating random language colors if not provided from the API (since Github API doesn't return colors directly in search)
private fun getLanguageColor(language: String?): String? = language?.let {
    // Simple deterministic color generation
    val hash = it.hashCode()
    val r = (hash and 0xFF0000) shr 16
    val g = (hash and 0x00FF00) shr 8
    val b = hash and 0x0000FF
    String.format("#%02X%02X%02X", r, g, b)
}

fun NetworkRepository.toDomain(): Repository = Repository(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    owner = owner.toDomain(),
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    language = language,
    languageColor = getLanguageColor(language),
    openIssuesCount = openIssuesCount,
    topics = topics.toImmutableList(),
    htmlUrl = htmlUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isBookmarked = false // Initial mapping doesn't have local DB context
)

fun NetworkSearchResponse.toDomain(): SearchResult = SearchResult(
    totalCount = totalCount,
    items = items.map { it.toDomain() }.toImmutableList(),
    isIncomplete = incompleteResults
)
