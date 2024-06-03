package com.example.task.presentation.main.mainRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemEventBinding
import com.example.task.databinding.ItemTitleBinding
import com.example.task.databinding.ItemUsersRecyclerBinding
import com.example.task.presentation.main.MainViewModel


class MainAdapter(
    private val viewModel: MainViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val onEventItemPressed: (Long) -> Unit,
    private val onUserItemClicked: (Long) -> Unit
) : PagingDataAdapter<MainUiModel, RecyclerView.ViewHolder>(ITEM_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_users_recycler -> ItemUsersViewHolder(
                binding = ItemUsersRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                viewModel = viewModel,
                viewLifecycleOwner = viewLifecycleOwner,
                onUserItemClicked = onUserItemClicked
            )

            R.layout.item_event -> EventViewHolder(
                binding = ItemEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onEventItemPressed = onEventItemPressed
            )

            R.layout.item_title -> ItemTitleViewHolder(
                binding = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw NoSuchElementException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem((position))) {
            is MainUiModel.Event -> (holder as EventViewHolder).onBind(item)
            is MainUiModel.Title -> (holder as ItemTitleViewHolder).onBind(item)
            MainUiModel.Users -> Unit
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainUiModel.Event -> R.layout.item_event
            is MainUiModel.Title -> R.layout.item_title
            MainUiModel.Users -> R.layout.item_users_recycler
            null -> 0
        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<MainUiModel>() {
            override fun areItemsTheSame(
                oldItem: MainUiModel,
                newItem: MainUiModel
            ): Boolean {
                return when {
                    oldItem is MainUiModel.Event && newItem is MainUiModel.Event ->
                        oldItem.id == newItem.id

                    oldItem is MainUiModel.Title && newItem is MainUiModel.Title ->
                        oldItem.textRes == newItem.textRes

                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: MainUiModel,
                newItem: MainUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
