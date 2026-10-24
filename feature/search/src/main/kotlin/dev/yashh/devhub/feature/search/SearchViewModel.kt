package dev.yashh.devhub.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.database.LocalDataSource
import dev.yashh.devhub.feature.search.usecase.ManageRecentSearchesUseCase
import dev.yashh.devhub.feature.search.usecase.SearchRepositoriesUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

sealed interface SearchUiState {
    data class Idle(val recentSearches: ImmutableList<String>) : SearchUiState
    data object Searching : SearchUiState
    data class Results(val items: ImmutableList<Repository>, val totalCount: Int) : SearchUiState
    data object Empty : SearchUiState
    data object Error : SearchUiState
}

sealed interface SearchIntent {
    data class UpdateQuery(val query: String) : SearchIntent
    data class Search(val query: String) : SearchIntent
    data object ClearQuery : SearchIntent
    data class SelectRecent(val query: String) : SearchIntent
    data class ToggleBookmark(val repository: Repository) : SearchIntent
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val manageRecentSearchesUseCase: ManageRecentSearchesUseCase,
    private val localDataSource: LocalDataSource,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle(persistentListOf()))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null
    private var recentQueries: ImmutableList<String> = persistentListOf()

    init {
        loadRecentSearches()
        observeQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        // Debounce search query to avoid spamming the network
        viewModelScope.launch(dispatchers.io) {
            searchQuery
                .debounce(300)
                .filter { it.length > 2 }
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> {
                searchQuery.value = intent.query
                if (intent.query.length <= 2) {
                    searchJob?.cancel()
                    _uiState.value = SearchUiState.Idle(recentQueries)
                }
            }
            is SearchIntent.Search -> performSearch(intent.query)
            is SearchIntent.ClearQuery -> {
                searchQuery.value = ""
                searchJob?.cancel()
                _uiState.value = SearchUiState.Idle(recentQueries)
            }
            is SearchIntent.SelectRecent -> {
                searchQuery.value = intent.query
                performSearch(intent.query)
            }
            is SearchIntent.ToggleBookmark -> viewModelScope.launch(dispatchers.io) {
                val repository = intent.repository
                if (repository.isBookmarked) localDataSource.removeBookmark(repository.id)
                else localDataSource.addBookmark(repository)
                _uiState.update { state ->
                    if (state is SearchUiState.Results) state.copy(items = state.items.map {
                        if (it.id == repository.id) it.copy(isBookmarked = !repository.isBookmarked) else it
                    }.toImmutableList()) else state
                }
            }
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch(dispatchers.io) {
            manageRecentSearchesUseCase.getRecentSearches().collectLatest { recent ->
                recentQueries = recent
                if (searchQuery.value.length <= 2) _uiState.update { SearchUiState.Idle(recent) }
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatchers.io) {
            _uiState.update { SearchUiState.Searching }
            
            try {
                // Save to recent searches
                manageRecentSearchesUseCase.addRecentSearch(query)
                
                val result = searchRepositoriesUseCase(query)
                if (result.isEmpty()) {
                    _uiState.update { SearchUiState.Empty }
                } else {
                    _uiState.update { SearchUiState.Results(result, result.size) }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { SearchUiState.Error }
            }
        }
    }
}
