package com.example.newsapp.repository.newsRepository

import com.example.newsapp.data.api.model.newsResponse.News
import com.example.newsapp.data.api.model.sourcesResponse.Source

interface NewsRepository {
    suspend fun getNews(source: Source, pageNumber: Int, pageSize: Int): List<News?>?
}