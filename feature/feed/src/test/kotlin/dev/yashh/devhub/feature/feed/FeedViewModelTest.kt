package dev.yashh.devhub.feature.feed

import app.cash.turbine.test
import dev.yashh.devhub.core.common.DataResult
import dev.yashh.devhub.core.model.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase = mockk()
    
    private lateinit val viewModel: FeedViewModel

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
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and successful fetch transitions to Success state`() = runTest {
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(dummyRepos))
        
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        
        viewModel.uiState.test {
            // First emission from MutableStateFlow initial value
            assertTrue(awaitItem() is FeedUiState.Loading)
            
            // Second emission after usecase returns
            val successState = awaitItem()
            assertTrue(successState is FeedUiState.Success)
            assertEquals(dummyRepos, (successState as FeedUiState.Success).repositories)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `network error transitions to Error state`() = runTest {
        val errorMessage = "Network timeout"
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Error(Exception(errorMessage)))
        
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        
        viewModel.uiState.test {
            assertTrue(awaitItem() is FeedUiState.Loading)
            
            val errorState = awaitItem()
            assertTrue(errorState is FeedUiState.Error)
            assertEquals(errorMessage, (errorState as FeedUiState.Error).message)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh triggers reload and shows refreshing state`() = runTest {
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(dummyRepos))
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        
        // Wait for initial load
        dispatcher.scheduler.advanceUntilIdle()
        
        viewModel.isRefreshing.test {
            assertEquals(false, awaitItem()) // Initial state
            
            viewModel.refresh()
            assertEquals(true, awaitItem()) // Starts refreshing
            
            // Since it's standard dispatcher, we need to advance time
            dispatcher.scheduler.advanceUntilIdle()
            
            assertEquals(false, awaitItem()) // Finishes refreshing
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filter language updates filtered results`() = runTest {
        val repos = dummyRepos + dummyRepos.first().copy(id = 2, language = "Java")
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(repos))
        
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        dispatcher.scheduler.advanceUntilIdle()
        
        viewModel.uiState.test {
            val initialState = awaitItem() as FeedUiState.Success
            assertEquals(2, initialState.repositories.size)
            
            viewModel.updateLanguageFilter("Kotlin")
            val filteredState = awaitItem() as FeedUiState.Success
            assertEquals(1, filteredState.repositories.size)
            assertEquals("Kotlin", filteredState.repositories.first().language)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggle bookmark updates item bookmark state`() = runTest {
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(dummyRepos))
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        dispatcher.scheduler.advanceUntilIdle()
        
        viewModel.uiState.test {
            val state = awaitItem() as FeedUiState.Success
            assertEquals(false, state.repositories.first().isBookmarked)
            
            // NOTE: In a real app this would call a use case to update the DB, 
            // and the DB flow would emit the new list.
            // Assuming ViewModel updates UI optimistically for the test
            viewModel.toggleBookmark(dummyRepos.first())
            
            val updatedState = awaitItem() as FeedUiState.Success
            assertEquals(true, updatedState.repositories.first().isBookmarked)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `empty response shows empty state`() = runTest {
        coEvery { getTrendingRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(emptyList()))
        
        viewModel = FeedViewModel(getTrendingRepositoriesUseCase)
        
        viewModel.uiState.test {
            assertTrue(awaitItem() is FeedUiState.Loading)
            
            val successState = awaitItem()
            assertTrue(successState is FeedUiState.Success)
            assertTrue((successState as FeedUiState.Success).repositories.isEmpty())
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
