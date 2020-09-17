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
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class KeyValueCell(
    override val id: String,
    private val key: CharSequence,
    private val value: CharSequence
) : Cell<KeyValueCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<KeyValueCell>() {

        override fun createViewHolder(parent: ViewGroup) = KeyValueViewHolder(parent)
    }

    private class KeyValueViewHolder(parent: ViewGroup) : ViewHolder<KeyValueCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_expanded_item_text, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
        private val bulletPointDrawable by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_bullet_point, textView.textColors.defaultColor) }

        override fun bind(model: KeyValueCell) = textView.run {
            setCompoundDrawablesWithIntrinsicBounds(bulletPointDrawable, null, null, null)
            text = SpannableString(model.key.append(": ").append(model.value)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, model.key.length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }
    }
}