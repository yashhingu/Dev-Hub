package dev.yashh.devhub.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.yashh.devhub.core.database.entity.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Query("SELECT * FROM repositories WHERE id = :id")
    fun getRepository(id: Long): Flow<RepositoryEntity?>

    @Query("SELECT * FROM repositories WHERE fullName = :fullName")
    fun getRepositoryByFullName(fullName: String): Flow<RepositoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(repository: RepositoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(repositories: List<RepositoryEntity>)

    @Query("SELECT id FROM repositories WHERE isBookmarked = 1")
    suspend fun getBookmarkedIds(): List<Long>

    @Query("SELECT * FROM repositories WHERE (:language IS NULL OR LOWER(language) = LOWER(:language)) ORDER BY stargazersCount DESC LIMIT 50")
    suspend fun getTrendingRepositories(language: String?): List<RepositoryEntity>

    @Query("SELECT * FROM repositories WHERE (:language IS NULL OR LOWER(language) = LOWER(:language)) ORDER BY stargazersCount DESC LIMIT 50")
    fun observeTrendingRepositories(language: String?): Flow<List<RepositoryEntity>>

    @Query("UPDATE repositories SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Long, isBookmarked: Boolean)

    @Query("DELETE FROM repositories WHERE cacheTimestamp < :threshold AND isBookmarked = 0")
    suspend fun clearOldCache(threshold: Long)
    
    @Transaction
    suspend fun withTransaction(action: suspend () -> Unit) {
        action()
    }
}
