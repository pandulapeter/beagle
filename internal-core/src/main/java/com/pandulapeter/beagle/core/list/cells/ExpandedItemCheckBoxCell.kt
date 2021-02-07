package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellExpandedItemCheckBoxBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class ExpandedItemCheckBoxCell(
    override val id: String,
    private val text: Text,
    private val isChecked: Boolean,
    private val isEnabled: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<ExpandedItemCheckBoxCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandedItemCheckBoxCell>() {

        override fun createViewHolder(parent: ViewGroup) = CheckBoxViewHolder(
            binding = BeagleCellExpandedItemCheckBoxBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class CheckBoxViewHolder(
        private val binding: BeagleCellExpandedItemCheckBoxBinding
    ) : ViewHolder<ExpandedItemCheckBoxCell>(binding.root) {

        override fun bind(model: ExpandedItemCheckBoxCell) = binding.beagleCheckBox.run {
            setText(model.text)
            setOnCheckedChangeListener(null)
            isChecked = model.isChecked
            isEnabled = model.isEnabled
            setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
        }
    }
}