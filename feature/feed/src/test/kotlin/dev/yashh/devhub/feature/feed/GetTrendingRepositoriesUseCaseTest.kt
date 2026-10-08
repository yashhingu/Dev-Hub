package dev.yashh.devhub.feature.feed

import app.cash.turbine.test
import dev.yashh.devhub.core.common.DataResult
import dev.yashh.devhub.core.model.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetTrendingRepositoriesUseCaseTest {

    private val repository: FeedRepository = mockk()
    private lateinit var useCase: GetTrendingRepositoriesUseCase

    private val dummyRepos = listOf(
        Repository(
            id = 1,
            name = "TestRepo",
            owner = "TestUser",
            description = "A test repository",
            language = "Kotlin",
            stars = 100,
            forks = 10,
            url = "https://github.com/TestUser/TestRepo",
            isBookmarked = false
        )
    )

    @Before
    fun setup() {
        useCase = GetTrendingRepositoriesUseCase(repository)
    }

    @Test
    fun `invoke returns successful data flow`() = runTest {
        coEvery { repository.getTrendingRepositories(any()) } returns flowOf(DataResult.Success(dummyRepos))

        useCase().test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(dummyRepos, (result as DataResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke handles network error with cache fallback`() = runTest {
        // Simulating the repository returning cached data despite error
        val exception = Exception("Network Error")
        coEvery { repository.getTrendingRepositories(any()) } returns flowOf(
            DataResult.Success(dummyRepos) // Repo returns cached data on network error
        )

        useCase().test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(dummyRepos, (result as DataResult.Success).data)
            awaitComplete()
        }
    }
    
    @Test
    fun `invoke with language filtering passes language to repository`() = runTest {
        val language = "Python"
        coEvery { repository.getTrendingRepositories(language) } returns flowOf(DataResult.Success(emptyList()))

        useCase(language).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertTrue((result as DataResult.Success).data.isEmpty())
            awaitComplete()
        }
    }
}
