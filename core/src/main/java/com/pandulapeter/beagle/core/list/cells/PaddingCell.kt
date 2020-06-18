package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class PaddingCell(
    override val id: String
) : Cell<PaddingCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<PaddingCell>() {

        override fun createViewHolder(parent: ViewGroup) = PaddingViewHolder(parent)
    }

    private class PaddingViewHolder(parent: ViewGroup) : ViewHolder<PaddingCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_padding, parent, false)) {

        override fun bind(model: PaddingCell) = Unit
    }
}