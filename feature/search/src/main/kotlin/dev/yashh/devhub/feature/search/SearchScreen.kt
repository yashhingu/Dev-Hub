package dev.yashh.devhub.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.yashh.devhub.core.designsystem.component.EmptyState
import dev.yashh.devhub.core.designsystem.component.ErrorState
import dev.yashh.devhub.core.designsystem.component.LoadingIndicator
import dev.yashh.devhub.core.designsystem.component.RepositoryCard

@Composable
fun SearchRoute(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        onIntent = viewModel::handleIntent,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onIntent: (SearchIntent) -> Unit,
    onNavigateToDetail: (owner: String, repoName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            query = query,
            onQueryChange = { 
                query = it
                onIntent(SearchIntent.UpdateQuery(it))
            },
            onSearch = { onIntent(SearchIntent.Search(query)) },
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search repositories...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { 
                        query = ""
                        onIntent(SearchIntent.ClearQuery)
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {}

        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is SearchUiState.Idle -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            Text(
                                "Recent Searches", 
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(uiState.recentSearches) { recent ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        query = recent
                                        onIntent(SearchIntent.SelectRecent(recent))
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(recent)
                            }
                        }
                    }
                }
                is SearchUiState.Searching -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }
                is SearchUiState.Empty -> {
                    EmptyState(
                        title = "No results found",
                        message = "We couldn't find anything matching '$query'.",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is SearchUiState.Error -> {
                    ErrorState(
                        message = "An error occurred while searching",
                        onRetry = { onIntent(SearchIntent.Search(query)) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is SearchUiState.Results -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text("Found ${uiState.totalCount} results", style = MaterialTheme.typography.labelLarge)
                        }
                        items(uiState.items, key = { it.id }) { repo ->
                            RepositoryCard(
                                repository = repo,
                                onClick = { onNavigateToDetail(repo.owner.login, repo.name) },
                                onBookmarkClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}
