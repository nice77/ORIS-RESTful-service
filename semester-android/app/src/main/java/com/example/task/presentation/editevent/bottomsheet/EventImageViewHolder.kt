package com.example.task.presentation.editevent.bottomsheet

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.databinding.ItemBottomSheetImageBinding
import com.example.task.utils.loadCaching

class EventImageViewHolder(
    private val binding : ItemBottomSheetImageBinding,
    private val onItemSwiped: (BottomUiModel.Image) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var path : BottomUiModel.Image? = null

    fun onBind(image : BottomUiModel.Image) {
        path = image
        binding.eventImg.loadCaching(if (!image.isUri) BuildConfig.PATH + image.path else Uri.parse(image.path))
    }

    fun remove() {
        path?.let(onItemSwiped)
    }
}