package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class ButtonCell(
    override val id: String,
    private val text: CharSequence,
    private val onButtonPressed: () -> Unit
) : Cell<ButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = ButtonViewHolder(parent)
    }

    private class ButtonViewHolder(parent: ViewGroup) : ViewHolder<ButtonCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_button, parent, false)) {

        private val button = itemView.findViewById<Button>(R.id.beagle_button)

        override fun bind(model: ButtonCell) = button.run {
            text = model.text
            itemView.setOnClickListener {
                adapterPosition.let { bindingAdapterPosition ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        model.onButtonPressed()
                    }
                }
            }
        }
    }
}