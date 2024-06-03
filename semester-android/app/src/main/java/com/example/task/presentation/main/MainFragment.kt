package com.example.task.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentMainBinding
import com.example.task.presentation.event.EventFragment
import com.example.task.presentation.main.mainRv.MainAdapter
import com.example.task.presentation.profile.ProfileFragment
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by lazyViewModel {
        requireContext().component.mainViewModel().create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainRv.adapter = MainAdapter(viewModel, viewLifecycleOwner, ::onEventItemPressed, ::onUserItemClicked)
        binding.root.setOnRefreshListener {
            viewModel.reloadData()
        }
        observeData()
    }

    private fun onEventItemPressed(eventId : Long) {
        val bundle = Bundle().apply {
            putLong(EventFragment.CURRENT_EVENT_KEY, eventId)
        }
        findNavController().navigate(R.id.action_mainFragment_to_eventFragment, bundle)
    }

    private fun onUserItemClicked(userId : Long) {
        val bundle = Bundle().apply {
            putLong(ProfileFragment.USER_ID_KEY, userId)
        }
        findNavController().navigate(R.id.action_mainFragment_to_profileFragment, bundle)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.eventsFlow.collectLatest {
                    (binding.mainRv.adapter as MainAdapter).submitData(it)
                    binding.root.isRefreshing = false
                }
            }
        }
    }
}
