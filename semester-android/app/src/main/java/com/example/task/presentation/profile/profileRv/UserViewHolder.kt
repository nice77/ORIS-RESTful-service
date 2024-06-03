package com.example.task.presentation.profile.profileRv

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.R
import com.example.task.databinding.ItemUserCardBinding
import com.example.task.utils.loadCaching

class UserViewHolder(
    private val binding : ItemUserCardBinding,
    private val onEditButtonPressed: () -> Unit,
    private val manageSubscriptionToUser: () -> Unit,
    private val onCreateNewClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.editIv.setOnClickListener {
            onEditButtonPressed()
        }
        binding.subsribeBtn.setOnClickListener {
            manageSubscriptionToUser()
        }
    }

    fun onBind(uiModel: ProfileUIModel.User) {
        binding.run {
            avatarIv.loadCaching(BuildConfig.PATH + uiModel.userImage)
            userGmailTv.text = uiModel.email
            userNameTv.text = uiModel.name
            authorsCountTv.text = uiModel.authorsCount.toString()
            followersCountTv.text = uiModel.subscribersCount.toString()
            editIv.isVisible = uiModel.isCurrentUser
            createNewEventBtn.isVisible = uiModel.isCurrentUser
            subsribeBtn.isVisible = !uiModel.isCurrentUser
            createNewEventBtn.setOnClickListener {
                onCreateNewClicked()
            }

            uiModel.isSubscribed?.let {
                val subscribedText = binding.root.context.getText(R.string.subscribe)
                val removeText = binding.root.context.getText(R.string.unsub_from_user)
                binding.subsribeBtn.text = if (it) removeText else subscribedText
            }
        }
    }

}