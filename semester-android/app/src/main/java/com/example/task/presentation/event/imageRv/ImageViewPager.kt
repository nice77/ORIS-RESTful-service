package com.example.task.presentation.event.imageRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemEventImageBinding

class ImageViewPager(
    private val imageList : List<String>
) : RecyclerView.Adapter<EventImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventImageViewHolder {
        return EventImageViewHolder(
            binding = ItemEventImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: EventImageViewHolder, position: Int) {
        holder.onBind(imageUri = imageList[position])
    }
}