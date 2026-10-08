package dev.yashh.devhub.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val repositoryId: Long,
    val addedAt: Long = System.currentTimeMillis(),
    val notes: String? = null
)
