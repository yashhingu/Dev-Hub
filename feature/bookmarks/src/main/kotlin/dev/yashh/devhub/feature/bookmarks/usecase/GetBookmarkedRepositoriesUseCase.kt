package dev.yashh.devhub.feature.bookmarks.usecase

import dev.yashh.devhub.core.database.LocalDataSource
import dev.yashh.devhub.core.model.Repository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBookmarkedRepositoriesUseCase @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    operator fun invoke(): Flow<ImmutableList<Repository>> {
        return localDataSource.getAllBookmarks().map { it.toImmutableList() }
    }
}
