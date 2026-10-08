package dev.yashh.devhub.feature.bookmarks

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object BookmarksRoutePath

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    navigate(BookmarksRoutePath, navOptions)
}

fun NavGraphBuilder.bookmarksScreen(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit
) {
    composable<BookmarksRoutePath> {
        BookmarksRoute(onNavigateToDetail = onNavigateToDetail)
    }
}
