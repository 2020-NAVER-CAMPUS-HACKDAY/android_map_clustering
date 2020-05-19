package com.naver.hackday2020.mapclustering.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naver.hackday2020.mapclustering.databinding.ItemCategoryBinding
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener


class CategoryRecyclerAdapter(
    private val categories: List<String>
): RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

    private var onCategoryClick : OnItemClickListener<String> =
            object : OnItemClickListener<String> {
                override fun onItemClick(item: String) {
                    // do nothing
                }
            }

    fun setOnItemClickListener(onClick: (item: String) -> Unit) {
        onCategoryClick = object : OnItemClickListener<String> {
            override fun onItemClick(item: String) {
                onClick(item)
            }
        }
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val category = categories[position]
            binding.categoryName.text = category
            binding.itemCategory.setOnClickListener { onCategoryClick.onItemClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) = holder.bind(position)
}