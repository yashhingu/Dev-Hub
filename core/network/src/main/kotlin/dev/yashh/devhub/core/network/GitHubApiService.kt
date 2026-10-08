package dev.yashh.devhub.core.network

import dev.yashh.devhub.core.network.model.NetworkReadme
import dev.yashh.devhub.core.network.model.NetworkRepository
import dev.yashh.devhub.core.network.model.NetworkSearchResponse

/**
 * Service for abstracting GitHub API calls.
 */
interface GitHubApiService {
    
    suspend fun searchRepositories(
        query: String,
        sort: String? = null,
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): NetworkSearchResponse

    suspend fun getRepository(
        owner: String,
        repo: String
    ): NetworkRepository

    suspend fun getReadme(
        owner: String,
        repo: String
    ): NetworkReadme

    suspend fun getTrendingRepositories(
        language: String? = null,
        since: String = "daily"
    ): List<NetworkRepository>
}
