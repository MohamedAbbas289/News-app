package com.example.newsapp.ui.home.news

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.api.model.newsResponse.News
import com.example.newsapp.databinding.ItemNewsBinding
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

class NewsAdapter(var newsList: List<News?>? = null) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = newsList?.size ?: 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList!![position]
        holder.binding.title.text = news?.title
        holder.binding.description.text = news?.description
        holder.binding.time.text = getTimeAgo(news?.publishedAt.toString())
        Glide.with(holder.itemView.context)
            .load(news?.urlToImage)
            .placeholder(R.drawable.news_logo)
            .into(holder.binding.image)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeAgo(publishedAt: String): String {
        return try {
            val publishedTime = ZonedDateTime.parse(publishedAt)
            val currentTime = ZonedDateTime.now()

            val diff = Duration.between(publishedTime, currentTime).abs()
            val seconds = diff.seconds
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            when {
                days > 0 -> "$days days ago"
                hours > 0 -> "$hours hours ago"
                minutes > 0 -> "$minutes minutes ago"
                else -> "$seconds seconds ago"
            }
        } catch (e: DateTimeParseException) {
            "Invalid date format"
        }
    }

    fun bindNews(articles: List<News?>?) {
        newsList = articles
        notifyDataSetChanged()
    }

}
