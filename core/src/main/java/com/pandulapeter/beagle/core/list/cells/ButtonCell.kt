package com.pandulapeter.beagle.core.list.cells

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ButtonCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    @DrawableRes private val icon: Int?, //TODO: Not handled.
    private val onButtonPressed: (() -> Unit)?
) : Cell<ButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ButtonCell>() {

        override fun createViewHolder(parent: ViewGroup) = ButtonViewHolder(parent)
    }

    private class ButtonViewHolder(parent: ViewGroup) : ViewHolder<ButtonCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_button, parent, false)) {

        private val button = itemView.findViewById<Button>(R.id.beagle_button)
        private val normalHorizontalPadding = itemView.context.dimension(R.dimen.beagle_item_horizontal_margin)
        private val iconHorizontalPadding = itemView.context.dimension(R.dimen.beagle_content_padding_small)
        private val drawablePadding = itemView.context.dimension(R.dimen.beagle_medium_content_padding)

        override fun bind(model: ButtonCell) = button.run {
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
                adapterPosition.let { bindingAdapterPosition ->
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