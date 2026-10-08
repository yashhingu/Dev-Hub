package dev.yashh.devhub.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.yashh.devhub.core.designsystem.icon.DevHubIcons
import dev.yashh.devhub.core.designsystem.theme.DevHubTheme

/**
 * A card displaying repository information.
 */
@Composable
fun RepositoryCard(
    ownerAvatarUrl: String,
    ownerName: String,
    repoName: String,
    description: String?,
    starsCount: String,
    forksCount: String,
    language: String?,
    isBookmarked: Boolean,
    onCardClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ownerAvatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Repository owner avatar", // use stringResource in real app
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ownerName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = repoName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector = if (isBookmarked) DevHubIcons.Bookmark else DevHubIcons.BookmarkOutline,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark repository",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatChip(
                    icon = DevHubIcons.Star,
                    count = starsCount,
                    contentDescription = "Stars count"
                )
                StatChip(
                    icon = DevHubIcons.Fork,
                    count = forksCount,
                    contentDescription = "Forks count"
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                if (language != null) {
                    LanguageBadge(language = language)
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RepositoryCardPreview() {
    DevHubTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                RepositoryCard(
                    ownerAvatarUrl = "",
                    ownerName = "google",
                    repoName = "accompanist",
                    description = "A collection of extension libraries for Jetpack Compose",
                    starsCount = "6.5k",
                    forksCount = "520",
                    language = "Kotlin",
                    isBookmarked = true,
                    onCardClick = {},
                    onBookmarkClick = {}
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                RepositoryCard(
                    ownerAvatarUrl = "",
                    ownerName = "JetBrains",
                    repoName = "kotlin",
                    description = "The Kotlin Programming Language.",
                    starsCount = "45k",
                    forksCount = "5.2k",
                    language = "Kotlin",
                    isBookmarked = false,
                    onCardClick = {},
                    onBookmarkClick = {}
                )
            }
        }
    }
}
