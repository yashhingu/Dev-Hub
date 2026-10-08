package dev.yashh.devhub.feature.bookmarks.usecase

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

class ManageTagsUseCase @Inject constructor() {
    suspend fun getAllTags(): ImmutableList<String> {
        return persistentListOf("Kotlin", "Android", "Architecture")
    }
}
