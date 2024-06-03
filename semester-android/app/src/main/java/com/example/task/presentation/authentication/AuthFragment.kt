package com.example.task.presentation.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentAuthBinding
import com.example.task.domain.models.errorEnum.AuthErrorEnum
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding : FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)

    private val viewModel : AuthViewModel by lazyViewModel {
        requireContext().component.authViewModel().create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        binding.run {
            registerTv.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment_to_registerFragment)
            }
            submitBtn.setOnClickListener {
                it.isEnabled = false
                viewModel.authenticateUser(
                    email = binding.emailEt.text.toString(),
                    password = binding.passwordEt.text.toString()
                )
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                with (viewModel) {
                    launch {
                        errorFlow.collect { error ->
                            when (error) {
                                AuthErrorEnum.UNKNOWN_HOST -> showSnackbar(getString(R.string.unknown_host))
                                AuthErrorEnum.WRONG_CREDENTIALS -> showSnackbar(getString(R.string.wrong_cretentials))
                            }
                            binding.submitBtn.isEnabled = true
                        }
                    }
                    launch {
                        submitFlow.collect { result ->
                            if (result) {
                                findNavController().navigate(R.id.action_authFragment_to_holderFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSnackbar(message : String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}
