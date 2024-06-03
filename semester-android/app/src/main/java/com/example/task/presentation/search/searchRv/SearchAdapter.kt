package com.example.task.presentation.search.searchRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemEventBinding
import com.example.task.databinding.ItemHorizontalUserBinding
import com.example.task.databinding.ItemSearchBarBinding

class SearchAdapter(
    private val onTextUpdate: (String) -> Unit,
    private val onFilterButtonPressed: () -> Unit,
    private val onEventItemPressed: (Long) -> Unit,
    private val onUserItemPressed: (Long) -> Unit
) : PagingDataAdapter<SearchUiModel, RecyclerView.ViewHolder>(ITEM_DIFF) {

    private var searchBar : SearchBarViewHolder? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SearchUiModel.Event -> (holder as EventViewHolder).onBind(item)
            is SearchUiModel.User -> (holder as UserViewHolder).onBind(item)
            is SearchUiModel.SearchBar -> (holder as SearchBarViewHolder).onBind(item)
            else -> throw NoSuchElementException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_search_bar -> {
                val toReturn = SearchBarViewHolder(
                    binding = ItemSearchBarBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onTextUpdate = onTextUpdate,
                    onFilterButtonPressed = onFilterButtonPressed
                )
                searchBar = searchBar ?: toReturn
                toReturn
            }
            R.layout.item_event -> EventViewHolder(
                binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onEventItemPressed = onEventItemPressed
            )
            R.layout.item_horizontal_user -> UserViewHolder(
                binding = ItemHorizontalUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onUserItemPressed = onUserItemPressed
            )
            else -> throw NoSuchElementException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchUiModel.SearchBar -> R.layout.item_search_bar
            is SearchUiModel.Event -> R.layout.item_event
            else -> R.layout.item_horizontal_user
        }
    }

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<SearchUiModel>() {
            override fun areItemsTheSame(oldItem: SearchUiModel, newItem: SearchUiModel): Boolean {
                return when {
                    oldItem is SearchUiModel.Event && newItem is SearchUiModel.Event -> oldItem.id == newItem.id
                    oldItem is SearchUiModel.User && newItem is SearchUiModel.User -> oldItem.id == newItem.id
                    oldItem is SearchUiModel.SearchBar && newItem is SearchUiModel.SearchBar -> true
                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: SearchUiModel,
                newItem: SearchUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}