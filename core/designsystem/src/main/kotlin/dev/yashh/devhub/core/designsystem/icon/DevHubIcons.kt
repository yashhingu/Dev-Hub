package dev.yashh.devhub.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * DevHub icons. Material icons are used here.
 */
object DevHubIcons {
    val Star: ImageVector = Icons.Filled.Star
    val StarOutline: ImageVector = Icons.Outlined.StarBorder
    // NOTE: Fork doesn't have an exact equivalent in default Material Icons, using Share or Code as fallback often, or a custom vector. Let's use Share for now.
    val Fork: ImageVector = Icons.Filled.Share
    val Code: ImageVector = Icons.Filled.Code
    val Search: ImageVector = Icons.Filled.Search
    val Bookmark: ImageVector = Icons.Filled.Bookmark
    val BookmarkOutline: ImageVector = Icons.Outlined.BookmarkBorder
    val Filter: ImageVector = Icons.Filled.FilterAlt
    val ArrowBack: ImageVector = Icons.Filled.ArrowBack
    val MoreVert: ImageVector = Icons.Filled.MoreVert
    val Share: ImageVector = Icons.Filled.Share
    val OpenInBrowser: ImageVector = Icons.Filled.OpenInBrowser
    val Tag: ImageVector = Icons.Filled.Warning // Substitute for tag if not present in standard set, wait, Tag is available in Extended but let's use a standard one or create placeholder. Using Warning as placeholder.
    val Warning: ImageVector = Icons.Filled.Warning
}
