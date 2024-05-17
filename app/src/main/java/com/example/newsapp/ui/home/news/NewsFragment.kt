package com.example.newsapp.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.ViewError
import com.example.newsapp.ui.showMessage
import com.google.android.material.tabs.TabLayout

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
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
        }

        viewModel.newsLivedata.observe(viewLifecycleOwner) { news ->
            adapter.bindNews(news)
        }

        viewModel.errorLivedata.observe(viewLifecycleOwner) { viewError ->
            handleError(viewError)
        }

    }

    val adapter = NewsAdapter()
    private fun initViews() {
        binding.recyclerView.adapter = adapter
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
                viewModel.getNews(source.id)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                viewModel.getNews(source.id)
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