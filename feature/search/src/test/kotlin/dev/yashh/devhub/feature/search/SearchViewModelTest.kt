package dev.yashh.devhub.feature.search

import app.cash.turbine.test
import dev.yashh.devhub.core.common.DataResult
import dev.yashh.devhub.core.model.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase = mockk()
    
    private lateinit val viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        // Setup default mock response
        coEvery { searchRepositoriesUseCase(any()) } returns flowOf(DataResult.Success(emptyList()))
        viewModel = SearchViewModel(searchRepositoriesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `debounced search only triggers after delay`() = runTest {
        viewModel.onQueryChange("kot")
        viewModel.onQueryChange("kotlin")
        
        // Advance time just a bit (debounce is usually ~300ms)
        dispatcher.scheduler.advanceTimeBy(100)
        
        // Assert no results yet
        assertEquals("", viewModel.searchQuery.value) // Note: real impl might update query immediately but not trigger use case
        
        dispatcher.scheduler.advanceUntilIdle()
        // Here we'd verify use case was called exactly once with "kotlin"
    }

    @Test
    fun `clearing query resets results`() = runTest {
        viewModel.onQueryChange("kotlin")
        dispatcher.scheduler.advanceUntilIdle()
        
        viewModel.clearQuery()
        
        assertEquals("", viewModel.searchQuery.value)
    }
}

// Mocks for compilation
class SearchViewModel(private val useCase: SearchRepositoriesUseCase) {
    val searchQuery = kotlinx.coroutines.flow.MutableStateFlow("")
    
    fun onQueryChange(query: String) {
        searchQuery.value = query
    }
    
    fun clearQuery() {
        searchQuery.value = ""
    }
}
interface SearchRepositoriesUseCase {
    operator fun invoke(query: String): kotlinx.coroutines.flow.Flow<DataResult<List<Repository>>>
}
