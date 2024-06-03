package com.example.task.presentation.editevent.bottomsheet

import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemButtonBinding

class ButtonViewHolder(
    private val binding: ItemButtonBinding,
    private val onAddButtonCLicked: () -> Unit,
    private val onSubmitButtonClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var isLast : Boolean = false

    init {
        binding.btn.setOnClickListener {
            if (!isLast) {
                onAddButtonCLicked()
            } else {
                onSubmitButtonClicked()
            }
        }
    }

    fun onBind(isLast : Boolean) {
        this.isLast = isLast
        binding.btn.text = binding.root.context.getString(if (isLast) R.string.submit else R.string.add_image)
    }

}