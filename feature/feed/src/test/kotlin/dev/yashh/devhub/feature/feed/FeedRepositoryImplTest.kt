package dev.yashh.devhub.feature.feed

import dev.yashh.devhub.core.common.result.DataError
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.database.LocalDataSource
import dev.yashh.devhub.core.model.Owner
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.network.NetworkDataSource
import dev.yashh.devhub.feature.feed.repository.FeedRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedRepositoryImplTest {
    private val remote: NetworkDataSource = mockk()
    private val local: LocalDataSource = mockk()
    private val repository = FeedRepositoryImpl(remote, local)

    private fun repo(id: Long) = Repository(
        id = id,
        name = "repo$id",
        fullName = "owner/repo$id",
        description = null,
        owner = Owner(1, "owner", "", ""),
        stargazersCount = 10,
        forksCount = 1,
        language = "Kotlin",
        languageColor = null,
        openIssuesCount = 0,
        topics = persistentListOf(),
        htmlUrl = "https://github.com/owner/repo$id",
        createdAt = "2026-01-01T00:00:00Z",
        updatedAt = "2026-01-01T00:00:00Z"
    )

    @Test fun cachedDataIsEmittedBeforeRefreshedData() = runTest {
        val cached = repo(1)
        val fresh = repo(2)
        coEvery { local.getTrendingRepositories(null) } returns listOf(cached) andThen listOf(fresh)
        coEvery { remote.fetchTrendingRepositories(null) } returns listOf(fresh)
        coEvery { local.saveTrendingRepositories(listOf(fresh)) } returns Unit

        val emissions = repository.getTrendingRepositories(null).toList()
        assertEquals(listOf(cached), (emissions[0] as DataResult.Success).data.toList())
        assertEquals(listOf(fresh), (emissions[1] as DataResult.Success).data.toList())
        coVerify(exactly = 1) { local.saveTrendingRepositories(listOf(fresh)) }
    }

    @Test fun cachedDataRemainsVisibleWhenNetworkFails() = runTest {
        val cached = repo(1)
        coEvery { local.getTrendingRepositories(null) } returns listOf(cached)
        coEvery { remote.fetchTrendingRepositories(null) } throws IllegalStateException("offline")

        val emissions = repository.getTrendingRepositories(null).toList()
        assertEquals(1, emissions.size)
        assertEquals(listOf(cached), (emissions.single() as DataResult.Success).data.toList())
    }

    @Test fun emptyCacheReportsNetworkFailure() = runTest {
        coEvery { local.getTrendingRepositories(null) } returns emptyList()
        coEvery { remote.fetchTrendingRepositories(null) } throws IllegalStateException("offline")

        val result = repository.getTrendingRepositories(null).toList().single()
        assertTrue(result is DataResult.Failure && result.error == DataError.NetworkError.NoInternet)
    }
}
