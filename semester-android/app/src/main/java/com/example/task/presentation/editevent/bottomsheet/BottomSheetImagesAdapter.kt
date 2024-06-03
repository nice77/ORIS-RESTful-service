package com.example.task.presentation.editevent.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemBottomSheetImageBinding
import com.example.task.databinding.ItemButtonBinding

class BottomSheetImagesAdapter(
    private val onAddButtonClicked: () -> Unit,
    private val onSubmitButtonClicked: () -> Unit,
    private val onItemSwiped: (BottomUiModel.Image) -> Unit
) : ListAdapter<BottomUiModel, RecyclerView.ViewHolder>(ITEM_DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_bottom_sheet_image -> EventImageViewHolder(
                binding = ItemBottomSheetImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemSwiped = onItemSwiped
            )
            R.layout.item_button -> ButtonViewHolder(
                binding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onAddButtonCLicked = onAddButtonClicked,
                onSubmitButtonClicked = onSubmitButtonClicked
            )
            else -> throw NoSuchElementException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is BottomUiModel.Image -> (holder as EventImageViewHolder).onBind(item)
            is BottomUiModel.Button -> (holder as ButtonViewHolder).onBind(position == itemCount - 1)
            else -> throw NoSuchElementException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BottomUiModel.Image -> R.layout.item_bottom_sheet_image
            is BottomUiModel.Button -> R.layout.item_button
        }
    }

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<BottomUiModel>() {
            override fun areItemsTheSame(oldItem: BottomUiModel, newItem: BottomUiModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BottomUiModel, newItem: BottomUiModel): Boolean {
                return when {
                    oldItem is BottomUiModel.Image && newItem is BottomUiModel.Image -> oldItem.path == newItem.path
                    else -> oldItem == newItem
                }
            }
        }
    }
}