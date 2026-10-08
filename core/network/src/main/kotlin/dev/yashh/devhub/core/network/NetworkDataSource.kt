package dev.yashh.devhub.core.network

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.mapper.toDomain
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkDataSource {
    suspend fun fetchTrendingRepositories(language: String?): List<Repository>
    suspend fun searchRepositories(query: String): List<Repository>
    suspend fun getRepository(owner: String, repo: String): Repository
    suspend fun getReadme(owner: String, repo: String): String
}

@Singleton
class KtorNetworkDataSource @Inject constructor(
    private val apiService: GitHubApiService
) : NetworkDataSource {

    override suspend fun fetchTrendingRepositories(language: String?): List<Repository> {
        val networkList = apiService.getTrendingRepositories(language = language)
        return networkList.map { it.toDomain() }
    }

    override suspend fun searchRepositories(query: String): List<Repository> {
        val response = apiService.searchRepositories(query = query)
        return response.items.map { it.toDomain() }
    }

    override suspend fun getRepository(owner: String, repo: String): Repository {
        val networkRepo = apiService.getRepository(owner = owner, repo = repo)
        return networkRepo.toDomain()
    }

    override suspend fun getReadme(owner: String, repo: String): String {
        val readmeDto = apiService.getReadme(owner = owner, repo = repo)
        return readmeDto.content
    }
}
