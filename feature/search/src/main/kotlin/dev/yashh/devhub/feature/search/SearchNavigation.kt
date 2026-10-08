package dev.yashh.devhub.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoutePath

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    navigate(SearchRoutePath, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onNavigateToDetail: (owner: String, repoName: String) -> Unit
) {
    composable<SearchRoutePath> {
        SearchRoute(onNavigateToDetail = onNavigateToDetail)
    }
}
