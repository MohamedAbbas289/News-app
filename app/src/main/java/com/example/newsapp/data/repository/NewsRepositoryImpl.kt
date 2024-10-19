package com.example.newsapp.data.repository

import com.example.newsapp.data.api.model.newsResponse.News
import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.dataSource.NewsDataSource
import com.example.newsapp.repository.newsRepository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsDataSource: NewsDataSource) :
    NewsRepository {
    override suspend fun getNews(source: Source, pageNumber: Int, pageSize: Int): List<News?>? {
        val news = newsDataSource.getNews(source, pageNumber, pageSize)
        return news
    }
}