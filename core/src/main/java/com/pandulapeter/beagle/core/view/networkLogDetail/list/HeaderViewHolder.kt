package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class HeaderViewHolder private constructor(
    itemView: View,
    onItemClicked: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val drawableExpand by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_expand, textView.textColors.defaultColor) }
    private val drawableCollapse by lazy { itemView.context.tintedDrawable(R.drawable.beagle_ic_collapse, textView.textColors.defaultColor) }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked()
            }
        }
    }

    fun bind(uiModel: UiModel) = textView.run {
        setText(uiModel.content)
        setCompoundDrawablesWithIntrinsicBounds(null, null, if (uiModel.isCollapsed) drawableExpand else drawableCollapse, null)
    }

    data class UiModel(
        override val lineIndex: Int,
        val content: Text,
        val isCollapsed: Boolean
    ) : NetworkLogDetailListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: () -> Unit
        ) = HeaderViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_network_log_detail_header, parent, false),
            onItemClicked = onItemClicked
        )
    }
}