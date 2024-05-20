package com.example.newsapp.ui.home.news.newsDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.api.model.newsResponse.News
import com.example.newsapp.databinding.ActivityNewsDetailsBinding

class NewsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailsBinding
    private lateinit var news: News
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        news = ((intent.getParcelableExtra("news") as? News)!!)
        binding.news = news
    }
}
