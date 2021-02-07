package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellLoadingIndicatorBinding
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class LoadingIndicatorCell(
    override val id: String
) : Cell<LoadingIndicatorCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<LoadingIndicatorCell>() {

        override fun createViewHolder(parent: ViewGroup) = LoadingIndicatorViewHolder(
            binding = BeagleCellLoadingIndicatorBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class LoadingIndicatorViewHolder(
        binding: BeagleCellLoadingIndicatorBinding
    ) : ViewHolder<LoadingIndicatorCell>(binding.root) {

        override fun bind(model: LoadingIndicatorCell) = Unit
    }
}