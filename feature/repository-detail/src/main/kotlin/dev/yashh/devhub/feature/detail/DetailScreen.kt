package dev.yashh.devhub.feature.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.yashh.devhub.core.designsystem.component.ErrorState
import dev.yashh.devhub.core.designsystem.component.LoadingIndicator
import dev.yashh.devhub.feature.detail.component.MarkdownRenderer
import dev.yashh.devhub.feature.detail.component.StatsRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    DetailScreen(
        uiState = uiState,
        onIntent = { intent ->
            if (intent is DetailIntent.Share && uiState is DetailUiState.Success) {
                val url = (uiState as DetailUiState.Success).repository.htmlUrl
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, url)
                }, "Share repository"))
            } else viewModel.handleIntent(intent)
        },
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onIntent: (DetailIntent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (uiState is DetailUiState.Success) {
                        Text(uiState.repository.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is DetailUiState.Success) {
                        IconButton(onClick = { onIntent(DetailIntent.Share) }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                        IconButton(onClick = { onIntent(DetailIntent.ToggleBookmark) }) {
                            val icon = if (uiState.repository.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder
                            Icon(icon, contentDescription = "Bookmark")
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        when (uiState) {
            is DetailUiState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize().padding(paddingValues))
            }
            is DetailUiState.Error -> {
                ErrorState(
                    message = "Failed to load repository details",
                    onRetry = { onIntent(DetailIntent.Retry) },
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                )
            }
            is DetailUiState.Success -> {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.repository.fullName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    uiState.repository.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    StatsRow(repository = uiState.repository)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "README.md",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    MarkdownRenderer(
                        markdown = uiState.readme,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
