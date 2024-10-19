package com.example.newsapp.dataSource

import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.localModel.Category

interface SourcesDataSource {
    suspend fun getSources(category: Category): List<Source?>?
}