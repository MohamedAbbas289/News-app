package com.example.newsapp.dataSource

import com.example.newsapp.data.api.model.newsResponse.News
import com.example.newsapp.data.api.model.sourcesResponse.Source

interface NewsDataSource {
    suspend fun getNews(source: Source, pageNumber: Int, pageSize: Int): List<News?>?
}