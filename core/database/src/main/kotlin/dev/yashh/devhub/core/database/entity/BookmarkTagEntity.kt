package dev.yashh.devhub.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_tags")
data class BookmarkTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookmarkId: Long, // Maps to the repositoryId in BookmarkEntity
    val name: String,
    val colorArgb: Long
)
