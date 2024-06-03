package com.example.task.presentation.themechoose

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentThemeChooseBinding
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel

class ThemeChooseFragment : Fragment(R.layout.fragment_theme_choose) {

    private val binding by viewBinding(FragmentThemeChooseBinding::bind)
    private val viewModel by lazyViewModel {
        requireContext().component.themeChooseViewModel().create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitBtn.setOnClickListener {
            viewModel.setCurrentDestination(R.id.selectFragment)
            AppCompatDelegate.setDefaultNightMode(
                when (binding.themeRg.checkedRadioButtonId) {
                    R.id.theme_light_rb -> AppCompatDelegate.MODE_NIGHT_NO
                    R.id.theme_dark_rb -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> throw RuntimeException()
                }
            )
            activity?.recreate()
            findNavController().navigate(R.id.action_themeChooseFragment_to_selectFragment)
        }
    }

}