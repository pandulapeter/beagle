package com.pandulapeter.beagle.core.list.cells

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.ViewGroup
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellExpandedItemTextBinding
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ExpandedItemKeyValueCell(
    override val id: String,
    private val key: Text,
    private val value: Text
) : Cell<ExpandedItemKeyValueCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandedItemKeyValueCell>() {

        override fun createViewHolder(parent: ViewGroup) = KeyValueViewHolder(
            binding = BeagleCellExpandedItemTextBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class KeyValueViewHolder(
        private val binding: BeagleCellExpandedItemTextBinding
    ) : ViewHolder<ExpandedItemKeyValueCell>(binding.root) {

        private val bulletPointDrawable by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_bullet_point, binding.beagleTextView.textColors.defaultColor) }

        override fun bind(model: ExpandedItemKeyValueCell) = binding.beagleTextView.run {
            setCompoundDrawablesWithIntrinsicBounds(bulletPointDrawable, null, null, null)
            val key = context.text(model.key)
            val value = context.text(model.value)
            text = SpannableString(key.append(": ").append(value)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, key.length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }
    }
}