package com.example.task.presentation.main.mainRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemTitleBinding

class ItemTitleViewHolder(
    private val binding: ItemTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(uiModel: MainUiModel.Title) {
        binding.run {
            rvTitleTv.text = root.context.getString(uiModel.textRes)
        }
    }
}
