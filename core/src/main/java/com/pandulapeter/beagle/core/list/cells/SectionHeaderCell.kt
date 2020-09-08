package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class SectionHeaderCell(
    override val id: String,
    private val title: CharSequence
) : Cell<SectionHeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<SectionHeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = SectionHeaderViewHolder(parent)
    }

    private class SectionHeaderViewHolder(parent: ViewGroup) : ViewHolder<SectionHeaderCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_section_header, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

        override fun bind(model: SectionHeaderCell) = textView.run {
            text = model.title
        }
    }
}