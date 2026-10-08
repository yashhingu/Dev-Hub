package dev.yashh.devhub.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// DevHub Brand Colors
val DevHubBlue = Color(0xFF2196F3)
val DevHubDarkBlue = Color(0xFF1976D2)
val DevHubAccent = Color(0xFFFF4081)

// Light Theme Colors
val md_theme_light_primary = Color(0xFF0061A4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFD1E4FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001D36)
val md_theme_light_secondary = Color(0xFF535F70)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD7E3F7)
val md_theme_light_onSecondaryContainer = Color(0xFF101C2B)
val md_theme_light_tertiary = Color(0xFF6B5778)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFF2DAFF)
val md_theme_light_onTertiaryContainer = Color(0xFF251431)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFDFBFF)
val md_theme_light_onBackground = Color(0xFF1A1C1E)
val md_theme_light_surface = Color(0xFFFDFBFF)
val md_theme_light_onSurface = Color(0xFF1A1C1E)
val md_theme_light_surfaceVariant = Color(0xFFDFE2EB)
val md_theme_light_onSurfaceVariant = Color(0xFF43474E)
val md_theme_light_outline = Color(0xFF73777F)
val md_theme_light_inverseOnSurface = Color(0xFFF1F0F4)
val md_theme_light_inverseSurface = Color(0xFF2F3033)
val md_theme_light_inversePrimary = Color(0xFF9ECAFF)
val md_theme_light_surfaceTint = Color(0xFF0061A4)

// Dark Theme Colors
val md_theme_dark_primary = Color(0xFF9ECAFF)
val md_theme_dark_onPrimary = Color(0xFF003258)
val md_theme_dark_primaryContainer = Color(0xFF00497D)
val md_theme_dark_onPrimaryContainer = Color(0xFFD1E4FF)
val md_theme_dark_secondary = Color(0xFFBBC7DB)
val md_theme_dark_onSecondary = Color(0xFF253140)
val md_theme_dark_secondaryContainer = Color(0xFF3B4858)
val md_theme_dark_onSecondaryContainer = Color(0xFFD7E3F7)
val md_theme_dark_tertiary = Color(0xFFD6BEE4)
val md_theme_dark_onTertiary = Color(0xFF3B2948)
val md_theme_dark_tertiaryContainer = Color(0xFF523F5F)
val md_theme_dark_onTertiaryContainer = Color(0xFFF2DAFF)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1A1C1E)
val md_theme_dark_onBackground = Color(0xFFE2E2E6)
val md_theme_dark_surface = Color(0xFF1A1C1E)
val md_theme_dark_onSurface = Color(0xFFE2E2E6)
val md_theme_dark_surfaceVariant = Color(0xFF43474E)
val md_theme_dark_onSurfaceVariant = Color(0xFFC3C7CF)
val md_theme_dark_outline = Color(0xFF8D9199)
val md_theme_dark_inverseOnSurface = Color(0xFF1A1C1E)
val md_theme_dark_inverseSurface = Color(0xFFE2E2E6)
val md_theme_dark_inversePrimary = Color(0xFF0061A4)
val md_theme_dark_surfaceTint = Color(0xFF9ECAFF)

// Language Colors Map (GitHub style)
val LanguageColors = mapOf(
    "Kotlin" to Color(0xFFA97BFF),
    "Java" to Color(0xFFB07219),
    "Python" to Color(0xFF3572A5),
    "JavaScript" to Color(0xFFF1E05A),
    "TypeScript" to Color(0xFF3178C6),
    "C++" to Color(0xFFF34B7D),
    "C" to Color(0xFF555555),
    "C#" to Color(0xFF178600),
    "Ruby" to Color(0xFF701516),
    "Go" to Color(0xFF00ADD8),
    "Swift" to Color(0xFFFFAC45),
    "Dart" to Color(0xFF00B4AB),
    "Rust" to Color(0xFFDEA584),
    "HTML" to Color(0xFFE34C26),
    "CSS" to Color(0xFF563D7C),
    "PHP" to Color(0xFF4F5D95),
    "Shell" to Color(0xFF89E051),
    "Scala" to Color(0xFFC22D40),
    "Vue" to Color(0xFF41B883),
    "Jupyter Notebook" to Color(0xFFDA5B0B)
)

// Fallback color for unknown languages
val DefaultLanguageColor = Color(0xFF888888)

/**
 * Helper function to safely get a language color.
 */
fun getLanguageColor(language: String?): Color {
    if (language == null) return DefaultLanguageColor
    return LanguageColors[language] ?: DefaultLanguageColor
}
