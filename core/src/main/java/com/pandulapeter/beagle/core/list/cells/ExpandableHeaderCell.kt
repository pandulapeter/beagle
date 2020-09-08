package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ExpandableHeaderCell(
    override val id: String,
    private val text: CharSequence,
    val isExpanded: Boolean,
    val canExpand: Boolean,
    val onItemSelected: () -> Unit
) : Cell<ExpandableHeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandableHeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(parent)
    }

    private class TextViewHolder(parent: ViewGroup) : ViewHolder<ExpandableHeaderCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_expandable_header, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
        private val drawableExpand by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_expand, textView.textColors.defaultColor) }
        private val drawableCollapse by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_collapse, textView.textColors.defaultColor) }
        private val drawableEmpty by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_empty, textView.textColors.defaultColor) }

        override fun bind(model: ExpandableHeaderCell) {
            textView.run {
                text = model.text
                setCompoundDrawablesWithIntrinsicBounds(null, null, if (model.canExpand) if (model.isExpanded) drawableCollapse else drawableExpand else drawableEmpty, null)
                setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        model.onItemSelected()
                    }
                }
                isClickable = model.canExpand
            }
        }
    }
}