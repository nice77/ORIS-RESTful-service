package com.example.task.presentation.main.mainRv

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemUsersRecyclerBinding
import com.example.task.presentation.main.MainViewModel
import com.example.task.presentation.main.usersRv.UsersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemUsersViewHolder(
    private val binding: ItemUsersRecyclerBinding,
    private val viewModel: MainViewModel,
    viewLifecycleOwner: LifecycleOwner,
    private val onUserItemClicked: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.usersRv.adapter = UsersAdapter(
            onUserItemClicked = onUserItemClicked
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usersFlow.collectLatest { usersList ->
                (binding.usersRv.adapter as UsersAdapter).submitData(usersList)
            }
        }
    }
}
