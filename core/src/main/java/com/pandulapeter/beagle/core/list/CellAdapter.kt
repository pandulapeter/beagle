package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolderDelegate

internal class CellAdapter : ListAdapter<Cell<out Cell<*>>, ViewHolderDelegate.ViewHolder<out Cell<*>>>(object : DiffUtil.ItemCallback<Cell<*>>() {

    override fun areItemsTheSame(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = oldItem == newItem

    override fun getChangePayload(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = ""
}) {
    private val delegates = mutableListOf<ViewHolderDelegate<*>>()

    override fun getItemViewType(position: Int): Int {
        val cell = getItem(position)
        val type = cell::class
        return delegates.indexOfFirst { type == it.cellType }.let { index ->
            if (index == -1) {
                delegates.add(cell.createViewHolderDelegate())
                delegates.lastIndex
            } else index
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegates[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolderDelegate.ViewHolder<out Cell<*>>, position: Int) = holder.forceBind(getItem(position))
}