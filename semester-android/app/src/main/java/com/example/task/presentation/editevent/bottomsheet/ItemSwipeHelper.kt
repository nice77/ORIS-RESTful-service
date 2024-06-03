package com.example.task.presentation.editevent.bottomsheet

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemSwipeHelper : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is EventImageViewHolder) {
            val dragFlags = 0
            val swipeFlags = ItemTouchHelper.START
            return makeMovementFlags(dragFlags, swipeFlags)
        }
        return makeMovementFlags(0, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is EventImageViewHolder) {
            if (direction == ItemTouchHelper.START) {
                viewHolder.remove()
            }
        }
    }

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true
}