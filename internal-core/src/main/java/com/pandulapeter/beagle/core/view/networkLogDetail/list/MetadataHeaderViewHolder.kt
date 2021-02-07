package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class MetadataHeaderViewHolder private constructor(
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
        setText(BeagleCore.implementation.appearance.networkLogTexts.metadata)
        setCompoundDrawablesWithIntrinsicBounds(if (uiModel.isCollapsed) drawableExpand else drawableCollapse, null, null, null)
    }

    data class UiModel(
        val isCollapsed: Boolean
    ) : NetworkLogDetailListItem {

        override val lineIndex = -300
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: () -> Unit
        ) = MetadataHeaderViewHolder(
            itemView = parent.inflater.inflate(R.layout.beagle_item_network_log_detail_metadata_header, parent, false),
            onItemClicked = onItemClicked
        )
    }
}