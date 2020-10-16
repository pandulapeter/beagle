package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class DetailsViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

    fun bind(uiModel: UiModel) {
        textView.text = uiModel.metadata
    }

    data class UiModel(
        val metadata: CharSequence,
    ) : NetworkLogDetailListItem {

        override val lineIndex = -200
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = DetailsViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_network_log_detail_details, parent, false)
        )
    }
}