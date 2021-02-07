package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellHeaderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class HeaderCell(
    override val id: String,
    private val title: Text,
    private val subtitle: Text?,
    private val text: Text?
) : Cell<HeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<HeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = HeaderViewHolder(
            binding = BeagleCellHeaderBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class HeaderViewHolder(
        private val binding: BeagleCellHeaderBinding
    ) : ViewHolder<HeaderCell>(binding.root) {

        override fun bind(model: HeaderCell) {
            binding.beagleTitle.setText(model.title)
            binding.beagleSubtitle.run {
                visible = model.subtitle != null
                setText(model.subtitle)
            }
            binding.beagleText.run {
                visible = model.text != null
                setText(model.text)
            }
        }
    }
}