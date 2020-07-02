package com.pandulapeter.beagle.core.list.cells

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.append

internal data class KeyValueCell(
    override val id: String,
    private val key: CharSequence,
    private val value: CharSequence
) : Cell<KeyValueCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<KeyValueCell>() {

        override fun createViewHolder(parent: ViewGroup) = KeyValueViewHolder(parent)
    }

    //TODO: Use a different layout
    private class KeyValueViewHolder(parent: ViewGroup) : ViewHolder<KeyValueCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_text, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

        override fun bind(model: KeyValueCell) = textView.run {
            text = SpannableString("• ".append(model.key.append(": ")).append(model.value)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 2, model.key.length + 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }
    }
}