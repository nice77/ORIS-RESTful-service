package com.example.task.presentation.event.eventRv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemCommentBinding
import com.example.task.databinding.ItemCommentInputFieldBinding
import com.example.task.databinding.ItemCurrentEventBinding
import com.example.task.domain.models.EventDomainModel

class EventAdapter(
    private val getUser: (Long) -> Unit,
    private val isCurrentUser: (Long) -> Unit,
    private val onEventSubscribed: () -> Unit,
    private val amISubscribedToEvent: () -> Unit,
    private val sendComment: (String) -> Unit,
    private val onUserNameClicked: (Long) -> Unit,
    private val onEditBtnClicked: () -> Unit,
    private val onImageMapClicked: (Double, Double) -> Unit
) : PagingDataAdapter<EventUiModel, RecyclerView.ViewHolder>(ITEM_DIFF) {

    private var eventViewHolder : EventViewHolder? = null
    private var isEventAuthorNameLoaded : Boolean = false
    private var eventData : EventUiModel.Event? = null
    private val dataMap = HashMap<String, Any>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is EventUiModel.Event -> {
                (holder as EventViewHolder).onBind(item)
                eventData = item
                if (!isEventAuthorNameLoaded) {
                    getUser(eventData!!.authorId)
                    isCurrentUser(eventData!!.authorId)
                    amISubscribedToEvent()
                    dataMap.clear()
                }
            }
            is EventUiModel.CommentInputField -> Unit
            is EventUiModel.Comment -> (holder as CommentViewHolder).onBind(item)
            else -> throw NoSuchElementException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_current_event -> {
                eventViewHolder = EventViewHolder(
                    binding = ItemCurrentEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onEventSubscribed = onEventSubscribed,
                    onUserNameClicked = onUserNameClicked,
                    onEditBtnClicked = onEditBtnClicked,
                    onImageMapClicked = onImageMapClicked
                )
                eventViewHolder?.let {
                    if (dataMap.isNotEmpty()) {
                        it.updateUserName(dataMap[USERNAME_KEY] as String)
                        it.showEditButton(dataMap[SHOW_EDIT_BTN_KEY] as Boolean)
                        it.updateSubscribeButton(dataMap[UPDATE_SUB_BTN_KEY] as Boolean)
                    }
                }
                eventViewHolder!!
            }
            R.layout.item_comment_input_field -> InputFieldViewHolder(
                binding = ItemCommentInputFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                sendComment = sendComment
            )
            R.layout.item_comment -> CommentViewHolder(
                binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw NoSuchElementException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_current_event
            1 -> R.layout.item_comment_input_field
            else -> R.layout.item_comment
        }
    }

    fun updateUserName(userName : String) {
        isEventAuthorNameLoaded = true
        eventViewHolder?.updateUserName(userName = userName)
        if (eventViewHolder == null) {
            dataMap[USERNAME_KEY] = userName
        }
    }

    fun showEditButton(result : Boolean) {
        isEventAuthorNameLoaded = true
        eventViewHolder?.showEditButton(result)
        if (eventViewHolder == null) {
            dataMap[SHOW_EDIT_BTN_KEY] = result
        }
    }

    fun updateSubscribeButton(subbed : Boolean) {
        isEventAuthorNameLoaded = true
        eventViewHolder?.updateSubscribeButton(subbed)
        if (eventViewHolder == null) {
            dataMap[UPDATE_SUB_BTN_KEY] = subbed
        }
    }

    companion object {

        const val USERNAME_KEY = "USERNAME_KEY"
        const val SHOW_EDIT_BTN_KEY = "SHOW_EDIT_BTN_KEY"
        const val UPDATE_SUB_BTN_KEY = "UPDATE_SUB_BTN_KEY"

        private val ITEM_DIFF = object : DiffUtil.ItemCallback<EventUiModel>() {
            override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean {
                return when {
                    oldItem is EventUiModel.Event && newItem is EventUiModel.Event -> oldItem.id == newItem.id
                    oldItem is EventUiModel.Comment && newItem is EventUiModel.Comment -> oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}