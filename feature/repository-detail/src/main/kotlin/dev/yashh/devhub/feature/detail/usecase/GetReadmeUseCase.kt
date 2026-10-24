package dev.yashh.devhub.feature.detail.usecase

import dev.yashh.devhub.core.network.NetworkDataSource
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class GetReadmeUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend operator fun invoke(owner: String, repoName: String): String {
        return try {
            networkDataSource.getReadme(owner, repoName)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            "No README available or failed to load."
        }
    }
}
