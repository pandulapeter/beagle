package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class LineViewHolder private constructor(
    itemView: View,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val drawableExpand by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_expand, textView.textColors.defaultColor) }
    private val drawableCollapse by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_collapse, textView.textColors.defaultColor) }
    private val drawableEmpty by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_empty, textView.textColors.defaultColor) }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                (textView.tag as? Int?)?.let { lineNumber ->
                    onItemClicked(lineNumber)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        textView.tag = uiModel.lineIndex
        textView.text = uiModel.line
        textView.isClickable = uiModel.isClickable
        textView.setCompoundDrawablesWithIntrinsicBounds(
            if (uiModel.isClickable) {
                if (uiModel.isCollapsed) drawableExpand else drawableCollapse
            } else drawableEmpty,
            null, null, null
        )
    }

    data class UiModel(
        override val lineIndex: Int,
        val line: CharSequence,
        val isClickable: Boolean,
        val isCollapsed: Boolean
    ) : NetworkLogDetailListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (Int) -> Unit
        ) = LineViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_network_log_detail_line, parent, false),
            onItemClicked = onItemClicked
        )
    }
}