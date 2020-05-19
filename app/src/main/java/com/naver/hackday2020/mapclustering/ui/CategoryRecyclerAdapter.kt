package com.naver.hackday2020.mapclustering.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naver.hackday2020.mapclustering.R
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryRecyclerAdapter(
    private var categories: List<String>
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

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemCategory = view.item_category
        private val categoryName = view.category_name

        fun bind(position: Int) {
            val category = categories[position]
            categoryName.text = category
            itemCategory.setOnClickListener { onCategoryClick.onItemClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) = holder.bind(position)
}