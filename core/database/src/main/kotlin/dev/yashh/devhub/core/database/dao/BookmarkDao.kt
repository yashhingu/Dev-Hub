package dev.yashh.devhub.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.yashh.devhub.core.database.entity.BookmarkEntity
import dev.yashh.devhub.core.database.entity.BookmarkTagEntity
import dev.yashh.devhub.core.database.entity.BookmarkWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Transaction
    @Query("SELECT * FROM bookmarks ORDER BY addedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE repositoryId = :repositoryId")
    suspend fun deleteBookmarkById(repositoryId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: BookmarkTagEntity)

    @Query("DELETE FROM bookmark_tags WHERE id = :tagId")
    suspend fun removeTag(tagId: Long)

    @Transaction
    @Query("""
        SELECT b.* FROM bookmarks b 
        INNER JOIN repositories r ON b.repositoryId = r.id 
        WHERE r.name LIKE '%' || :query || '%' OR r.description LIKE '%' || :query || '%'
        ORDER BY b.addedAt DESC
    """)
    fun searchBookmarks(query: String): Flow<List<BookmarkWithTags>>
}
