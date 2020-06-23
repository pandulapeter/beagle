package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.colorResource


internal data class DividerCell(
    override val id: String
) : Cell<DividerCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<DividerCell>() {

        override fun createViewHolder(parent: ViewGroup) = PaddingViewHolder(parent)
    }

    private class PaddingViewHolder(parent: ViewGroup) : ViewHolder<DividerCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_divider, parent, false)) {

        override fun bind(model: DividerCell) = itemView.setBackgroundColor(itemView.context.colorResource(android.R.attr.textColorPrimary))
    }
}

