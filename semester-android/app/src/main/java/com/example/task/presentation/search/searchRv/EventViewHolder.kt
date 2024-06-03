package com.example.task.presentation.search.searchRv

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.task.BuildConfig
import com.example.task.databinding.ItemEventBinding

class EventViewHolder(
    private val binding : ItemEventBinding,
    private val onEventItemPressed: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentEventId: Long? = null

    init {
        binding.root.setOnClickListener {
            currentEventId?.let {
                onEventItemPressed(it)
            }
        }
    }

    fun onBind(item : SearchUiModel.Event) {
        currentEventId = item.id
        if (item.eventImage.isNotEmpty()) {
            binding.eventImg.load(BuildConfig.PATH + item.eventImage)
        }
        binding.eventTitleTv.text = item.title
    }

}