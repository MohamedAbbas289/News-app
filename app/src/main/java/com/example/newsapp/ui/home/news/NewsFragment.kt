package com.example.newsapp.ui.home.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.ViewError
import com.example.newsapp.ui.home.newsDetails.NewsDetailsActivity
import com.example.newsapp.ui.showMessage
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var sourceObj: Source
    private var isLoading = false
    var currentPage = 1
    val pageSize = 20
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        viewModel.getNewsSources()
    }

    private fun initObservers() {
        viewModel.shouldShowLoading.observe(viewLifecycleOwner) { shouldShowLoading ->
            binding.progressBar.isVisible = shouldShowLoading
        }

        viewModel.sourcesLivedata.observe(viewLifecycleOwner) { sources ->
            bindTabs(sources)
            viewModel.getNews(sourceObj.id, currentPage, pageSize)
        }

        viewModel.newsLivedata.observe(viewLifecycleOwner) { news ->
            adapter.bindNews(news)
        }

        viewModel.errorLivedata.observe(viewLifecycleOwner) { viewError ->
            handleError(viewError)
        }

    }

    private val adapter = NewsAdapter()
    private fun initViews() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val visibleThreshold = 3
                if (!isLoading && totalItemCount - lastVisiblePosition <= visibleThreshold) {
                    isLoading = true
                    currentPage++
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(3000)

                        // Call the viewModel.getNews() method on the main thread
                        withContext(Dispatchers.Main) {
                            viewModel.getNews(sourceObj.id, currentPage, pageSize)
                            isLoading = false
                        }
                    }
                }
            }
        })
        adapter.onItemClickListener = NewsAdapter.OnItemClickListener { news ->
            val intent = Intent(requireContext(), NewsDetailsActivity::class.java)
            intent.putExtra("news", news)
            startActivity(intent)
        }
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
                sourceObj = source
                viewModel.getNews(source.id, currentPage, pageSize)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                sourceObj = source
                viewModel.getNews(source.id, currentPage, pageSize)
            }

        })
        binding.tabLayout.getTabAt(0)?.select()

    }


    private fun handleError(viewError: ViewError) {
        showMessage(
            message = viewError.message
                ?: viewError.throwable?.localizedMessage
                ?: "Error",
            posActionName = "Try again",
            posAction = { dialog, which ->
                dialog.dismiss()
                viewError.onTryAgainClickListener?.onTryAgainClick()
            },
            negActionName = "Cancel",
            negAction = { dialog, which ->
                dialog.dismiss()
            })
    }


}