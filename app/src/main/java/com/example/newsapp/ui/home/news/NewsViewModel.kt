package com.example.newsapp.ui.home.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.api.ApiConstants
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.newsResponse.News
import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.example.newsapp.ui.ViewError
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val shouldShowLoading = MutableLiveData<Boolean>()
    val sourcesLivedata = MutableLiveData<List<Source?>?>()
    val newsLivedata = MutableLiveData<List<News?>?>()
    val errorLivedata = MutableLiveData<ViewError>()

    fun getNewsSources() {
        shouldShowLoading.postValue(true)
        ApiManager
            .getApis()
            .getSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    shouldShowLoading.postValue(false)
                    if (response.isSuccessful) {
                        //show tabs
                        sourcesLivedata.postValue(response.body()?.sources)
                    } else {
                        val errorBodyJsonString = response.errorBody()?.string()
                        val errorResponse =
                            Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)
                        errorLivedata.postValue(ViewError(
                            message = errorResponse.message
                        ) {
                            getNewsSources()
                        })
                    }
                }

                override fun onFailure(p0: Call<SourcesResponse>, t: Throwable) {
                    shouldShowLoading.postValue(false)
                    errorLivedata.postValue(ViewError(
                        throwable = t
                    ) {
                        getNewsSources()
                    })

                }

            })
    }

    fun getNews(sourceId: String?) {
        shouldShowLoading.postValue(true)
        ApiManager
            .getApis()
            .getNews(ApiConstants.API_KEY, sources = sourceId ?: "")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    shouldShowLoading.postValue(false)
                    if (response.isSuccessful) {
                        //show news
                        newsLivedata.postValue(response.body()?.articles)
                        return
                    }
                    val errorBodyJsonString = response.errorBody()?.string()
                    val errorResponse =
                        Gson().fromJson(errorBodyJsonString, NewsResponse::class.java)
                    errorLivedata.postValue(ViewError(
                        message = errorResponse.message
                    ) {
                        getNews(sourceId)
                    })
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    shouldShowLoading.postValue(false)
                    errorLivedata.postValue(ViewError(
                        throwable = t
                    ) {
                        getNews(sourceId)
                    })
                }

            })
    }
}