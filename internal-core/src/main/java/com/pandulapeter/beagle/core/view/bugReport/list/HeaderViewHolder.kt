package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.util.extension.visible

internal class HeaderViewHolder private constructor(
    itemView: View,
    private val onAttachAllButtonClicked: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val id get() = itemView.tag as String
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val attachAllButton = itemView.findViewById<TextView>(R.id.beagle_attach_all_button)

    init {
        attachAllButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onAttachAllButtonClicked(id)
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.id
        textView.setText(uiModel.text)
        attachAllButton.setText(BeagleCore.implementation.appearance.bugReportTexts.selectAllText)
        attachAllButton.visible = uiModel.shouldShowAttachAllButton
    }

    data class UiModel(
        override val id: String,
        val text: Text,
        val shouldShowAttachAllButton: Boolean,
    ) : BugReportListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onAttachAllButtonClicked: (String) -> Unit
        ) = HeaderViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_header, parent, false),
            onAttachAllButtonClicked = onAttachAllButtonClicked
        )
    }
}