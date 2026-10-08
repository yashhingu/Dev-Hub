package dev.yashh.devhub.feature.feed.usecase

import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.feature.feed.repository.FeedRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTrendingRepositoriesUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
    private val dispatchers: DispatcherProvider
) {
    /**
     * Fetches trending repositories based on an optional language filter.
     * Uses offline-first approach managed by the repository.
     */
    operator fun invoke(language: String? = null): Flow<DataResult<ImmutableList<Repository>, DataError>> {
        return feedRepository.getTrendingRepositories(language)
            .flowOn(dispatchers.io) // Ensure we operate on IO thread
    }
}
