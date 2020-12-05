package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.utils.extensions.colorResource


internal data class DividerCell(
    override val id: String
) : Cell<DividerCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<DividerCell>() {

        override fun createViewHolder(parent: ViewGroup) = DividerViewHolder(parent)
    }

    private class DividerViewHolder(parent: ViewGroup) : ViewHolder<DividerCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_divider, parent, false)) {

        override fun bind(model: DividerCell) = itemView.setBackgroundColor(itemView.context.colorResource(android.R.attr.textColorPrimary))
    }
}