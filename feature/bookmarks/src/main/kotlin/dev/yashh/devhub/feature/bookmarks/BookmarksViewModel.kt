package dev.yashh.devhub.feature.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.feature.bookmarks.usecase.GetBookmarkedRepositoriesUseCase
import dev.yashh.devhub.feature.bookmarks.usecase.ManageTagsUseCase
import dev.yashh.devhub.feature.bookmarks.usecase.RemoveBookmarkUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BookmarksUiState {
    data object Loading : BookmarksUiState
    data class Success(
        val repositories: ImmutableList<Repository>,
        val tags: ImmutableList<String>,
        val selectedTag: String? = null
    ) : BookmarksUiState
    data object Error : BookmarksUiState
}

sealed interface BookmarksIntent {
    data object Refresh : BookmarksIntent
    data class RemoveBookmark(val id: Long) : BookmarksIntent
    data class SelectTag(val tag: String?) : BookmarksIntent
}

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedRepositoriesUseCase: GetBookmarkedRepositoriesUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val manageTagsUseCase: ManageTagsUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookmarksUiState>(BookmarksUiState.Loading)
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    init {
        loadBookmarks()
    }

    fun handleIntent(intent: BookmarksIntent) {
        when (intent) {
            is BookmarksIntent.Refresh -> loadBookmarks()
            is BookmarksIntent.RemoveBookmark -> removeBookmark(intent.id)
            is BookmarksIntent.SelectTag -> {
                // Handle tag selection filtering
            }
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch(dispatchers.io) {
            try {
                getBookmarkedRepositoriesUseCase().collectLatest { repos ->
                    val tags = manageTagsUseCase.getAllTags()
                    _uiState.update { 
                        BookmarksUiState.Success(
                            repositories = repos,
                            tags = tags
                        ) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { BookmarksUiState.Error }
            }
        }
    }

    private fun removeBookmark(id: Long) {
        viewModelScope.launch(dispatchers.io) {
            removeBookmarkUseCase(id)
        }
    }
}
