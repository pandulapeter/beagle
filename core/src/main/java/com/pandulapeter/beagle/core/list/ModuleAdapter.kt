package com.pandulapeter.beagle.core.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.common.contracts.Cell
import com.pandulapeter.beagle.common.contracts.CellHandler
import com.pandulapeter.beagle.common.modules.text.TextCell

internal class ModuleAdapter : ListAdapter<Cell, CellHandler.ViewHolder<out Cell>>(object : DiffUtil.ItemCallback<Cell>() {

    override fun areItemsTheSame(oldItem: Cell, newItem: Cell) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell) = oldItem == newItem

    override fun getChangePayload(oldItem: Cell, newItem: Cell) = ""
}) {
    private val cellHandlers = mutableListOf<CellHandler<*>>()

    init {
        registerCellHandler(TextCell.TextCellHandler())
    }

    private fun registerCellHandler(cellHandler: CellHandler<out Cell>) {
        cellHandlers.add(cellHandler)
    }

    override fun getItemViewType(position: Int): Int {
        val type = getItem(position)::class
        return cellHandlers.indexOfFirst { type == it.cell }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = cellHandlers[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: CellHandler.ViewHolder<out Cell>, position: Int) {
        @Suppress("UNCHECKED_CAST")
        (holder as CellHandler.ViewHolder<in Cell>).bind(getItem(position))
    }
}