package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal class SendButtonViewHolder private constructor(
    itemView: View,
    onSendButtonPressed: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val button = itemView.findViewById<Button>(R.id.beagle_button)

    init {
        button.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onSendButtonPressed()
            }
        }
    }

    fun bind(uiModel: UiModel) = button.run {
        setText(BeagleCore.implementation.appearance.bugReportTexts.sendButtonText)
        isEnabled = uiModel.isEnabled
    }

    data class UiModel(
        val isEnabled: Boolean
    ) : BugReportListItem {

        override val id = "sendButton"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSendButtonPressed: () -> Unit
        ) = SendButtonViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_send_button, parent, false),
            onSendButtonPressed = onSendButtonPressed
        )
    }
}