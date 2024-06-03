package com.example.task.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentSearchBinding
import com.example.task.presentation.event.EventFragment
import com.example.task.presentation.profile.ProfileFragment
import com.example.task.presentation.search.searchRv.SearchAdapter
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel : SearchViewModel by lazyViewModel {
        requireContext().component.searchViewModel().create()
    }
    private var checkedId = R.id.option_event_rb

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResultListener()
        checkedId = viewModel.configFlow.value.checkedId
        binding.searchRv.adapter = SearchAdapter(
            onTextUpdate = ::onTextUpdate,
            onFilterButtonPressed = ::openFilterBottomFragment,
            onEventItemPressed = ::onEventItemPressed,
            onUserItemPressed = ::onUserItemPressed
        )
        observeData()
    }

    private fun setupResultListener() {
        setFragmentResultListener(
            FilterBottomFragment.CHECKED_ID_INT
        ) { key, bundle ->
            if (key == FilterBottomFragment.CHECKED_ID_INT) {
                checkedId = bundle.getInt(FilterBottomFragment.CHECKED_ID_INT)
                onItemCheck(checkedId)
            }
        }
    }

    private fun onTextUpdate(query : String) {
        val searchConfig = SearchConfig(
            checkedId = checkedId,
            query = query
        )
        viewModel.setupNewSearchConfig(searchConfig)
    }

    private fun openFilterBottomFragment() {
        val dialog = FilterBottomFragment.getInstance(checkedId)
        dialog.show(parentFragmentManager, FilterBottomFragment.FILTER_BOTTOM_FRAGMENT_TAG)
    }

    private fun onItemCheck(newCheckedId : Int) {
        val searchConfig = SearchConfig(
            checkedId = newCheckedId,
            query = null
        )
        viewModel.setupNewSearchConfig(searchConfig)
    }

    private fun onEventItemPressed(eventId : Long) {
        val bundle = Bundle().apply {
            putLong(EventFragment.CURRENT_EVENT_KEY, eventId)
        }
        findNavController().navigate(R.id.action_searchFragment_to_eventFragment, bundle)
    }

    private fun onUserItemPressed(userId : Long) {
        val bundle = Bundle().apply {
            putLong(ProfileFragment.USER_ID_KEY, userId)
        }
        findNavController().navigate(R.id.action_searchFragment_to_profileFragment, bundle)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.configFlow.collectLatest {
                        checkedId = it.checkedId
                    }
                }
                viewModel.listItems.collectLatest {
                    (binding.searchRv.adapter as SearchAdapter).submitData(it)
                }
            }
        }
    }
}