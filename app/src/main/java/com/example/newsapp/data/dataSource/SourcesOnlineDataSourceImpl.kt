package com.example.newsapp.data.dataSource

import com.example.newsapp.data.api.WebServices
import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.dataSource.SourcesDataSource
import com.example.newsapp.localModel.Category
import javax.inject.Inject

class SourcesOnlineDataSourceImpl @Inject constructor(
    val webServices: WebServices
) : SourcesDataSource {
    override suspend fun getSources(category: Category): List<Source?>? {
        val response = webServices.getSources(category = category.id)
        return response.sources
    }
}