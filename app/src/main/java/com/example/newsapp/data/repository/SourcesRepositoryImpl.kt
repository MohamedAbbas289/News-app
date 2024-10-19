package com.example.newsapp.data.repository

import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.dataSource.SourcesDataSource
import com.example.newsapp.localModel.Category
import com.example.newsapp.repository.sourcesRepository.SourcesRepository
import javax.inject.Inject

class SourcesRepositoryImpl @Inject constructor(
    val sourcesDataSource: SourcesDataSource
) : SourcesRepository {
    override suspend fun getSources(category: Category): List<Source?>? {
        val sources = sourcesDataSource.getSources(category)
        return sources
    }
}