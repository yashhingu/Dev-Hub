package dev.yashh.devhub.feature.search.usecase

import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.NetworkDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend operator fun invoke(query: String): ImmutableList<Repository> {
        val result = networkDataSource.searchRepositories(query)
        return result.toImmutableList()
    }
}
