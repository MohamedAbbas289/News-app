package com.example.newsapp.ui.home.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.R
import com.example.newsapp.localModel.Category

class CategoriesViewModel : ViewModel() {
    private val _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData: LiveData<List<Category>> = _categoriesLiveData

    init {
        getCategories()
    }

    fun getCategories() {
        _categoriesLiveData.postValue(
            listOf(
                Category("sports", R.drawable.sports_item),
                Category("general", R.drawable.politics_item),
                Category("health", R.drawable.health_item),
                Category("business", R.drawable.business_item),
                Category("entertainment", R.drawable.enviroment_item),
                Category("science", R.drawable.science_item)
            )
        )
    }

}