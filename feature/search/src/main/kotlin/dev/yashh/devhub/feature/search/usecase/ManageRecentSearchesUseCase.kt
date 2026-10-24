package dev.yashh.devhub.feature.search.usecase

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.searchHistoryStore by preferencesDataStore(name = "search_history")

class ManageRecentSearchesUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val recentKey = stringPreferencesKey("recent_queries")

    fun getRecentSearches(): Flow<ImmutableList<String>> = context.searchHistoryStore.data.map { preferences ->
        preferences[recentKey].orEmpty().split('\n').filter(String::isNotBlank).toImmutableList()
    }

    suspend fun addRecentSearch(query: String) {
        val normalized = query.trim().replace('\n', ' ')
        if (normalized.isEmpty()) return
        context.searchHistoryStore.edit { preferences ->
            val previous = preferences[recentKey].orEmpty().split('\n').filter(String::isNotBlank)
            preferences[recentKey] = (listOf(normalized) + previous.filterNot {
                it.equals(normalized, ignoreCase = true)
            }).take(10).joinToString("\n")
        }
    }
}
