package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.utils.extensions.dimension

internal data class PaddingCell(
    override val id: String,
    val size: PaddingModule.Size
) : Cell<PaddingCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<PaddingCell>() {

        override fun createViewHolder(parent: ViewGroup) = PaddingViewHolder(parent)
    }

    private class PaddingViewHolder(
        parent: ViewGroup
    ) : ViewHolder<PaddingCell>(
        LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_padding, parent, false)
    ) {

        override fun bind(model: PaddingCell) {
            itemView.apply {
                layoutParams = (layoutParams as ViewGroup.LayoutParams).apply {
                    height = context.dimension(
                        when (model.size) {
                            PaddingModule.Size.SMALL -> R.dimen.beagle_small_content_padding
                            PaddingModule.Size.MEDIUM -> R.dimen.beagle_content_padding
                            PaddingModule.Size.LARGE -> R.dimen.beagle_large_content_padding
                        }
                    )
                }
            }
        }
    }
}