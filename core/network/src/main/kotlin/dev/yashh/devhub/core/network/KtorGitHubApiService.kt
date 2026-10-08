package dev.yashh.devhub.core.network

import dev.yashh.devhub.core.network.model.NetworkReadme
import dev.yashh.devhub.core.network.model.NetworkRepository
import dev.yashh.devhub.core.network.model.NetworkSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

// NOTE: Ktor abstracts away much of the boilerplate for HTTP calls.
class KtorGitHubApiService @Inject constructor(
    private val client: HttpClient
) : GitHubApiService {

    override suspend fun searchRepositories(
        query: String,
        sort: String?,
        order: String,
        page: Int,
        perPage: Int
    ): NetworkSearchResponse {
        return client.get("search/repositories") {
            parameter("q", query)
            parameter("sort", sort)
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }

    override suspend fun getRepository(owner: String, repo: String): NetworkRepository {
        return client.get("repos/$owner/$repo").body()
    }

    override suspend fun getReadme(owner: String, repo: String): NetworkReadme {
        return client.get("repos/$owner/$repo/readme").body()
    }

    override suspend fun getTrendingRepositories(
        language: String?,
        since: String
    ): List<NetworkRepository> {
        // Trending API isn't officially supported by GitHub in a clean way, so usually we construct a search query
        // E.g., searching for repositories created in the last week
        val langQuery = if (language.isNullOrBlank()) "" else "language:$language"
        val query = "stars:>1 $langQuery".trim()
        
        val response = client.get("search/repositories") {
            parameter("q", query)
            parameter("sort", "stars")
            parameter("order", "desc")
            parameter("per_page", 30)
        }.body<NetworkSearchResponse>()
        
        return response.items
    }
}
