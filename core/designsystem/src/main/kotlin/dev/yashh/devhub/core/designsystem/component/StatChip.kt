package dev.yashh.devhub.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yashh.devhub.core.designsystem.icon.DevHubIcons
import dev.yashh.devhub.core.designsystem.theme.DevHubTheme

/**
 * A chip to display a statistic like stars, forks, or issues with an icon.
 */
@Composable
fun StatChip(
    icon: ImageVector,
    count: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = count,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun StatChipPreview() {
    DevHubTheme {
        Surface {
            Row(modifier = Modifier.padding(16.dp)) {
                StatChip(
                    icon = DevHubIcons.Star,
                    count = "1.2k",
                    contentDescription = "Stars"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatChip(
                    icon = DevHubIcons.Fork,
                    count = "340",
                    contentDescription = "Forks"
                )
            }
        }
    }
}
