package com.example.newsapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityHomeBinding
import com.example.newsapp.localModel.Category
import com.example.newsapp.ui.home.categories.CategoriesFragment
import com.example.newsapp.ui.home.news.NewsFragment
import com.example.newsapp.ui.home.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), CategoriesFragment.OnCategoryClickListener {
    private lateinit var binding: ActivityHomeBinding
    private var categoriesFragment = CategoriesFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoriesFragment.onCategoryClickListener = this

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, categoriesFragment)
            .commit()

        showToggleNavDrawer()
    }

    private fun showToggleNavDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, binding.myDrawer, binding.toolbar, R.string.open, R.string.close
        )
        binding.myDrawer.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.categories -> {
                    showFragment(categoriesFragment)
                }

                R.id.settings -> {
                    showFragment(SettingsFragment())
                }

                else -> {}
            }
            binding.myDrawer.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCategoryClick(category: Category) {
        showCategoryDetailsFragment(category)
    }

    private fun showCategoryDetailsFragment(category: Category) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NewsFragment.getInstance(category))
            .addToBackStack(null)
            .commit()
    }
}
