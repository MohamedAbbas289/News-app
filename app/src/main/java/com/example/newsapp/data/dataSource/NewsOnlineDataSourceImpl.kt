package com.example.newsapp.data.dataSource

import com.example.newsapp.data.api.WebServices
import com.example.newsapp.data.api.model.newsResponse.News
import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.dataSource.NewsDataSource
import javax.inject.Inject

class NewsOnlineDataSourceImpl @Inject constructor(
    private val webServices: WebServices
) : NewsDataSource {
    override suspend fun getNews(source: Source, pageNumber: Int, pageSize: Int): List<News?>? {
        val response = webServices.getNews(
            sources = source.id!!, page = pageNumber, pageSize = pageSize
        )
        return response.articles
    }
}