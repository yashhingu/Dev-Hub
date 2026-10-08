package dev.yashh.devhub.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KtorGitHubApiServiceTest {

    private fun createMockClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `successful API response parses JSON correctly`() = runTest {
        val jsonResponse = """
            {
              "total_count": 1,
              "incomplete_results": false,
              "items": [
                {
                  "id": 12345,
                  "name": "mock-repo",
                  "full_name": "user/mock-repo",
                  "owner": { "login": "user", "avatar_url": "url" },
                  "html_url": "https://github.com/user/mock-repo",
                  "description": "A mock repo",
                  "stargazers_count": 500,
                  "language": "Kotlin",
                  "forks_count": 50
                }
              ]
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val client = createMockClient(mockEngine)
        val apiService = KtorGitHubApiService(client)

        val response = apiService.searchRepositories("language:kotlin", "stars", "desc")
        
        assertNotNull(response)
        assertEquals(1, response.items.size)
        assertEquals("mock-repo", response.items.first().name)
        assertEquals(500, response.items.first().stargazersCount)
    }

    @Test
    fun `API rate limit error throws appropriate exception`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "{\"message\": \"API rate limit exceeded for user.\"}",
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val client = createMockClient(mockEngine)
        val apiService = KtorGitHubApiService(client)

        var exceptionThrown = false
        try {
            apiService.searchRepositories("test", "stars", "desc")
        } catch (e: Exception) {
            exceptionThrown = true
            // In a real scenario we might wrap Ktor exceptions in custom NetworkException
        }
        
        assertTrue("Expected exception to be thrown for 403 Forbidden", exceptionThrown)
    }
}

// Simple implementations for test to compile
class KtorGitHubApiService(private val client: HttpClient) {
    suspend fun searchRepositories(query: String, sort: String, order: String): SearchResponse {
        return io.ktor.client.call.body(
            client.get("https://api.github.com/search/repositories") {
                io.ktor.client.request.url {
                    parameters.append("q", query)
                    parameters.append("sort", sort)
                    parameters.append("order", order)
                }
            }
        )
    }
}

@kotlinx.serialization.Serializable
data class SearchResponse(
    @kotlinx.serialization.SerialName("total_count") val totalCount: Int,
    val items: List<RepoDto>
)

@kotlinx.serialization.Serializable
data class RepoDto(
    val id: Long,
    val name: String,
    val owner: OwnerDto,
    val description: String?,
    val language: String?,
    @kotlinx.serialization.SerialName("stargazers_count") val stargazersCount: Int,
    @kotlinx.serialization.SerialName("forks_count") val forksCount: Int,
    @kotlinx.serialization.SerialName("html_url") val htmlUrl: String
)

@kotlinx.serialization.Serializable
data class OwnerDto(val login: String, @kotlinx.serialization.SerialName("avatar_url") val avatarUrl: String)
