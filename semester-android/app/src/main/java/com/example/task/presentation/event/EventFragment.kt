package com.example.task.presentation.event

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentEventBinding
import com.example.task.presentation.editevent.EditEventFragment
import com.example.task.presentation.event.eventRv.EventAdapter
import com.example.task.presentation.map.MapFragment
import com.example.task.presentation.profile.ProfileFragment
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import kotlinx.coroutines.launch

class EventFragment : Fragment(R.layout.fragment_event) {

    private val binding by viewBinding(FragmentEventBinding::bind)
    private val viewModel by lazyViewModel {
        requireContext().component.eventViewModel().create(arguments?.getLong(CURRENT_EVENT_KEY) ?: throw NoSuchElementException())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.eventRv.adapter = EventAdapter(
            getUser = ::getUser,
            isCurrentUser = ::isCurrentUser,
            onEventSubscribed = ::onEventSubscribed,
            amISubscribedToEvent = ::amISubscribedToEvent,
            sendComment = ::sendComment,
            onUserNameClicked = ::onUserNameClicked,
            onEditBtnClicked = ::onEditBtnClicked,
            onImageMapClicked = ::onImageMapClicked
        )
        observeData()
        findNavController().currentBackStackEntry?.savedStateHandle?.get<Bundle>(EditEventFragment.IS_MODIFIED_KEY)?.let {
            viewModel.reloadEventData()
            findNavController().currentBackStackEntry?.savedStateHandle?.set(EditEventFragment.IS_MODIFIED_KEY, null)
        }
    }

    private fun onImageMapClicked(latitude : Double, longitude : Double) {
        val bundle = Bundle().apply {
            putDouble(MapFragment.LATITUDE_KEY, latitude)
            putDouble(MapFragment.LONGITUDE_KEY, longitude)
            putBoolean(MapFragment.IS_EDITABLE_KEY, false)
        }
        findNavController().navigate(R.id.action_eventFragment_to_mapFragment, bundle)
    }

    private fun getUser(userId : Long) {
        viewModel.getUser(userId = userId)
    }

    private fun isCurrentUser(userId: Long) {
        viewModel.getIsCurrentUser(userId = userId)
    }

    private fun onEventSubscribed() {
        viewModel.subscribeToEvent()
    }

    private fun amISubscribedToEvent() {
        viewModel.amISubscribedToEvent()
    }

    private fun sendComment(text : String) {
        viewModel.sendComment(text = text)
    }

    private fun onUserNameClicked(userId: Long) {
        val bundle = Bundle().apply {
            putLong(ProfileFragment.USER_ID_KEY, userId)
        }
        findNavController().navigate(R.id.action_eventFragment_to_profileFragment, bundle)
    }

    private fun onEditBtnClicked() {
        val bundle = Bundle().apply {
            arguments?.let {
                putLong(CURRENT_EVENT_KEY, it.getLong(CURRENT_EVENT_KEY))
            }
        }
        findNavController().navigate(R.id.action_eventFragment_to_editEventFragment, bundle)
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.eventPageFlow.collect {
                        (binding.eventRv.adapter as EventAdapter).submitData(it)
                    }
                }
                launch {
                    viewModel.userFlow.collect {
                        it?.name?.let((binding.eventRv.adapter as EventAdapter)::updateUserName)
                    }
                }
                launch {
                    viewModel.requestCurrentUserFlow.collect { result ->
                        result?.let((binding.eventRv.adapter as EventAdapter)::showEditButton)
                    }
                }
                launch {
                    viewModel.requestSubscriptionFlow.collect { result ->
                        result?.let((binding.eventRv.adapter as EventAdapter)::updateSubscribeButton)
                    }
                }
                launch {
                    viewModel.commentFlow.collect {
                        it?.let { comment ->
                            val adapter = (binding.eventRv.adapter as EventAdapter)
                            val currItems = adapter.snapshot().items.toMutableList()
                            currItems.add(2, comment)
                            adapter.submitData(PagingData.from(currItems))
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val CURRENT_EVENT_KEY = "CURRENT_EVENT_KEY"
    }
}