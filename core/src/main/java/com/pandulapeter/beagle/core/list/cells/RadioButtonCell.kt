package com.pandulapeter.beagle.core.list.cells

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatRadioButton
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension

internal data class RadioButtonCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?,
    private val isChecked: Boolean,
    private val onValueChanged: (Boolean) -> Unit
) : Cell<RadioButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<RadioButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = SwitchViewHolder(parent.context)
    }

    private class SwitchViewHolder(context: Context) : ViewHolder<RadioButtonCell>(AppCompatRadioButton(context)) {

        init {
            itemView.context.dimension(R.dimen.beagle_content_padding).let { padding ->
                itemView.setPadding(padding, padding, padding, padding)
            }
        }

        override fun bind(model: RadioButtonCell) {
            (itemView as AppCompatRadioButton).run {
                text = model.text
                model.color?.let { setTextColor(it) }
                setOnCheckedChangeListener(null)
                isChecked = model.isChecked
                setOnCheckedChangeListener { _, isChecked -> model.onValueChanged(isChecked) }
            }
        }
    }
}