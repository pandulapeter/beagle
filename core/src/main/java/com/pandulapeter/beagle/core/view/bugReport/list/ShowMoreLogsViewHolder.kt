package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal class ShowMoreLogsViewHolder private constructor(
    itemView: View,
    onButtonPressed: (String?) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val label get() = itemView.tag as? String?

    init {
        itemView.findViewById<TextView>(R.id.beagle_text_view).apply {
            setText(BeagleCore.implementation.appearance.bugReportTexts.showMoreText)
            setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onButtonPressed(label)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.label
    }

    data class UiModel(
        val label: String?
    ) : BugReportListItem {
        override val id: String = "showMoreLogs_$label"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: (String?) -> Unit
        ) = ShowMoreLogsViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_show_more_logs, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}