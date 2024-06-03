package com.example.task.presentation.search.searchRv

import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ItemSearchBarBinding

class SearchBarViewHolder(
    private val binding : ItemSearchBarBinding,
    private val onTextUpdate: (String) -> Unit,
    private val onFilterButtonPressed: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            searchEt.addTextChangedListener { text ->
                onTextUpdate(text.toString())
            }
            filterIv.setOnClickListener {
                onFilterButtonPressed()
            }
        }
    }

    fun onBind(item : SearchUiModel.SearchBar) {
        binding.searchEt.setText(item.query)
        val currentText = binding.searchEt.text.toString()
        binding.searchEt.setSelection(if (currentText.isEmpty()) 0 else currentText.length)
    }

}