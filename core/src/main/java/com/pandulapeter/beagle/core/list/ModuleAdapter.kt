package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.ViewHolderDelegate

internal class ModuleAdapter : ListAdapter<ModuleCell<out ModuleCell<*>>, ViewHolderDelegate.ViewHolder<out ModuleCell<*>>>(object : DiffUtil.ItemCallback<ModuleCell<*>>() {

    override fun areItemsTheSame(oldItem: ModuleCell<out ModuleCell<*>>, newItem: ModuleCell<out ModuleCell<*>>) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ModuleCell<out ModuleCell<*>>, newItem: ModuleCell<out ModuleCell<*>>) = oldItem == newItem

    override fun getChangePayload(oldItem: ModuleCell<out ModuleCell<*>>, newItem: ModuleCell<out ModuleCell<*>>) = ""
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

    override fun onBindViewHolder(holder: ViewHolderDelegate.ViewHolder<out ModuleCell<*>>, position: Int) = holder.forceBind(getItem(position))
}