package com.example.task.presentation.editevent.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.ItemTouchHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.task.R
import com.example.task.databinding.FragmentEventImagesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EventImagesBottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_event_images) {

    private val binding by viewBinding(FragmentEventImagesBinding::bind)

    var sizeLeft = arguments?.getInt(IMAGE_SIZE_KEY) ?: 0
    private val loadedImages : MutableList<Uri> = mutableListOf()
    private val removedImages : MutableList<String> = mutableListOf()

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10 - sizeLeft)) {
        loadedImages.addAll(it)
        val adapter = (binding.imagesRv.adapter as BottomSheetImagesAdapter)
        val newList : List<BottomUiModel> = adapter.currentList.subList(0, adapter.itemCount - 2) + it.map { BottomUiModel.Image(path = it.toString(), isUri = true) } + adapter.currentList.subList(adapter.itemCount - 2, adapter.itemCount)
        adapter.submitList(newList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val list = it.getStringArrayList(CURRENT_IMAGES_KEY) ?: throw NoSuchElementException()
            val loadedImagesStringList = it.getStringArrayList(LOADED_IMAGES_KEY)?.toMutableList() ?: mutableListOf()

            val combinedList : MutableList<BottomUiModel> = list.map {
                BottomUiModel.Image(path = it, isUri = false)
            }.toMutableList()
            combinedList.addAll(loadedImagesStringList.map {
                BottomUiModel.Image(path = it, isUri = true)
            })
            repeat(2) {
                combinedList.add(BottomUiModel.Button)
            }
            binding.imagesRv.adapter = BottomSheetImagesAdapter(
                onAddButtonClicked = ::onAddButtonClicked,
                onSubmitButtonClicked = ::onSubmitButtonClicked,
                onItemSwiped = ::onItemSwiped
            )
            (binding.imagesRv.adapter as BottomSheetImagesAdapter).submitList(combinedList.toList())
            val callback = ItemSwipeHelper()
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.imagesRv)
        }
    }

    private fun onItemSwiped(image : BottomUiModel.Image) {
        val adapter = (binding.imagesRv.adapter as BottomSheetImagesAdapter)
        adapter.submitList(adapter.currentList.filter { if (it is BottomUiModel.Image && it.isUri == image.isUri) it.path != image.path else true })
        if (image.isUri) {
            val uri = Uri.parse(image.path)
            loadedImages.filter { path -> path != uri }
        } else {
            removedImages.add(image.path)
        }
    }

    private fun onAddButtonClicked() {
        imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onSubmitButtonClicked() {
        setFragmentResult(
            requestKey = FRAGMENT_RESULT_KEY,
            result = Bundle().apply {
                putStringArrayList(IMAGES_AFTER_REMOVAL_KEY, ArrayList(removedImages))
                putStringArrayList(LOADED_IMAGES_KEY, ArrayList(loadedImages.map {
                    it.toString()
                }))
            }
        )
        dismiss()
    }

    fun reduceSizeLeft() {
        sizeLeft--
    }

    companion object {
        const val CURRENT_IMAGES_KEY = "STRING_ARRAY_KEY"
        const val IMAGE_SIZE_KEY = "IMAGE_SIZE_KEY"
        const val FRAGMENT_RESULT_KEY = "FRAGMENT_RESULT_KEY"
        const val LOADED_IMAGES_KEY = "ARRAY_LIST_KEY"
        const val IMAGES_AFTER_REMOVAL_KEY = "IMAGES_AFTER_REMOVAL_KEY"

        fun getInstance(images : List<String>, imagesUri : List<Uri>) : EventImagesBottomSheetFragment {
            val bundle = Bundle().apply {
                putStringArrayList(CURRENT_IMAGES_KEY, ArrayList(images))
                putStringArrayList(LOADED_IMAGES_KEY, ArrayList(imagesUri.map { it.toString() }))
            }
            return EventImagesBottomSheetFragment().apply {
                arguments = bundle
            }
        }
    }
}