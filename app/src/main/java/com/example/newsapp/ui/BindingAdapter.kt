package com.example.newsapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

@BindingAdapter("url")
fun bindImageWithUrl(
    imageView: ImageView,
    url: String?
) {

    Glide.with(imageView)
        .load(url)
        .placeholder(R.drawable.news_logo)
        .into(imageView)

}

@BindingAdapter(value = ["showLoading"])
fun setVisibility(view: View, value: Boolean) {
    if (value) {
        view.isVisible = true
    } else {
        view.isVisible = false
    }
}

@BindingAdapter("imageUrl")
fun bindImageDetailsWithUrl(
    imageView: ImageView,
    url: String?
) {

    Glide.with(imageView)
        .load(url)
        .into(imageView)

}

@BindingAdapter("launcher")
fun clickToLaunchUrl(view: View, url: String) {
    view.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

}

@BindingAdapter("backgroundColor")
fun changeCardViewBackground(cardView: CardView, color: Int) {
    cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, color))
}

@BindingAdapter("imageId")
fun loadImageByIdDrawable(imageView: ImageView, image: Int) {
    imageView.setImageResource(image)
}


@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("getTimeAgo")
fun getTimeAgo(textView: TextView, publishedAt: String) {
    try {
        val publishedTime = ZonedDateTime.parse(publishedAt)
        val currentTime = ZonedDateTime.now()

        val diff = Duration.between(publishedTime, currentTime).abs()
        val seconds = diff.seconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            days > 0 -> textView.text = "$days days ago"
            hours > 0 -> textView.text = "$hours hours ago"
            minutes > 0 -> textView.text = "$minutes minutes ago"
            else -> textView.text = "$seconds seconds ago"
        }
    } catch (e: DateTimeParseException) {
        textView.text = "Invalid date format"
    }
}

