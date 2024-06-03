package com.example.task.presentation.event.eventRv

import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.databinding.ItemCommentBinding
import com.example.task.utils.loadCaching

class CommentViewHolder(
    private val binding : ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item : EventUiModel.Comment) {
        binding.run {
            nameTv.text = item.userName
            userImageIv.loadCaching(BuildConfig.PATH + item.userAvatar)
            commentTv.text = item.text
            calendar.timeInMillis = item.date.time
            dateTv.text = dateFormat.format(calendar)
        }
    }

    companion object {
        private val calendar = Calendar.getInstance()
        private val dateFormat = DateFormat.getDateInstance()
    }
}