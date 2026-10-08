package dev.yashh.devhub.feature.search.usecase

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ManageRecentSearchesUseCase @Inject constructor() {
    
    fun getRecentSearches(): Flow<ImmutableList<String>> = flow {
        // DataStore logic mock
        emit(persistentListOf("kotlin", "compose", "architecture"))
    }
    
    suspend fun addRecentSearch(query: String) {
        // Store in datastore
    }
}
