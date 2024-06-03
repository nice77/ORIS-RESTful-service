package com.example.task.presentation.main.usersRv

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.task.BuildConfig
import com.example.task.databinding.ItemUserBinding
import com.example.task.domain.models.UserDomainModel
import com.example.task.utils.loadCaching

class UserViewHolder(
    private val binding : ItemUserBinding,
    private val onUserItemClicked: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentUserId : Long? = null

    init {
        binding.root.setOnClickListener {
            currentUserId?.let(onUserItemClicked)
        }
    }

    fun onBind(userDomainModel: UserDomainModel) {
        currentUserId = userDomainModel.id
        binding.run {
            if (userDomainModel.userImage.isNotEmpty()) {
                userImg.loadCaching("${BuildConfig.PATH}${userDomainModel.userImage}")
            }
            usernameTv.text = userDomainModel.name
        }
    }
}
