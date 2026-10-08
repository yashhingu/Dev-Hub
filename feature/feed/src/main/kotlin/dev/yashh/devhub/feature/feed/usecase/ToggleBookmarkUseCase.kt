package dev.yashh.devhub.feature.feed.usecase

import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.feature.feed.repository.FeedRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
    private val dispatchers: DispatcherProvider
) {
    /**
     * Toggles the bookmark status for a specific repository.
     */
    suspend operator fun invoke(repositoryId: Long) {
        withContext(dispatchers.io) {
            feedRepository.toggleBookmark(repositoryId)
        }
    }
}
