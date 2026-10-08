package dev.yashh.devhub.feature.feed.repository

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.model.Repository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getTrendingRepositories(language: String?): Flow<DataResult<ImmutableList<Repository>, DataError>>
    suspend fun toggleBookmark(repositoryId: Long)
}
