package com.example.task.presentation.event.imageRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.databinding.ItemEventImageBinding
import com.example.task.utils.loadCaching

class EventImageViewHolder(
    private val binding : ItemEventImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(imageUri : String) {
        binding.eventIv.loadCaching(BuildConfig.PATH + imageUri)
    }

}