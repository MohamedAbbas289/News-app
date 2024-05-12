package com.example.newsapp.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.api.ApiConstants
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.showMessage
import com.example.newsapp.ui.home.news.NewsFragment.OnTryAgainClickListener
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getNewsSources()
    }

    val adapter = NewsAdapter()
    private fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    private fun getNewsSources() {
        binding.progressBar.visibility = View.VISIBLE
        ApiManager
            .getApis()
            .getSources()
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        //show tabs
                        bindTabs(response.body()?.sources)
                    } else {
                        val errorBodyJsonString = response.errorBody()?.string()
                        val errorResponse =
                            Gson().fromJson(errorBodyJsonString, SourcesResponse::class.java)
                        handleError(errorResponse.message, onClick = OnTryAgainClickListener {
                            getNewsSources()
                        })
                    }
                }

                override fun onFailure(p0: Call<SourcesResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    handleError(t, onClick = OnTryAgainClickListener {
                        getNewsSources()
                    })
                }

            })
    }

    private fun bindTabs(sources: List<Source?>?) {
        if (sources == null) return
        sources.forEach { source ->
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source
            binding.tabLayout.addTab(tab)
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                getNews(source.id)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                getNews(source.id)
            }

        })
        binding.tabLayout.getTabAt(0)?.select()

    }

    private fun getNews(sourceId: String?) {
        binding.progressBar.visibility = View.VISIBLE
        ApiManager
            .getApis()
            .getNews(ApiConstants.apikey, sources = sourceId ?: "")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        //show news
                        adapter.bindNews(response.body()?.articles)
                        return
                    }
                    val errorBodyJsonString = response.errorBody()?.string()
                    val errorResponse =
                        Gson().fromJson(errorBodyJsonString, NewsResponse::class.java)
                    handleError(errorResponse.message, onClick = OnTryAgainClickListener {
                        getNews(sourceId)
                    })
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    handleError(t, onClick = OnTryAgainClickListener {
                        getNews(sourceId)
                    })
                }

            })
    }


    fun interface OnTryAgainClickListener {
        fun onTryAgainClick()
    }

    fun handleError(t: Throwable, onClick: OnTryAgainClickListener) {
        showMessage(message = t.localizedMessage ?: "Error",
            posActionName = "Try again",
            posAction = { dialog, which ->
                dialog.dismiss()
                onClick.onTryAgainClick()
            },
            negActionName = "Cancel",
            negAction = { dialog, which ->
                dialog.dismiss()
            })
    }

    fun handleError(message: String?, onClick: OnTryAgainClickListener) {
        showMessage(message = message ?: "Error",
            posActionName = "Try again",
            posAction = { dialog, which ->
                dialog.dismiss()
                onClick.onTryAgainClick()
            },
            negActionName = "Cancel",
            negAction = { dialog, which ->
                dialog.dismiss()
            })
    }
}