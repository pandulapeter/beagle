package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal class HeaderViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

    fun bind(uiModel: UiModel) = textView.setText(uiModel.text)

    data class UiModel(
        override val id: String,
        val text: Text
    ) : BugReportListItem

    companion object {
        fun create(parent: ViewGroup) = HeaderViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_header, parent, false)
        )
    }
}