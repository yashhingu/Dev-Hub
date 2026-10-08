package dev.yashh.devhub.core.database

import dev.yashh.devhub.core.database.dao.BookmarkDao
import dev.yashh.devhub.core.database.dao.RepositoryDao
import dev.yashh.devhub.core.database.entity.BookmarkEntity
import dev.yashh.devhub.core.database.entity.BookmarkTagEntity
import dev.yashh.devhub.core.database.mapper.toDomain
import dev.yashh.devhub.core.database.mapper.toDomainRepository
import dev.yashh.devhub.core.database.mapper.toEntity
import dev.yashh.devhub.core.model.BookmarkTag
import dev.yashh.devhub.core.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface LocalDataSource {
    suspend fun getTrendingRepositories(language: String?): List<Repository>
    fun observeTrendingRepositories(language: String?): Flow<List<Repository>>
    suspend fun saveTrendingRepositories(repositories: List<Repository>)
    suspend fun getRepository(id: Long): Repository?
    fun observeRepository(id: Long): Flow<Repository?>
    suspend fun toggleBookmark(repositoryId: Long)
    fun getAllBookmarks(): Flow<List<Repository>>
    suspend fun removeBookmark(repositoryId: Long)
    suspend fun addBookmark(repository: Repository)
    suspend fun addTag(repositoryId: Long, name: String, color: Long)
}

@Singleton
class RoomLocalDataSource @Inject constructor(
    private val repositoryDao: RepositoryDao,
    private val bookmarkDao: BookmarkDao
) : LocalDataSource {

    override suspend fun getTrendingRepositories(language: String?): List<Repository> {
        return repositoryDao.getTrendingRepositories(language).map { it.toDomain() }
    }

    override fun observeTrendingRepositories(language: String?): Flow<List<Repository>> {
        return repositoryDao.observeTrendingRepositories(language).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveTrendingRepositories(repositories: List<Repository>) {
        val entities = repositories.map { it.toEntity() }
        repositoryDao.upsertAll(entities)
    }

    override suspend fun getRepository(id: Long): Repository? {
        return repositoryDao.getRepository(id).firstOrNull()?.toDomain()
    }

    override fun observeRepository(id: Long): Flow<Repository?> {
        return repositoryDao.getRepository(id).map { it?.toDomain() }
    }

    override suspend fun toggleBookmark(repositoryId: Long) {
        val existing = repositoryDao.getRepository(repositoryId).firstOrNull()
        if (existing != null) {
            val newStatus = !existing.isBookmarked
            repositoryDao.updateBookmarkStatus(repositoryId, newStatus)
            if (newStatus) {
                bookmarkDao.insertBookmark(BookmarkEntity(repositoryId = repositoryId))
            } else {
                bookmarkDao.deleteBookmarkById(repositoryId)
            }
        }
    }

    override fun getAllBookmarks(): Flow<List<Repository>> {
        return bookmarkDao.getAllBookmarks().map { list ->
            list.map { it.toDomainRepository() }
        }
    }

    override suspend fun removeBookmark(repositoryId: Long) {
        repositoryDao.updateBookmarkStatus(repositoryId, false)
        bookmarkDao.deleteBookmarkById(repositoryId)
    }

    override suspend fun addBookmark(repository: Repository) {
        repositoryDao.upsert(repository.toEntity().copy(isBookmarked = true))
        bookmarkDao.insertBookmark(BookmarkEntity(repositoryId = repository.id))
    }

    override suspend fun addTag(repositoryId: Long, name: String, color: Long) {
        bookmarkDao.addTag(
            BookmarkTagEntity(
                bookmarkId = repositoryId,
                name = name,
                colorArgb = color
            )
        )
    }
}
