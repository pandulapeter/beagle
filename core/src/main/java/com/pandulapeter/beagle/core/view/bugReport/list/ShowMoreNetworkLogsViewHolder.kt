package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal class ShowMoreNetworkLogsViewHolder private constructor(
    itemView: View,
    onButtonPressed: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.findViewById<TextView>(R.id.beagle_text_view).apply {
            setText(BeagleCore.implementation.appearance.bugReportTexts.showMoreText)
            setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onButtonPressed()
                }
            }
        }
    }

    data class UiModel(
        override val id: String = "showMoreNetworkLogs"
    ) : BugReportListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = ShowMoreNetworkLogsViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_show_more_network_logs, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}