package com.naver.hackday2020.mapclustering.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.naver.hackday2020.mapclustering.databinding.ItemBottomSheetBinding
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener


class PlaceRecyclerAdapter : RecyclerView.Adapter<PlaceViewHolder>() {
    var onItemClickListener: OnItemClickListener<NaverPlaceItem>? = null

    private val diffUtil = AsyncListDiffer(this, PlaceDiffUtilCallback())

    fun replace(newItem: NaverPlaceItem) = diffUtil.submitList(listOf(newItem))

    fun replace(newItems: List<NaverPlaceItem>) = diffUtil.submitList(newItems)

    fun getItem(position: Int) = diffUtil.currentList[position]

    override fun getItemCount(): Int = diffUtil.currentList.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClickListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaceViewHolder(
        ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}

class PlaceDiffUtilCallback : DiffUtil.ItemCallback<NaverPlaceItem>() {

    override fun areItemsTheSame(oldItem: NaverPlaceItem, newItem: NaverPlaceItem) =
        oldItem.place.id == newItem.place.id

    override fun areContentsTheSame(oldItem: NaverPlaceItem, newItem: NaverPlaceItem) =
        oldItem.place == newItem.place
}