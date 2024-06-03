package com.example.task.presentation.main.usersRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.task.databinding.ItemUserBinding
import com.example.task.domain.models.UserDomainModel

class UsersAdapter(
    private val onUserItemClicked: (Long) -> Unit
) : PagingDataAdapter<UserDomainModel, UserViewHolder>(ITEM_DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onUserItemClicked = onUserItemClicked
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    companion object {
        val ITEM_DIFF: DiffUtil.ItemCallback<UserDomainModel> = object : DiffUtil.ItemCallback<UserDomainModel>() {
            override fun areItemsTheSame(oldItem: UserDomainModel, newItem: UserDomainModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UserDomainModel,
                newItem: UserDomainModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
