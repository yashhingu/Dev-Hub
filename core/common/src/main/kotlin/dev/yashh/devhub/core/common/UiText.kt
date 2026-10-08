package dev.yashh.devhub.core.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Represents string resources that can be rendered in the UI, either dynamically or from Android resources.
 * This is incredibly useful for passing string-related data from ViewModels without exposing Context.
 */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val id: Int,
        val args: List<Any> = emptyList()
    ) : UiText()

    /**
     * Resolves the string using standard Android Context. Useful in normal views or when outside Compose.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args.toTypedArray())
        }
    }

    /**
     * Resolves the string in a Composable context.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id, *args.toTypedArray())
        }
    }
}
