package com.example.task.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStates
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentProfileBinding
import com.example.task.presentation.event.EventFragment
import com.example.task.presentation.profile.profileRv.ProfileAdapter
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel : ProfileViewModel by lazyViewModel {
        val userId = arguments?.getLong(USER_ID_KEY)
        requireContext().component.profileViewModel().create(userId)
    }
    private val binding : FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = findNavController().currentBackStackEntry?.savedStateHandle?.get<Bundle>(IS_MODIFIED_KEY)
        bundle?.let {
            viewModel.reloadProfileData()
            findNavController().currentBackStackEntry?.savedStateHandle?.set(IS_MODIFIED_KEY, null)
        }
        binding.root.setOnRefreshListener {
            viewModel.reloadProfileData()
        }
        binding.profileRv.adapter = ProfileAdapter(
            onRadioButtonChecked = ::onRadioButtonChecked,
            onEditButtonPressed = ::onEditButtonPressed,
            onEventItemPressed = ::onEventClicked,
            manageSubscriptionToUser = ::manageSubscriptionToUser,
            onCreateNewClicked = ::onCreateNewClicked
        )
        binding.profileRv.overScrollMode
        observeData()
    }

    private fun onCreateNewClicked() {
        findNavController().navigate(R.id.action_profileFragment_to_editEventFragment)
    }

    private fun onEditButtonPressed() {
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    private fun onRadioButtonChecked(radioButtonId : Int) {
        viewModel.checkNewItem(checkedItemId = radioButtonId)
    }

    private fun onEventClicked(eventId : Long) {
        val bundle = Bundle().apply {
            putLong(EventFragment.CURRENT_EVENT_KEY, eventId)
        }
        findNavController().navigate(R.id.action_profileFragment_to_eventFragment, bundle)
    }

    private fun manageSubscriptionToUser() {
        viewModel.manageSubscriptionToUser()
        viewModel.reloadProfileData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.eventList.collect {
                        (binding.profileRv.adapter as ProfileAdapter).submitData(it)
                        binding.root.isRefreshing = false
                    }
                }
            }
        }
    }

    companion object {
        const val USER_ID_KEY = "USER_ID_KEY"
        const val IS_MODIFIED_KEY = "IS_MODIFIED_KEY"
    }
}