package dev.yashh.devhub.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.feature.detail.usecase.GetReadmeUseCase
import dev.yashh.devhub.feature.detail.usecase.GetRepositoryDetailUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(
        val repository: Repository,
        val readme: String
    ) : DetailUiState
    data object Error : DetailUiState
}

sealed interface DetailIntent {
    data class LoadRepo(val owner: String, val repoName: String) : DetailIntent
    data object ToggleBookmark : DetailIntent
    data object Share : DetailIntent
    data object OpenInBrowser : DetailIntent
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRepositoryDetailUseCase: GetRepositoryDetailUseCase,
    private val getReadmeUseCase: GetReadmeUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val owner: String? = savedStateHandle["owner"]
    private val repoName: String? = savedStateHandle["repoName"]

    init {
        if (owner != null && repoName != null) {
            handleIntent(DetailIntent.LoadRepo(owner, repoName))
        }
    }

    fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadRepo -> loadRepositoryData(intent.owner, intent.repoName)
            is DetailIntent.ToggleBookmark -> {
                // Implement toggle bookmark logic
            }
            is DetailIntent.Share -> {
                // Implement share logic
            }
            is DetailIntent.OpenInBrowser -> {
                // Implement open in browser logic
            }
        }
    }

    private fun loadRepositoryData(owner: String, repoName: String) {
        viewModelScope.launch(dispatchers.io) {
            _uiState.update { DetailUiState.Loading }
            
            try {
                val repository = getRepositoryDetailUseCase(owner, repoName)
                val readme = getReadmeUseCase(owner, repoName)
                
                _uiState.update {
                    DetailUiState.Success(
                        repository = repository,
                        readme = readme
                    )
                }
            } catch (e: Exception) {
                _uiState.update { DetailUiState.Error }
            }
        }
    }
}
