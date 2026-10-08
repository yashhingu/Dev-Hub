package dev.yashh.devhub.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.yashh.devhub.core.database.converter.Converters
import dev.yashh.devhub.core.database.dao.BookmarkDao
import dev.yashh.devhub.core.database.dao.RepositoryDao
import dev.yashh.devhub.core.database.entity.BookmarkEntity
import dev.yashh.devhub.core.database.entity.BookmarkTagEntity
import dev.yashh.devhub.core.database.entity.RepositoryEntity

@Database(
    entities = [
        RepositoryEntity::class,
        BookmarkEntity::class,
        BookmarkTagEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class DevHubDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
    abstract fun bookmarkDao(): BookmarkDao
}
