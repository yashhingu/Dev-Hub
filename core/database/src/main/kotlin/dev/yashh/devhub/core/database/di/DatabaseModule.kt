package dev.yashh.devhub.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.yashh.devhub.core.database.DevHubDatabase
import dev.yashh.devhub.core.database.dao.BookmarkDao
import dev.yashh.devhub.core.database.dao.RepositoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDevHubDatabase(
        @ApplicationContext context: Context
    ): DevHubDatabase {
        return Room.databaseBuilder(
            context,
            DevHubDatabase::class.java,
            "devhub_database"
        )
        .fallbackToDestructiveMigration() // Usually fine for caching, careful in prod!
        .build()
    }

    @Provides
    fun provideRepositoryDao(database: DevHubDatabase): RepositoryDao {
        return database.repositoryDao()
    }

    @Provides
    fun provideBookmarkDao(database: DevHubDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        repositoryDao: RepositoryDao,
        bookmarkDao: BookmarkDao
    ): LocalDataSource {
        return RoomLocalDataSource(repositoryDao, bookmarkDao)
    }
}
