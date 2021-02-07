package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellTextBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class TextCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    @DrawableRes private val icon: Int?,
    val onItemSelected: (() -> Unit)?
) : Cell<TextCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<TextCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(
            binding = BeagleCellTextBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class TextViewHolder(
        private val binding: BeagleCellTextBinding
    ) : ViewHolder<TextCell>(binding.root) {

        private val normalHorizontalPadding = binding.root.context.dimension(R.dimen.beagle_item_horizontal_margin)
        private val iconHorizontalPadding = binding.root.context.dimension(R.dimen.beagle_item_horizontal_margin_large)

        override fun bind(model: TextCell) = binding.beagleTextView.run {
            setText(model.text)
            isEnabled = model.isEnabled
            setCompoundDrawablesWithIntrinsicBounds(model.icon?.let { icon -> context.tintedDrawable(icon, textColors.defaultColor) }, null, null, null)
            setPadding(
                if (model.icon == null) normalHorizontalPadding else iconHorizontalPadding,
                paddingTop,
                if (model.icon == null) normalHorizontalPadding else iconHorizontalPadding,
                paddingBottom
            )
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