package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class ExpandedItemRadioButtonCell(
    override val id: String,
    private val text: CharSequence,
    private val isChecked: Boolean,
    private val isEnabled: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<ExpandedItemRadioButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandedItemRadioButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = SwitchViewHolder(parent)
    }

    private class SwitchViewHolder(parent: ViewGroup) : ViewHolder<ExpandedItemRadioButtonCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_expanded_item_radio_button, parent, false)) {

        private val radioButton = itemView.findViewById<RadioButton>(R.id.beagle_radio_button)

        override fun bind(model: ExpandedItemRadioButtonCell) = radioButton.run {
            text = model.text
            setOnCheckedChangeListener(null)
            isChecked = model.isChecked
            isEnabled = model.isEnabled
            setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
        }
    }
}