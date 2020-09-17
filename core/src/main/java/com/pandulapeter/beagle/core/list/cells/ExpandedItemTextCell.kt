package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class ExpandedItemTextCell(
    override val id: String,
    private val text: CharSequence,
    private val isEnabled: Boolean,
    val onItemSelected: (() -> Unit)?
) : Cell<ExpandedItemTextCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandedItemTextCell>() {

        override fun createViewHolder(parent: ViewGroup) = ExpandedItemTextViewHolder(parent)
    }

    private class ExpandedItemTextViewHolder(parent: ViewGroup) : ViewHolder<ExpandedItemTextCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_expanded_item_text, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

        override fun bind(model: ExpandedItemTextCell) = textView.run {
            text = model.text
            isEnabled = model.isEnabled
            alpha = if (model.isEnabled) 1f else 0.6f
            model.onItemSelected.let { onItemSelected ->
                if (onItemSelected == null) {
                    setOnClickListener(null)
                } else {
                    setOnClickListener {
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            onItemSelected()
                        }
                    }
                }
                isClickable = onItemSelected != null && model.isEnabled
            }
        }
    }
}