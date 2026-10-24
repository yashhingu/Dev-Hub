package dev.yashh.devhub.feature.bookmarks

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.yashh.devhub.core.designsystem.component.EmptyState
import dev.yashh.devhub.core.designsystem.component.ErrorState
import dev.yashh.devhub.core.designsystem.component.LoadingIndicator
import dev.yashh.devhub.core.designsystem.component.RepositoryCard
import dev.yashh.devhub.core.designsystem.icon.DevHubIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksRoute(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarksScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    uiState: BookmarksUiState,
    onIntent: (BookmarksIntent) -> Unit,
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        
        Crossfade(targetState = uiState, label = "BookmarksState") { state ->
            when (state) {
                is BookmarksUiState.Loading -> LoadingIndicator(modifier = Modifier.fillMaxSize())
                is BookmarksUiState.Error -> ErrorState(
                    message = "Failed to load bookmarks",
                    onRetry = { onIntent(BookmarksIntent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                )
                is BookmarksUiState.Success -> {
                    if (state.repositories.isEmpty()) {
                        EmptyState(
                            icon = DevHubIcons.BookmarkOutline,
                            title = "No Bookmarks",
                            subtitle = "You haven't bookmarked any repositories yet.",
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
                                    onBookmarkClick = { onIntent(BookmarksIntent.RemoveBookmark(repo.id)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
