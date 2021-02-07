package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellSwitchBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater

internal data class SwitchCell(
    override val id: String,
    private val text: Text,
    private val isChecked: Boolean,
    private val isEnabled: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<SwitchCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<SwitchCell>() {

        override fun createViewHolder(parent: ViewGroup) = SwitchViewHolder(
            binding = BeagleCellSwitchBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class SwitchViewHolder(
        private val binding: BeagleCellSwitchBinding
    ) : ViewHolder<SwitchCell>(binding.root) {

        override fun bind(model: SwitchCell) = binding.beagleSwitch.run {
            setText(model.text)
            setOnCheckedChangeListener(null)
            isChecked = model.isChecked
            isEnabled = model.isEnabled
            setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
        }
    }
}