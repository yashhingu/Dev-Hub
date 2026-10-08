package dev.yashh.devhub.feature.feed

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.yashh.devhub.core.common.UiText
import dev.yashh.devhub.core.designsystem.component.EmptyState
import dev.yashh.devhub.core.designsystem.component.ErrorState
import dev.yashh.devhub.core.designsystem.component.RepositoryCard
import dev.yashh.devhub.core.designsystem.component.ShimmerRepositoryCard
import dev.yashh.devhub.core.model.Repository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedRoute(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeedScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    uiState: FeedUiState,
    onIntent: (FeedIntent) -> Unit,
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = listOf("All", "Kotlin", "Java", "Python", "JavaScript", "TypeScript", "Rust", "Go", "Swift")
    
    // NOTE: PullToRefreshBox handles the pull-to-refresh logic elegantly in M3.
    Column(modifier = modifier.fillMaxSize()) {
        // Filter Chips row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(languages) { language ->
                val isSelected = when(uiState) {
                    is FeedUiState.Success -> uiState.selectedLanguage == language || (uiState.selectedLanguage == null && language == "All")
                    is FeedUiState.Loading -> language == "All" // default to All during loading
                    else -> language == "All"
                }
                
                FilterChip(
                    selected = isSelected,
                    onClick = { 
                        onIntent(FeedIntent.FilterLanguage(if (language == "All") null else language)) 
                    },
                    label = { Text(language) }
                )
            }
        }

        Crossfade(targetState = uiState, label = "FeedState") { state ->
            when (state) {
                is FeedUiState.Loading -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(6) {
                            ShimmerRepositoryCard()
                        }
                    }
                }
                is FeedUiState.Error -> {
                    ErrorState(
                        message = state.message.asString(), // Using UiText resolution pattern
                        onRetry = { onIntent(FeedIntent.Refresh) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is FeedUiState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { onIntent(FeedIntent.Refresh) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (state.repositories.isEmpty()) {
                            EmptyState(
                                title = "No repositories found",
                                message = "Try selecting a different language.",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = state.repositories,
                                    key = { it.id }
                                ) { repo ->
                                    RepositoryCard(
                                        repository = repo,
                                        onClick = { onNavigateToDetail(repo.owner.login, repo.name) },
                                        onBookmarkClick = { onIntent(FeedIntent.ToggleBookmark(repo.id)) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                                
                                // Could add load more indicator here for pagination
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun FeedScreenPreview() {
    // Preview mock data
}
