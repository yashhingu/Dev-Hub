package dev.yashh.devhub.feature.detail.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun MarkdownRenderer(
    markdown: String,
    modifier: Modifier = Modifier
) {
    // NOTE: This is a simplified markdown renderer. 
    // In a production app, consider using a full-fledged markdown rendering library like 'compose-markdown'.
    // We hand-craft a basic bold/italic parser here for demonstration purposes.

    val annotatedString = buildAnnotatedString {
        // Very rudimentary markdown parsing logic
        var currentIndex = 0
        while (currentIndex < markdown.length) {
            when {
                markdown.startsWith("**", currentIndex) -> {
                    val endIndex = markdown.indexOf("**", currentIndex + 2)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(markdown.substring(currentIndex + 2, endIndex))
                        }
                        currentIndex = endIndex + 2
                    } else {
                        append(markdown[currentIndex])
                        currentIndex++
                    }
                }
                markdown.startsWith("*", currentIndex) -> {
                    val endIndex = markdown.indexOf("*", currentIndex + 1)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(markdown.substring(currentIndex + 1, endIndex))
                        }
                        currentIndex = endIndex + 1
                    } else {
                        append(markdown[currentIndex])
                        currentIndex++
                    }
                }
                markdown.startsWith("`", currentIndex) -> {
                    val endIndex = markdown.indexOf("`", currentIndex + 1)
                    if (endIndex != -1) {
                        withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = MaterialTheme.colorScheme.surfaceVariant)) {
                            append(markdown.substring(currentIndex + 1, endIndex))
                        }
                        currentIndex = endIndex + 1
                    } else {
                        append(markdown[currentIndex])
                        currentIndex++
                    }
                }
                else -> {
                    append(markdown[currentIndex])
                    currentIndex++
                }
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium
    )
}
