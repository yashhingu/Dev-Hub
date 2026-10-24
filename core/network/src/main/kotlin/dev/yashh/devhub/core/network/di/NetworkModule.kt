package dev.yashh.devhub.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.yashh.devhub.core.network.GitHubApiService
import dev.yashh.devhub.core.network.KtorGitHubApiService
import dev.yashh.devhub.core.network.KtorNetworkDataSource
import dev.yashh.devhub.core.network.NetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = LogLevel.INFO
            }
            defaultRequest {
                url("https://api.github.com/")
                header(HttpHeaders.Accept, "application/vnd.github.v3+json")
                // Optional: add authorization header if a token is available
            }
        }
    }

    @Provides
    @Singleton
    fun provideGitHubApiService(client: HttpClient): GitHubApiService {
        return KtorGitHubApiService(client)
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(apiService: GitHubApiService): NetworkDataSource {
        return KtorNetworkDataSource(apiService)
    }
}
