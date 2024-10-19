package com.example.newsapp.data.repository

import com.example.newsapp.data.dataSource.NewsOnlineDataSourceImpl
import com.example.newsapp.data.dataSource.SourcesOnlineDataSourceImpl
import com.example.newsapp.dataSource.NewsDataSource
import com.example.newsapp.dataSource.SourcesDataSource
import com.example.newsapp.repository.newsRepository.NewsRepository
import com.example.newsapp.repository.sourcesRepository.SourcesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun provideSourcesRepository(
        sourcesRepositoryImpl: SourcesRepositoryImpl
    ): SourcesRepository

    @Binds
    abstract fun provideSourceDataSource(
        sourcesOnlineDataSourceImpl: SourcesOnlineDataSourceImpl
    ): SourcesDataSource

    @Binds
    abstract fun provideNewsDataSource(
        newsOnlineDataSourceImpl: NewsOnlineDataSourceImpl
    ): NewsDataSource

    @Binds
    abstract fun provideNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}