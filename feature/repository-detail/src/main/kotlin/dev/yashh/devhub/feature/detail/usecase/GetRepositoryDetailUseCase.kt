package dev.yashh.devhub.feature.detail.usecase

import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.NetworkDataSource
import javax.inject.Inject

class GetRepositoryDetailUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend operator fun invoke(owner: String, repoName: String): Repository {
        return networkDataSource.getRepository(owner, repoName)
    }
}
