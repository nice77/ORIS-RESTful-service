package com.example.task.presentation.profile.profileRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemFilterButtonsBinding

class ButtonsViewHolder(
    private val binding : ItemFilterButtonsBinding,
    private val onRadioButtonChecked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            onRadioButtonChecked(i)
        }
    }

    fun onBind(uiModel: ProfileUIModel.Buttons) {

    }

}