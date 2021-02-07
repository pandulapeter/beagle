package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellPaddingBinding
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class PaddingCell(
    override val id: String,
    val size: PaddingModule.Size
) : Cell<PaddingCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<PaddingCell>() {

        override fun createViewHolder(parent: ViewGroup) = PaddingViewHolder(
            binding = BeagleCellPaddingBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class PaddingViewHolder(
        private val binding: BeagleCellPaddingBinding
    ) : ViewHolder<PaddingCell>(binding.root) {

        override fun bind(model: PaddingCell) {
            binding.root.apply {
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