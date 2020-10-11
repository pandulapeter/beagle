package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal data class ButtonCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    @DrawableRes private val icon: Int?, //TODO: Not handled.
    private val onButtonPressed: (() -> Unit)?
) : Cell<ButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = ButtonViewHolder(parent)
    }

    private class ButtonViewHolder(parent: ViewGroup) : ViewHolder<ButtonCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_button, parent, false)) {

        private val button = itemView.findViewById<Button>(R.id.beagle_button)

        override fun bind(model: ButtonCell) = button.run {
            setText(model.text)
            setOnClickListener {
                adapterPosition.let { bindingAdapterPosition ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        model.onButtonPressed?.invoke()
                    }
                }
            }
            isClickable = model.onButtonPressed != null
            isEnabled = model.isEnabled
        }
    }
}