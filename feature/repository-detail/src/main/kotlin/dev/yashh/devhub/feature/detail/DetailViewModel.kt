package dev.yashh.devhub.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yashh.devhub.core.common.dispatcher.DispatcherProvider
import dev.yashh.devhub.core.model.Repository
import dev.yashh.devhub.core.database.LocalDataSource
import dev.yashh.devhub.feature.detail.usecase.GetReadmeUseCase
import dev.yashh.devhub.feature.detail.usecase.GetRepositoryDetailUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
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
    data object Retry : DetailIntent
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRepositoryDetailUseCase: GetRepositoryDetailUseCase,
    private val getReadmeUseCase: GetReadmeUseCase,
    private val localDataSource: LocalDataSource,
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
                val state = _uiState.value as? DetailUiState.Success ?: return
                viewModelScope.launch(dispatchers.io) {
                    if (state.repository.isBookmarked) {
                        localDataSource.removeBookmark(state.repository.id)
                    } else {
                        localDataSource.addBookmark(state.repository)
                    }
                    _uiState.update { current ->
                        if (current is DetailUiState.Success) current.copy(
                            repository = current.repository.copy(isBookmarked = !state.repository.isBookmarked)
                        ) else current
                    }
                }
            }
            is DetailIntent.Share -> Unit
            is DetailIntent.Retry -> if (owner != null && repoName != null) loadRepositoryData(owner, repoName)
        }
    }

    private fun loadRepositoryData(owner: String, repoName: String) {
        viewModelScope.launch(dispatchers.io) {
            _uiState.update { DetailUiState.Loading }
            
            try {
                val remoteRepository = getRepositoryDetailUseCase(owner, repoName)
                val cached = localDataSource.getRepository(remoteRepository.id)
                val repository = remoteRepository.copy(isBookmarked = cached?.isBookmarked == true)
                val readme = getReadmeUseCase(owner, repoName)
                
                _uiState.update {
                    DetailUiState.Success(
                        repository = repository,
                        readme = readme
                    )
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update { DetailUiState.Error }
            }
        }
    }
}
