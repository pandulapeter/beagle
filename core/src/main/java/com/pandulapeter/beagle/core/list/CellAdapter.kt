package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.common.contracts.module.ViewHolderDelegate
import kotlin.reflect.KClass

internal class CellAdapter : ListAdapter<Cell<out Cell<*>>, ViewHolder<out Cell<*>>>(object : DiffUtil.ItemCallback<Cell<*>>() {

    override fun areItemsTheSame(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = oldItem == newItem

    override fun getChangePayload(oldItem: Cell<out Cell<*>>, newItem: Cell<out Cell<*>>) = ""
}) {
    private val delegates = mutableMapOf<KClass<*>, ViewHolderDelegate<*>>()

    override fun getItemViewType(position: Int): Int {
        val cell = getItem(position)
        return delegates.keys.indexOf(cell::class).let { index ->
            if (index == -1) {
                delegates[cell::class] = cell.createViewHolderDelegate()
                delegates.size - 1
            } else index
        }
    }

    //TODO: Optimize
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*> = delegates.values.toList()[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder<out Cell<*>>, position: Int) = holder.forceBind(getItem(position))
}