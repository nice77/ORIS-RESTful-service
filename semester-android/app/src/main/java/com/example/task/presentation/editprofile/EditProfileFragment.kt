package com.example.task.presentation.editprofile

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.BuildConfig
import com.example.task.R
import com.example.task.databinding.FragmentEditProfileBinding
import com.example.task.presentation.profile.ProfileFragment
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import com.example.task.utils.loadCaching
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val viewModel : EditProfileViewModel by lazyViewModel {
        requireContext().component.editProfileViewModel().create()
    }
    private val binding by viewBinding(FragmentEditProfileBinding::bind)

    private val launcher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let { uri ->
            viewModel.updateUserImage(requireContext().contentResolver.openInputStream(uri))
            binding.avatarIv.loadCaching(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        viewModel.getUserById()
        binding.run {
            avatarIv.setOnClickListener {
                onImageClicked()
            }
            submitBtn.setOnClickListener {
                onSubmitButtonClicked()
            }
        }
    }

    private fun onImageClicked() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onSubmitButtonClicked() {
        binding.submitBtn.isEnabled = false
        viewModel.onSubmitButtonClicked(
            name = binding.nameEt.text.toString(),
            age = binding.ageEt.text.toString().toInt(),
            city = binding.cityEt.text.toString()
        )
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.submitFlow.collect {
                        if (it) {
                            val bundle = Bundle()
                            bundle.putBoolean(ProfileFragment.IS_MODIFIED_KEY, true)
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(ProfileFragment.IS_MODIFIED_KEY, bundle)
                            findNavController().navigateUp()
                        } else {
                            binding.submitBtn.isEnabled = true
                        }
                    }
                }
                launch {
                    viewModel.userFlow.collect {
                        it?.let { userDomainModel ->
                            binding.run {
                                avatarIv.loadCaching(BuildConfig.PATH + userDomainModel.userImage)
                                nameEt.setText(userDomainModel.name)
                                ageEt.setText(userDomainModel.age.toString())
                                cityEt.setText(userDomainModel.city)
                                submitBtn.isEnabled = true
                            }
                        }
                    }
                }
            }
        }
    }
}