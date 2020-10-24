package com.pandulapeter.beagle.core.list.cells

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class TextCell(
    override val id: String,
    private val text: Text,
    private val isEnabled: Boolean,
    private val isSectionHeader: Boolean,
    @DrawableRes private val icon: Int?, //TODO: Not handled.
    val onItemSelected: (() -> Unit)?
) : Cell<TextCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<TextCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(parent)
    }

    private class TextViewHolder(parent: ViewGroup) : ViewHolder<TextCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_text, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
        private val normalHorizontalPadding = itemView.context.dimension(R.dimen.beagle_item_horizontal_margin)
        private val iconHorizontalPadding = itemView.context.dimension(R.dimen.beagle_item_horizontal_margin_large)

        override fun bind(model: TextCell) = textView.run {
            setText(model.text)
            isEnabled = model.isEnabled
            setTypeface(typeface, if (model.isSectionHeader) Typeface.BOLD else Typeface.NORMAL)
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