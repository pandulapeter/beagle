package com.pandulapeter.beagle.core.list.cells

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension

internal data class ExpandableHeaderCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?,
    val isExpanded: Boolean,
    val onItemSelected: () -> Unit
) : Cell<ExpandableHeaderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<ExpandableHeaderCell>() {

        override fun createViewHolder(parent: ViewGroup) = TextViewHolder(parent.context)
    }

    private class TextViewHolder(context: Context) : ViewHolder<ExpandableHeaderCell>(AppCompatTextView(context)) {

        init {
            itemView.context.dimension(R.dimen.beagle_content_padding).let { padding ->
                itemView.setPadding(padding * 2, padding, padding, padding)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(model: ExpandableHeaderCell) {
            (itemView as TextView).run {
                text = "${model.text} [${if (model.isExpanded) "EXPANDED" else "COLLAPSED"}]"
                model.color?.let { setTextColor(it) }
                setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        model.onItemSelected()
                    }
                }
            }
        }
    }
}