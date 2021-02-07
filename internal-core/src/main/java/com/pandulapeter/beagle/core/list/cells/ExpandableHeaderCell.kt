package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleCellExpandableHeaderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal data class ExpandableHeaderCell(
    override val id: String,
    private val text: Text,
    val isExpanded: Boolean,
    val canExpand: Boolean,
    val onItemSelected: () -> Unit
) : Cell<ExpandableHeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandableHeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(
            binding = BeagleCellExpandableHeaderBinding.inflate(parent.inflater, parent, false)
        )
    }

    private class TextViewHolder(
        private val binding: BeagleCellExpandableHeaderBinding
    ) : ViewHolder<ExpandableHeaderCell>(binding.root) {

        private val drawableExpand by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_expand, binding.beagleTextView.textColors.defaultColor) }
        private val drawableCollapse by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_collapse, binding.beagleTextView.textColors.defaultColor) }
        private val drawableEmpty by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_empty, binding.beagleTextView.textColors.defaultColor) }

        override fun bind(model: ExpandableHeaderCell) = binding.beagleTextView.run {
            setText(model.text)
            setCompoundDrawablesWithIntrinsicBounds(null, null, if (model.canExpand) if (model.isExpanded) drawableCollapse else drawableExpand else drawableEmpty, null)
            setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    model.onItemSelected()
                }
            }
            isClickable = model.canExpand
        }
    }
}