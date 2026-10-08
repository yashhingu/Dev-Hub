package dev.yashh.devhub.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dev.yashh.devhub.core.designsystem.icon.DevHubIcons
import dev.yashh.devhub.core.designsystem.theme.DevHubTheme

/**
 * A standard center-aligned TopAppBar for the DevHub app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevHubTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DevHubTopAppBarPreview() {
    DevHubTheme {
        Surface {
            DevHubTopAppBar(
                title = "DevHub",
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = DevHubIcons.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = DevHubIcons.BookmarkOutline,
                            contentDescription = "Bookmarks"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = DevHubIcons.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
            )
        }
    }
}
