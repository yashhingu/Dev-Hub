package dev.yashh.devhub.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import dev.yashh.devhub.R
import dev.yashh.devhub.feature.bookmarks.BookmarksRoutePath
import dev.yashh.devhub.feature.feed.FeedRoutePath
import dev.yashh.devhub.feature.search.SearchRoutePath

/**
 * Defines the top-level navigation destinations in the bottom navigation bar.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleTextId: Int,
    val route: Any
) {
    FEED(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleTextId = R.string.nav_feed,
        route = FeedRoutePath
    ),
    SEARCH(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        titleTextId = R.string.nav_search,
        route = SearchRoutePath
    ),
    BOOKMARKS(
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        titleTextId = R.string.nav_bookmarks,
        route = BookmarksRoutePath
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        titleTextId = R.string.nav_profile,
        route = "profile"
    )
}
