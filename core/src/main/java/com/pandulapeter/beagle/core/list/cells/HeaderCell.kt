package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.visible

internal data class HeaderCell(
    override val id: String,
    private val title: CharSequence,
    private val subtitle: CharSequence?,
    private val text: CharSequence?
) : Cell<HeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<HeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = HeaderViewHolder(parent)
    }

    private class HeaderViewHolder(parent: ViewGroup) : ViewHolder<HeaderCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_header, parent, false)) {

        private val titleTextView = itemView.findViewById<TextView>(R.id.beagle_title)
        private val subtitleTextView = itemView.findViewById<TextView>(R.id.beagle_subtitle)
        private val textTextView = itemView.findViewById<TextView>(R.id.beagle_text)

        override fun bind(model: HeaderCell) {
            titleTextView.text = model.title
            subtitleTextView.visible = model.subtitle != null
            subtitleTextView.text = model.subtitle
            textTextView.visible = model.text != null
            textTextView.text = model.text
        }
    }
}