package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.jsonLevel
import com.pandulapeter.beagle.utils.extensions.dimension

internal class TitleViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val longestTextView = itemView.findViewById<TextView>(R.id.beagle_longest_text_view)
    private val contentPadding = itemView.context.dimension(R.dimen.beagle_large_content_padding)

    fun bind(uiModel: UiModel) {
        textView.text = uiModel.title
        longestTextView.text = uiModel.longestLine.trim()
        longestTextView.run {
            setPadding(contentPadding * uiModel.longestLine.jsonLevel, paddingTop, 0, paddingBottom)
        }
    }

    data class UiModel(
        val title: String,
        val longestLine: String
    ) : NetworkLogDetailListItem {

        override val lineIndex = -100
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = TitleViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_network_log_detail_title, parent, false)
        )
    }
}