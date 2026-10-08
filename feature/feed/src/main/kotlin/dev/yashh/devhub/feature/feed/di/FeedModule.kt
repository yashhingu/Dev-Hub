package dev.yashh.devhub.feature.feed.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.yashh.devhub.feature.feed.repository.FeedRepository
import dev.yashh.devhub.feature.feed.repository.FeedRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedModule {

    @Binds
    @Singleton
    abstract fun bindFeedRepository(
        impl: FeedRepositoryImpl
    ): FeedRepository
}
