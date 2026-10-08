package dev.yashh.devhub.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookmarkWithTags(
    @Embedded val bookmark: BookmarkEntity,
    @Relation(
        parentColumn = "repositoryId",
        entityColumn = "bookmarkId"
    )
    val tags: List<BookmarkTagEntity>,
    @Relation(
        parentColumn = "repositoryId",
        entityColumn = "id"
    )
    val repository: RepositoryEntity
)
