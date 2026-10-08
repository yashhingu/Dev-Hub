package dev.yashh.devhub.feature.bookmarks.usecase

import dev.yashh.devhub.core.database.LocalDataSource
import javax.inject.Inject

class RemoveBookmarkUseCase @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    suspend operator fun invoke(id: Long) {
        localDataSource.removeBookmark(id)
    }
}
