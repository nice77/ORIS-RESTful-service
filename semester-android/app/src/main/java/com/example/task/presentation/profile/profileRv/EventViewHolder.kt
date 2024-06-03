package com.example.task.presentation.profile.profileRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.databinding.ItemEventBinding
import com.example.task.utils.loadCaching

class EventViewHolder(
    private val binding: ItemEventBinding,
    private val onEventItemPressed: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentEventId : Long? = null

    init {
        binding.root.setOnClickListener {
            currentEventId?.let {
                onEventItemPressed(it)
            }
        }
    }

    fun onBind(uiModel: ProfileUIModel.Event) {
        binding.run {
            currentEventId = uiModel.id
            if (uiModel.eventImage.isNotEmpty()) {
                eventImg.loadCaching("${BuildConfig.PATH}${uiModel.eventImage}")
            }
            eventTitleTv.text = uiModel.title
        }
    }

}