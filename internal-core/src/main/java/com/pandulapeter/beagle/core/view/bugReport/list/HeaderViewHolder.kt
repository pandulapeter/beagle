package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportHeaderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.utils.extensions.inflater

internal class HeaderViewHolder private constructor(
    private val binding: BeagleItemBugReportHeaderBinding,
    private val onAttachAllButtonClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val id get() = itemView.tag as String

    init {
        binding.beagleAttachAllButton.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onAttachAllButtonClicked(id)
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.id
        binding.beagleTextView.setText(uiModel.text)
        binding.beagleAttachAllButton.run {
            setText(BeagleCore.implementation.appearance.bugReportTexts.selectAllText)
            visible = uiModel.shouldShowAttachAllButton
        }
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
            binding = BeagleItemBugReportHeaderBinding.inflate(parent.inflater, parent, false),
            onAttachAllButtonClicked = onAttachAllButtonClicked
        )
    }
}