package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellButtonBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ButtonCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    @DrawableRes private val icon: Int?, //TODO: Not handled.
    private val onButtonPressed: (() -> Unit)?
) : Cell<ButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = ButtonViewHolder(
            binding = BeagleCellButtonBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class ButtonViewHolder(
        private val binding: BeagleCellButtonBinding
    ) : ViewHolder<ButtonCell>(binding.root) {

        private val normalHorizontalPadding = binding.root.context.dimension(R.dimen.beagle_item_horizontal_margin)
        private val iconHorizontalPadding = binding.root.context.dimension(R.dimen.beagle_small_content_padding)
        private val drawablePadding = binding.root.context.dimension(R.dimen.beagle_medium_content_padding)

        override fun bind(model: ButtonCell) = binding.beagleButton.run {
            setText(model.text)
            setCompoundDrawablesWithIntrinsicBounds(model.icon?.let { icon -> context.tintedDrawable(icon, textColors.defaultColor) }, null, null, null)
            setPadding(
                if (model.icon == null) normalHorizontalPadding else iconHorizontalPadding,
                paddingTop,
                normalHorizontalPadding,
                paddingBottom
            )
            compoundDrawablePadding = drawablePadding
            setOnClickListener {
                bindingAdapterPosition.let { bindingAdapterPosition ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        model.onButtonPressed?.invoke()
                    }
                }
            }
            isClickable = model.onButtonPressed != null
            isEnabled = model.isEnabled
        }
    }
}