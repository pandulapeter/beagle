package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayDeque
import kotlin.reflect.KClass

internal class CellAdapter : RecyclerView.Adapter<ViewHolder<out Cell<*>>>() {

    private val delegates = mutableMapOf<KClass<*>, ViewHolder.Delegate<*>>()
    private var items = emptyList<Cell<*>>()
    private var job: Job? = null
    private val pendingUpdates = ArrayDeque<List<Cell<*>>>()

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val cell = items[position]
        return delegates.keys.indexOf(cell::class).let { index ->
            if (index == -1) {
                delegates[cell::class] = cell.createViewHolderDelegate()
                delegates.size - 1
            } else index
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> = delegates.values.toList()[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder<out Cell<*>>, position: Int) = holder.forceBind(items[position])

    suspend fun submitList(newItems: List<Cell<*>>, onListUpdated: (() -> Unit)? = null) {
        withContext(Dispatchers.Default) {
            synchronized(pendingUpdates) {
                pendingUpdates.add(newItems)
                if (pendingUpdates.size == 1) {
                    update(newItems, onListUpdated)
                }
            }
        }
    }

    private fun update(newItems: List<Cell<*>>, onListUpdated: (() -> Unit)? = null) {
        job?.cancel()
        job = GlobalScope.launch(Dispatchers.Default) {
            val result = DiffUtil.calculateDiff(DiffCallback(items, newItems))
            job = launch(Dispatchers.Main) {
                items = newItems
                result.dispatchUpdatesTo(this@CellAdapter)
                launch(Dispatchers.Default) {
                    processQueue(onListUpdated)
                    job = null
                }
            }
        }
    }

    private fun processQueue(onListUpdated: (() -> Unit)?) = synchronized(pendingUpdates) {
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

    private class DiffCallback(private val oldItems: List<Cell<*>>, private val newItems: List<Cell<*>>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = Unit
    }
}