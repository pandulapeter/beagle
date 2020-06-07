package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class CheckBoxCellRenameMe(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?,
    private val isChecked: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<CheckBoxCellRenameMe> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<CheckBoxCellRenameMe>() {

        override fun createViewHolder(parent: ViewGroup) = CheckBoxViewHolder(parent)
    }

    private class CheckBoxViewHolder(parent: ViewGroup) : ViewHolder<CheckBoxCellRenameMe>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_check_box, parent, false)) {

        private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)

        override fun bind(model: CheckBoxCellRenameMe) {
            checkBox.run {
                text = model.text
                model.color?.let { setTextColor(it) }
                setOnCheckedChangeListener(null)
                isChecked = model.isChecked
                setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
            }
        }
    }
}