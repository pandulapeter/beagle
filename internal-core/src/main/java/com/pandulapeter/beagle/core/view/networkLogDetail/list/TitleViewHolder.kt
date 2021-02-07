package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.utils.extensions.inflater

internal class TitleViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)


    fun bind(uiModel: UiModel) {
        textView.text = uiModel.title
    }

    data class UiModel(
        val title: String
    ) : NetworkLogDetailListItem {

        override val lineIndex = -100
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = TitleViewHolder(
            itemView = parent.inflater.inflate(R.layout.beagle_item_network_log_detail_title, parent, false)
        )
    }
}