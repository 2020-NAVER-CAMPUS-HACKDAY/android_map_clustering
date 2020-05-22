package com.naver.hackday2020.mapclustering.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.naver.hackday2020.mapclustering.databinding.ItemBottomSheetBinding
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener


class PlaceRecyclerAdapter : ListAdapter<NaverPlaceItem, PlaceViewHolder>(PlaceDiffUtilCallback()) {
    var onItemClickListener: OnItemClickListener<NaverPlaceItem>? = null

    fun replace(newItem: NaverPlaceItem) = submitList(listOf(newItem))

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