package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayDeque


open class BaseAdapter<T : ListItem>(private val scope: CoroutineScope) : RecyclerView.Adapter<BaseViewHolder<*, *>>() {

    private var items = emptyList<T>()
    private var job: Job? = null
    private val pendingUpdates = ArrayDeque<List<T>>()

    final override fun getItemCount() = items.size

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    @CallSuper
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TextViewHolder.UiModel -> R.layout.item_text
        is CodeSnippetViewHolder.UiModel -> R.layout.item_code_snippet
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_text -> TextViewHolder.create(parent)
        R.layout.item_code_snippet -> CodeSnippetViewHolder.create(parent)
        else -> throw  IllegalArgumentException("Unsupported item view type: $viewType.")
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CodeSnippetViewHolder -> holder.bind(getItem(position) as CodeSnippetViewHolder.UiModel)
        is TextViewHolder -> holder.bind(getItem(position) as TextViewHolder.UiModel)
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }

    fun getItem(position: Int) = items[position]

    fun submitList(newItems: List<T>, onListUpdated: (() -> Unit)? = null) {
        pendingUpdates.add(newItems)
        if (pendingUpdates.size == 1) {
            update(newItems, onListUpdated)
        }
    }

    private fun update(newItems: List<T>, onListUpdated: (() -> Unit)?) {
        job?.cancel()
        job = scope.launch(Dispatchers.IO) {
            val result = DiffUtil.calculateDiff(DiffCallback(items, newItems))
            job = launch(Dispatchers.Main) {
                items = newItems
                result.dispatchUpdatesTo(this@BaseAdapter)
                processQueue(onListUpdated)
                job = null
            }
        }
    }

    private fun processQueue(onListUpdated: (() -> Unit)?) {
        pendingUpdates.remove()
        if (pendingUpdates.isEmpty()) {
            onListUpdated?.invoke()
        } else {
            if (pendingUpdates.size > 1) {
                val lastList = pendingUpdates.peekLast()
                pendingUpdates.clear()
                pendingUpdates.add(lastList)
            }
            pendingUpdates.peek()?.let { update(it, onListUpdated) }
        }
    }

    private class DiffCallback<T : ListItem>(private val oldItems: List<T>, private val newItems: List<T>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Unit
    }
}