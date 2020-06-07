package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class TextCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?,
    val onItemSelected: (() -> Unit)?
) : Cell<TextCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<TextCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(parent)
    }

    private class TextViewHolder(parent: ViewGroup) : ViewHolder<TextCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_text, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

        override fun bind(model: TextCell) {
            textView.run {
                text = model.text
                model.color?.let { setTextColor(it) }
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
                }
            }
        }
    }
}