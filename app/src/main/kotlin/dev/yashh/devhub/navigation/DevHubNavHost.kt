package dev.yashh.devhub.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.yashh.devhub.feature.bookmarks.bookmarksScreen
import dev.yashh.devhub.feature.detail.detailScreen
import dev.yashh.devhub.feature.detail.navigateToDetail
import dev.yashh.devhub.feature.feed.FeedRoutePath
import dev.yashh.devhub.feature.feed.feedScreen
import dev.yashh.devhub.feature.search.searchScreen

/**
 * Top-level navigation graph for DevHub app.
 * Connects feature graphs to the main app scaffold.
 */
@Composable
fun DevHubNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FeedRoutePath,
        modifier = modifier
    ) {
        // Feature: Feed
        feedScreen(
            onNavigateToDetail = { owner, repoName ->
                navController.navigateToDetail(owner, repoName)
            }
        )

        // Feature: Search
        searchScreen(
            onNavigateToDetail = { owner, repoName ->
                navController.navigateToDetail(owner, repoName)
            }
        )

        // Feature: Bookmarks
        bookmarksScreen(
            onNavigateToDetail = { owner, repoName ->
                navController.navigateToDetail(owner, repoName)
            }
        )

        // Feature: Repository Detail
        detailScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

    }
}
