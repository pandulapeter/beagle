package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R


internal data class LoadingIndicatorCell(
    override val id: String
) : Cell<LoadingIndicatorCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<LoadingIndicatorCell>() {

        override fun createViewHolder(parent: ViewGroup) = LoadingIndicatorViewHolder(parent)
    }

    private class LoadingIndicatorViewHolder(parent: ViewGroup) : ViewHolder<LoadingIndicatorCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_loading_indicator, parent, false)) {

        override fun bind(model: LoadingIndicatorCell) = Unit
    }
}