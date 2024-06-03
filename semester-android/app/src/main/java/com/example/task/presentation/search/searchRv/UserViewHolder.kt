package com.example.task.presentation.search.searchRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.databinding.ItemHorizontalUserBinding
import com.example.task.utils.loadCaching

class UserViewHolder(
    private val binding : ItemHorizontalUserBinding,
    private val onUserItemPressed: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentUserId : Long? = null

    init {
        binding.root.setOnClickListener {
            currentUserId?.let(onUserItemPressed)
        }
    }

    fun onBind(item : SearchUiModel.User) {
        currentUserId = item.id
        binding.run {
            userImageIv.loadCaching(BuildConfig.PATH + item.userImage)
            nameTv.text = item.name
        }
    }

}