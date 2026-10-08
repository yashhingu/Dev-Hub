package dev.yashh.devhub.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yashh.devhub.core.common.UiText
import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.common.result.DataResult
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.feature.feed.usecase.GetTrendingRepositoriesUseCase
import dev.yashh.devhub.feature.feed.usecase.ToggleBookmarkUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FeedUiState {
    data object Loading : FeedUiState
    
    data class Success(
        val repositories: ImmutableList<Repository>,
        val selectedLanguage: String?,
        val isRefreshing: Boolean
    ) : FeedUiState
    
    data class Error(val message: UiText) : FeedUiState
}

sealed interface FeedIntent {
    data object Refresh : FeedIntent
    data class ToggleBookmark(val repositoryId: Long) : FeedIntent
    data class FilterLanguage(val language: String?) : FeedIntent
    data object LoadMore : FeedIntent
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getTrendingRepositoriesUseCase: GetTrendingRepositoriesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private var currentLanguage: String? = null

    init {
        loadRepositories()
    }

    fun handleIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.Refresh -> refresh()
            is FeedIntent.ToggleBookmark -> toggleBookmark(intent.repositoryId)
            is FeedIntent.FilterLanguage -> {
                currentLanguage = intent.language
                loadRepositories()
            }
            is FeedIntent.LoadMore -> {
                // Implement pagination logic here if needed
            }
        }
    }

    private fun loadRepositories(isRefresh: Boolean = false) {
        if (!isRefresh) {
            _uiState.update { FeedUiState.Loading }
        } else {
            _uiState.update { state ->
                if (state is FeedUiState.Success) state.copy(isRefreshing = true) else FeedUiState.Loading
            }
        }

        viewModelScope.launch(dispatchers.io) {
            getTrendingRepositoriesUseCase(currentLanguage).collectLatest { result ->
                when (result) {
                    is DataResult.Success -> {
                        _uiState.update {
                            FeedUiState.Success(
                                repositories = result.data,
                                selectedLanguage = currentLanguage,
                                isRefreshing = false
                            )
                        }
                    }
                    is DataResult.Failure -> {
                        _uiState.update {
                            FeedUiState.Error(UiText.DynamicString("Failed to load repositories"))
                        }
                    }
                }
            }
        }
    }

    private fun refresh() {
        loadRepositories(isRefresh = true)
    }

    private fun toggleBookmark(repositoryId: Long) {
        viewModelScope.launch(dispatchers.io) {
            // Optimistic update could go here
            toggleBookmarkUseCase(repositoryId)
        }
    }
}
