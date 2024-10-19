package com.example.newsapp.ui.home.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.api.model.newsResponse.News
import com.example.newsapp.data.api.model.newsResponse.NewsResponse
import com.example.newsapp.data.api.model.sourcesResponse.Source
import com.example.newsapp.data.api.model.sourcesResponse.SourcesResponse
import com.example.newsapp.localModel.Category
import com.example.newsapp.repository.newsRepository.NewsRepository
import com.example.newsapp.repository.sourcesRepository.SourcesRepository
import com.example.newsapp.ui.ViewError
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val sourcesRepo: SourcesRepository,
    private val newsRepo: NewsRepository
) : ViewModel() {
    val shouldShowLoading = MutableLiveData<Boolean>()
    val sourcesLivedata = MutableLiveData<List<Source?>?>()
    val newsLivedata = MutableLiveData<List<News?>?>()
    val errorLivedata = MutableLiveData<ViewError>()


    fun getNewsSources(category: Category) {
        viewModelScope.launch {
            shouldShowLoading.postValue(true)
            try {
                val sources = sourcesRepo.getSources(category)
                sourcesLivedata.postValue(sources)
            } catch (e: HttpException) {
                val errorBodyJsonString = e.response()?.errorBody()?.string()
                val errorResponse =
                    Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)
                errorLivedata.postValue(ViewError(
                    message = errorResponse.message
                ) {
                    getNewsSources(category)
                })
            } catch (e: Exception) {
                errorLivedata.postValue(ViewError(
                    throwable = e
                ) {
                    getNewsSources(category)
                })
            } finally {
                shouldShowLoading.postValue(false)
            }
        }
    }

    fun getNews(source: Source, page: Int, pageSize: Int) {
        shouldShowLoading.postValue(true)
        viewModelScope.launch {
            try {
                val articles = newsRepo.getNews(source, page, pageSize)
                newsLivedata.postValue(articles)
            } catch (e: HttpException) {
                val errorBodyJsonString = e.response()?.errorBody()?.string()
                    val errorResponse =
                        Gson().fromJson(errorBodyJsonString, NewsResponse::class.java)
                    errorLivedata.postValue(ViewError(
                        message = errorResponse.message
                    ) {
                        getNews(source, page, pageSize)
                    })
            } catch (e: Exception) {
                errorLivedata.postValue(
                    ViewError(
                        throwable = e
                    ) {
                        getNews(source, page, pageSize)
                    })
            } finally {
                shouldShowLoading.postValue(false)
            }
        }
    }
}