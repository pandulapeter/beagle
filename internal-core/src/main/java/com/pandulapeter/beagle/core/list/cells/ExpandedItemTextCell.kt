package com.pandulapeter.beagle.core.list.cells

import android.text.TextUtils
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellExpandedItemTextBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ExpandedItemTextCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    private val shouldEllipsize: Boolean,
    val onItemSelected: (() -> Unit)?
) : Cell<ExpandedItemTextCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandedItemTextCell>() {

        override fun createViewHolder(parent: ViewGroup) = ExpandedItemTextViewHolder(
            binding = BeagleCellExpandedItemTextBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class ExpandedItemTextViewHolder(
        private val binding: BeagleCellExpandedItemTextBinding
    ) : ViewHolder<ExpandedItemTextCell>(binding.root) {

        private val bulletPointDrawable by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_bullet_point, binding.beagleTextView.textColors.defaultColor) }

        override fun bind(model: ExpandedItemTextCell) = binding.beagleTextView.run {
            setText(model.text)
            maxLines = if (model.shouldEllipsize) 4 else Int.MAX_VALUE
            ellipsize = TextUtils.TruncateAt.END
            isEnabled = model.isEnabled
            alpha = if (model.isEnabled) 1f else 0.6f
            setCompoundDrawablesWithIntrinsicBounds(bulletPointDrawable, null, null, null)
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