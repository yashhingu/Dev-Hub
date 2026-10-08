package dev.yashh.devhub.feature.feed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object FeedRoutePath

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    navigate(FeedRoutePath, navOptions)
}

fun NavGraphBuilder.feedScreen(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit
) {
    composable<FeedRoutePath> {
        FeedRoute(onNavigateToDetail = onNavigateToDetail)
    }
}
