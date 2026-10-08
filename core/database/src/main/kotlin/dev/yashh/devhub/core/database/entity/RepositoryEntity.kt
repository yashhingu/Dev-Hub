package dev.yashh.devhub.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val ownerId: Long,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val ownerHtmlUrl: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val language: String?,
    val languageColor: String?,
    val openIssuesCount: Int,
    val topics: List<String>,
    val htmlUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val isBookmarked: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)
