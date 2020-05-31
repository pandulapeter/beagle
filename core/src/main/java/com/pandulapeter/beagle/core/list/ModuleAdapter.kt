package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.ViewHolderDelegate

internal class ModuleAdapter : ListAdapter<ModuleCell, ViewHolderDelegate.ViewHolder<out ModuleCell>>(object : DiffUtil.ItemCallback<ModuleCell>() {

    override fun areItemsTheSame(oldItem: ModuleCell, newItem: ModuleCell) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ModuleCell, newItem: ModuleCell) = oldItem == newItem

    override fun getChangePayload(oldItem: ModuleCell, newItem: ModuleCell) = ""
}) {
    private val cellHandlers = mutableListOf<ViewHolderDelegate<*>>()

    override fun getItemViewType(position: Int): Int {
        val cell = getItem(position)
        val type = cell::class
        return cellHandlers.indexOfFirst { type == it.cellType }.let { index ->
            if (index == -1) {
                cellHandlers.add(cell.createViewHolderDelegate())
                cellHandlers.lastIndex
            } else index
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = cellHandlers[viewType].createViewHolder(parent) as ViewHolderDelegate.ViewHolder<ModuleCell>

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolderDelegate.ViewHolder<out ModuleCell>, position: Int) = (holder as ViewHolderDelegate.ViewHolder<ModuleCell>).bind(getItem(position))
}