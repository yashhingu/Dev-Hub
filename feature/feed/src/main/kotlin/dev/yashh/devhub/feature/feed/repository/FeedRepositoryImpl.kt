package dev.yashh.devhub.feature.feed.repository

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.database.LocalDataSource
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.NetworkDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : FeedRepository {

    override fun getTrendingRepositories(language: String?): Flow<DataResult<ImmutableList<Repository>, DataError>> = flow {
        // Offline-first approach
        
        // 1. First emit from local cache if available to show UI immediately
        val cachedData = localDataSource.getTrendingRepositories(language)
        if (cachedData.isNotEmpty()) {
            emit(DataResult.Success(cachedData.toImmutableList()))
        }
        
        // 2. Try fetching from network
        try {
            val networkResult = networkDataSource.fetchTrendingRepositories(language)
            
            // 3. Save to local DB cache
            localDataSource.saveTrendingRepositories(networkResult)
            
            // 4. Emit fresh data from local DB (single source of truth)
            val freshData = localDataSource.getTrendingRepositories(language)
            emit(DataResult.Success(freshData.toImmutableList()))
            
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // NOTE: If network fails and we already have cache, we've already emitted it.
            // But we should notify the UI about the failure so it can show a non-intrusive snackbar if needed.
            // If cache was empty, we emit a true Error state.
            if (cachedData.isEmpty()) {
                emit(DataResult.Failure(DataError.NetworkError.NoInternet))
            }
        }
    }

    override suspend fun toggleBookmark(repositoryId: Long) {
        localDataSource.toggleBookmark(repositoryId)
    }
}
