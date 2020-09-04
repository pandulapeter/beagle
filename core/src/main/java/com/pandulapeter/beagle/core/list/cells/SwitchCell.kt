package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class SwitchCell(
    override val id: String,
    private val text: CharSequence,
    private val isChecked: Boolean,
    private val isEnabled: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<SwitchCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<SwitchCell>() {

        override fun createViewHolder(parent: ViewGroup) = SwitchViewHolder(parent)
    }

    private class SwitchViewHolder(parent: ViewGroup) : ViewHolder<SwitchCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_switch, parent, false)) {

        private val switch = itemView.findViewById<SwitchCompat>(R.id.beagle_switch)

        override fun bind(model: SwitchCell) = switch.run {
            text = model.text
            setOnCheckedChangeListener(null)
            isChecked = model.isChecked
            isEnabled = model.isEnabled
            setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
        }
    }
}