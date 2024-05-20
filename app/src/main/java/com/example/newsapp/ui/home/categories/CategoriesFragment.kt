package com.example.newsapp.ui.home.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.databinding.FragmentCategoriesBinding
import com.example.newsapp.localModel.Category

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.bindCategories(categories)
        }
    }

    private val categoriesAdapter = CategoriesAdapter()
    private fun initViews() {
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        categoriesAdapter.onItemClickListener =
            CategoriesAdapter.OnItemClickListener { position, category ->
                onCategoryClickListener?.onCategoryClick(category!!)
            }
    }

    var onCategoryClickListener: OnCategoryClickListener? = null

    fun interface OnCategoryClickListener {
        fun onCategoryClick(category: Category)
    }
}