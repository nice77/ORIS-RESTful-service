package com.example.task.presentation.select

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentSelectBinding
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel

class SelectFragment : Fragment(R.layout.fragment_select) {

    private val binding : FragmentSelectBinding by viewBinding(FragmentSelectBinding::bind)
    private val viewModel by lazyViewModel {
        requireContext().component.selectViewModel().create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            binding.signInBtn.setOnClickListener {
                viewModel.setFirstRun()
                findNavController().navigate(R.id.action_welcomeFragment_to_authFragment)
            }
            binding.registerBtn.setOnClickListener {
                viewModel.setFirstRun()
                findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
            }
        }
    }
}