package com.example.task.presentation.profile.profileRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemEventBinding
import com.example.task.databinding.ItemFilterButtonsBinding
import com.example.task.databinding.ItemUserCardBinding
import com.example.task.domain.usecases.ManageSubscriptionToUserUseCase

class ProfileAdapter(
    private val onRadioButtonChecked: (Int) -> Unit,
    private val onEditButtonPressed: () -> Unit,
    private val onEventItemPressed: (Long) -> Unit,
    private val manageSubscriptionToUser: () -> Unit,
    private val onCreateNewClicked: () -> Unit
) : PagingDataAdapter<ProfileUIModel, RecyclerView.ViewHolder>(ITEM_DIFF) {

    private var userViewHolder : UserViewHolder? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> {
                val item = getItem(position) as ProfileUIModel.User
                (holder as UserViewHolder).onBind(item)
            }
            1 -> (holder as ButtonsViewHolder).onBind(getItem(position) as ProfileUIModel.Buttons)
            else -> (holder as EventViewHolder).onBind(getItem(position) as ProfileUIModel.Event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user_card -> {
                userViewHolder = UserViewHolder(
                    binding = ItemUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onEditButtonPressed = onEditButtonPressed,
                    manageSubscriptionToUser = manageSubscriptionToUser,
                    onCreateNewClicked = onCreateNewClicked
                )
                userViewHolder!!
            }
            R.layout.item_filter_buttons -> ButtonsViewHolder(
                binding = ItemFilterButtonsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onRadioButtonChecked = onRadioButtonChecked
            )
            R.layout.item_event -> EventViewHolder(
                binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onEventItemPressed = onEventItemPressed
            )
            else -> throw NoSuchElementException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_user_card
            1 -> R.layout.item_filter_buttons
            else -> R.layout.item_event
        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<ProfileUIModel>() {
            override fun areItemsTheSame(
                oldItem: ProfileUIModel,
                newItem: ProfileUIModel
            ): Boolean {
                return when {
                    oldItem is ProfileUIModel.User && newItem is ProfileUIModel.User ->
                        oldItem.id == newItem.id
                    oldItem is ProfileUIModel.Buttons && newItem is ProfileUIModel.Buttons ->
                        oldItem == newItem
                    oldItem is ProfileUIModel.Event && newItem is ProfileUIModel.Event ->
                        oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(
                oldItem: ProfileUIModel,
                newItem: ProfileUIModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}