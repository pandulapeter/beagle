package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class CheckBoxCell(
    override val id: String,
    private val text: CharSequence,
    private val isChecked: Boolean,
    private val isEnabled: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<CheckBoxCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<CheckBoxCell>() {

        override fun createViewHolder(parent: ViewGroup) = CheckBoxViewHolder(parent)
    }

    private class CheckBoxViewHolder(parent: ViewGroup) : ViewHolder<CheckBoxCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_check_box, parent, false)) {

        private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)

        override fun bind(model: CheckBoxCell) {
            checkBox.run {
                text = model.text
                setOnCheckedChangeListener(null)
                isChecked = model.isChecked
                isEnabled = model.isEnabled
                setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
            }
        }
    }
}