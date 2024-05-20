package com.example.newsapp.ui.home.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemCategoriesBinding
import com.example.newsapp.localModel.Category

class CategoriesAdapter(var categories: List<Category?>? = null) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category?) {
            binding.category = category
            binding.invalidateAll()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoriesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categories?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoriesList = categories!![position]
        holder.bind(categoriesList)
        if (onItemClickListener != null) {
            holder.itemView.rootView.setOnClickListener {
                onItemClickListener!!.onItemClick(position, categoriesList)
            }
        }
    }

    fun bindCategories(categories: List<Category?>?) {
        this.categories = categories
        notifyDataSetChanged()
    }

    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, categories: Category?)
    }
}