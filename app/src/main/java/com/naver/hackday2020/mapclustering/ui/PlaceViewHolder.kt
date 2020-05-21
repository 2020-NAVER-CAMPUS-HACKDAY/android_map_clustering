package com.naver.hackday2020.mapclustering.ui

import androidx.recyclerview.widget.RecyclerView
import com.naver.hackday2020.mapclustering.databinding.ItemBottomSheetBinding
import com.naver.hackday2020.mapclustering.listener.OnItemClickListener

class PlaceViewHolder(
    private val binding: ItemBottomSheetBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        naverPlaceItem: NaverPlaceItem,
        onItemClickListener: OnItemClickListener<NaverPlaceItem>?
    ) {
        binding.apply {
            item = naverPlaceItem
            listener = onItemClickListener
        }
    }
}