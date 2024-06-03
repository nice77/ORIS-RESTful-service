package com.example.task.presentation.registration

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentRegisterBinding
import com.example.task.domain.models.errorEnum.RegisterErrorEnum
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding : FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel : RegisterViewModel by lazyViewModel {
        requireContext().component.registerViewModel().create()
    }

    private var emailCorrect = false
    private var passwordCorrect = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()

        binding.run {
            emailEt.addTextChangedListener { text ->
                emailCorrect = viewModel.validateEmail(text.toString())
                changeOutline(emailEt, emailCorrect)
                submitBtn.isEnabled = emailCorrect && passwordCorrect
            }

            passwordEt.addTextChangedListener { text ->
                passwordCorrect = viewModel.validatePassword(text.toString())
                changeOutline(passwordEt, passwordCorrect)
                submitBtn.isEnabled = emailCorrect && passwordCorrect
            }

            submitBtn.setOnClickListener {
                it.isEnabled = false
                viewModel.registerUser(
                    name = binding.nameEt.text.toString(),
                    email = binding.emailEt.text.toString(),
                    password = binding.passwordEt.text.toString()
                )
            }

            signInTv.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_authFragment)
            }
        }
    }

    private fun changeOutline(editText : EditText, isValid : Boolean) {
        val pixels = (requireContext().resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT).roundToInt()
        val background = editText.background as GradientDrawable

        if (!isValid) {
            background.setStroke(pixels, requireContext().getColor(R.color.red))
            return
        }
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.strokeColor, typedValue, true)
        background.setStroke(pixels, typedValue.data)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                with (viewModel) {
                    launch {
                        errorFlow.collect { error ->
                            when (error) {
                                RegisterErrorEnum.UNKNOWN_HOST -> showSnackbar(getString(R.string.unknown_host))
                                RegisterErrorEnum.EMAIL_IN_USE -> showSnackbar(getString(R.string.email_in_use))
                            }
                            binding.submitBtn.isEnabled = true
                        }
                    }
                    submitFlow.collect { result ->
                        findNavController().navigate(R.id.action_registerFragment_to_holderFragment)
                    }
                }
            }
        }
    }

    private fun showSnackbar(message : String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}