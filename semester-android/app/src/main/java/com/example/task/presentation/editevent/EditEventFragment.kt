package com.example.task.presentation.editevent

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
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
import com.example.task.databinding.FragmentEditEventBinding
import com.example.task.presentation.editevent.bottomsheet.EventImagesBottomSheetFragment
import com.example.task.presentation.map.MapFragment
import com.example.task.utils.component
import com.example.task.utils.lazyViewModel
import kotlinx.coroutines.launch
import java.util.Date

class EditEventFragment : Fragment(R.layout.fragment_edit_event) {

    private val binding by viewBinding(FragmentEditEventBinding::bind)
    private val viewModel by lazyViewModel {
        requireContext().component.editEventViewModel().create(arguments?.getLong(CURRENT_EVENT_KEY))
    }

    private var coords : Pair<Double, Double> = Pair(55.751527, 37.618835)
    private var images : List<String>? = null
    private val loadedImageUris : MutableList<Uri> = mutableListOf()
    private var removedImages : MutableList<String> = mutableListOf()
    private var selectedDate : Date = Date()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeLl.setOnClickListener {
            val bundle = Bundle().apply {
                putDouble(MapFragment.LATITUDE_KEY, coords.first)
                putDouble(MapFragment.LONGITUDE_KEY, coords.second)
                putBoolean(MapFragment.IS_EDITABLE_KEY, true)
            }
            findNavController().navigate(R.id.action_editEventFragment_to_mapFragment, bundle)
        }
        binding.imagesLl.setOnClickListener {
            EventImagesBottomSheetFragment.getInstance(images ?: listOf(), loadedImageUris).show(parentFragmentManager, TAG)
        }
        binding.dateLl.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                selectedDate = calendar.time
            }, year, month, day)
            datePicker.show()
        }
        binding.run {
            submitBtn.setOnClickListener {
                viewModel.onSubmitButtonClicked(
                    title = titleEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    latitude = coords.first,
                    longitude = coords.second,
                    imageNameList = removedImages.toList(),
                    uriList = loadedImageUris.toList(),
                    contentResolver = requireContext().contentResolver,
                    selectedDate = selectedDate
                )
                submitBtn.isEnabled = false
            }
        }
        setupResultListener()
        getEvent()
        observeData()
    }

    private fun setupResultListener() {
        setFragmentResultListener(
            requestKey = EventImagesBottomSheetFragment.FRAGMENT_RESULT_KEY
        ) { key, bundle ->
            if (key == EventImagesBottomSheetFragment.FRAGMENT_RESULT_KEY) {
                bundle.getStringArrayList(EventImagesBottomSheetFragment.LOADED_IMAGES_KEY)?.let {
                    loadedImageUris.addAll(it.map(Uri::parse))
                }
                bundle.getStringArrayList(EventImagesBottomSheetFragment.IMAGES_AFTER_REMOVAL_KEY)?.let {
                    removedImages.clear()
                    removedImages.addAll(it.toList())
                }
            }
        }

        setFragmentResultListener(
            requestKey = MapFragment.MAP_POINT_SELECT_KEY
        ) { key, bundle ->
            if (key == MapFragment.MAP_POINT_SELECT_KEY) {
                viewModel.submitCoords(Pair(bundle.getDouble(MapFragment.LATITUDE_KEY), bundle.getDouble(MapFragment.LONGITUDE_KEY)))
            }
        }
    }

    private fun getEvent() {
        viewModel.getEvent()
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.eventFlow.collect {
                        it?.let { event ->
                            binding.run {
                                titleEt.setText(event.title)
                                descriptionEt.setText(event.description)
                                coords = Pair(event.latitude, event.longitude)
                                images = event.eventImages
                                selectedDate = event.date
                            }
                        }
                    }
                }
                launch {
                    viewModel.submitFlow.collect {
                        if (it.first && it.second) {
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(IS_MODIFIED_KEY, Bundle().apply { putBoolean(IS_MODIFIED_KEY, true) })
                            findNavController().navigateUp()
                        }
                    }
                }
                launch {
                    viewModel.coordsFlow.collect {
                        it?.let {
                            coords = it
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val IS_MODIFIED_KEY = "IS_MODIFIED_KEY"
        const val CURRENT_EVENT_KEY = "CURRENT_EVENT_KEY"
        const val TAG = "TAG"
    }
}