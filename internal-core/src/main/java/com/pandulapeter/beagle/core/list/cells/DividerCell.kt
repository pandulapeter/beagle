package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellDividerBinding
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class DividerCell(
    override val id: String
) : Cell<DividerCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<DividerCell>() {

        override fun createViewHolder(parent: ViewGroup) = DividerViewHolder(
            binding = BeagleCellDividerBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class DividerViewHolder(
        private val binding: BeagleCellDividerBinding
    ) : ViewHolder<DividerCell>(binding.root) {

        override fun bind(model: DividerCell) = binding.root.setBackgroundColor(binding.root.context.colorResource(android.R.attr.textColorPrimary))
    }
}