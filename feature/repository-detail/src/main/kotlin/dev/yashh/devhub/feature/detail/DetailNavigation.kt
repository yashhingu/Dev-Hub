package dev.yashh.devhub.feature.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoutePath(
    val owner: String,
    val repoName: String
)

fun NavController.navigateToDetail(owner: String, repoName: String, navOptions: NavOptions? = null) {
    navigate(DetailRoutePath(owner = owner, repoName = repoName), navOptions)
}

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit
) {
    composable<DetailRoutePath> {
        DetailRoute(onNavigateBack = onNavigateBack)
    }
}
