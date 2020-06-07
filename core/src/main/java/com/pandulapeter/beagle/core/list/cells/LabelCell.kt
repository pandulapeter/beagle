package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class LabelCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?
) : Cell<LabelCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<LabelCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(parent)
    }

    private class TextViewHolder(parent: ViewGroup) : ViewHolder<LabelCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_label, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

        override fun bind(model: LabelCell) {
            textView.run {
                text = model.text
                model.color?.let { setTextColor(it) }
            }
        }
    }
}