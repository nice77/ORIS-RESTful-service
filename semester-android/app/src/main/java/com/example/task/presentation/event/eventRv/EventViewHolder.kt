package com.example.task.presentation.event.eventRv

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.util.DisplayMetrics
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.task.BuildConfig
import com.example.task.R
import com.example.task.databinding.ItemCurrentEventBinding
import com.example.task.presentation.event.imageRv.ImageViewPager
import com.example.task.utils.loadCaching

class EventViewHolder(
    private val binding: ItemCurrentEventBinding,
    private val onEventSubscribed: () -> Unit,
    private val onUserNameClicked: (Long) -> Unit,
    private val onEditBtnClicked: () -> Unit,
    private val onImageMapClicked: (Double, Double) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentAuthorName : String? = null
    private var currentAuthorId : Long? = null

    private var latitude : Double? = null
    private var longitude : Double? = null

    init {
        binding.joinBtn.setOnClickListener {
            onEventSubscribed()
            updateSubState()
        }
        binding.authorNameTv.setOnClickListener {
            currentAuthorId?.let(onUserNameClicked)
        }
        binding.editBtn.setOnClickListener {
            onEditBtnClicked()
        }
        binding.mapIv.setOnClickListener {
            if (latitude != null && longitude != null) {
                onImageMapClicked(latitude!!, longitude!!)
            }
        }
    }

    fun onBind(item : EventUiModel.Event) {
        currentAuthorId = item.authorId
        latitude = item.latitude
        longitude = item.longitude
        binding.run {
            imageVp.adapter = ImageViewPager(item.eventImages)
            titleTv.text = item.title
            subtitleTv.text = item.description.ifEmpty { binding.root.context.getString(R.string.no_description) }
            calendar.timeInMillis = item.date.time
            dateTv.text = dateFormat.format(calendar)

            var width = root.resources.displayMetrics.xdpi.toInt()
            var height = 270 * (root.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            width = if (width > 650) 650 else width
            height = if (height > 450) 450 else height
            val pointInfo = "${item.longitude},${item.latitude},org"
            mapIv.loadCaching("${BuildConfig.STATIC_MAP_URI}apikey=${BuildConfig.STATIC_MAP_KEY}&ll=${item.longitude},${item.latitude}&z=16&size=$width,$height&pt=$pointInfo")
        }
    }

    fun updateUserName(userName : String) {
        if (currentAuthorName == null) {
            currentAuthorName = userName
        }
        val text = "${binding.root.context.getString(R.string.author)} $userName"
        binding.authorNameTv.text = text
    }

    fun showEditButton(result: Boolean) {
        binding.editBtn.isVisible = result
    }

    private fun updateSubState() {
        val currText = binding.joinBtn.text
        val joinEventText = binding.root.context.getText(R.string.join_event)
        val unsubFromEvent = binding.root.context.getText(R.string.unsub_from_event)
        if (currText == joinEventText) {
            binding.joinBtn.text = unsubFromEvent
        } else {
            binding.joinBtn.text = joinEventText
        }
    }

    fun updateSubscribeButton(subbed : Boolean) {
        val joinEventText = binding.root.context.getText(R.string.join_event)
        val unsubFromEvent = binding.root.context.getText(R.string.unsub_from_event)
        binding.joinBtn.text = if (subbed) unsubFromEvent else joinEventText
    }

    companion object {
        private val calendar = Calendar.getInstance()
        private val dateFormat = DateFormat.getDateInstance()
    }
}