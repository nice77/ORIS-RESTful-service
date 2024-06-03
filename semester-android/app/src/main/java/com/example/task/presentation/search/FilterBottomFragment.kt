package com.example.task.presentation.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentFilterBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomFragment : BottomSheetDialogFragment(R.layout.fragment_filter_bottom) {

    private val binding by viewBinding(FragmentFilterBottomBinding::bind)
    @IdRes private var checkedId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            checkedId = it.getInt(CHECKED_ID_INT)
        }
        binding.radioGroup.check(checkedId)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            setFragmentResult(
                requestKey = CHECKED_ID_INT,
                result = Bundle().apply {
                    putInt(CHECKED_ID_INT, i)
                }
            )
            dismiss()
        }
    }

    companion object {
        const val FILTER_BOTTOM_FRAGMENT_TAG = "FILTER_BOTTOM_FRAGMENT"
        const val CHECKED_ID_INT = "CHECKED_ID_INT"
        fun getInstance(
            @IdRes checkedId : Int
        ) : FilterBottomFragment {
            return FilterBottomFragment().apply {
                val bundle = Bundle().apply {
                    putInt(CHECKED_ID_INT, checkedId)
                }
                arguments = bundle
            }
        }
    }
}