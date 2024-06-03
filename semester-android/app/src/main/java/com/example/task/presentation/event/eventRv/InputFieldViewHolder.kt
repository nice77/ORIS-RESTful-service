package com.example.task.presentation.event.eventRv

import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemCommentInputFieldBinding

class InputFieldViewHolder(
    private val binding: ItemCommentInputFieldBinding,
    private val sendComment: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.submitIv.setOnClickListener {
            sendComment(binding.commentEt.text.toString())
            binding.commentEt.setText("")
        }
    }

}