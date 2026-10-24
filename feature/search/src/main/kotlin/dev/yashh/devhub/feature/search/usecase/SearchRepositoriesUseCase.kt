package dev.yashh.devhub.feature.search.usecase

import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.NetworkDataSource
import dev.yashh.devhub.core.database.LocalDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend operator fun invoke(query: String): ImmutableList<Repository> {
        val result = networkDataSource.searchRepositories(query)
        return result.map { repository ->
            repository.copy(isBookmarked = localDataSource.getRepository(repository.id)?.isBookmarked == true)
        }.toImmutableList()
    }
}
