package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportMetadataItemBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.view.bugReport.BugReportViewModel
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class MetadataItemViewHolder private constructor(
    private val binding: BeagleItemBugReportMetadataItemBinding,
    onItemClicked: (BugReportViewModel.MetadataType) -> Unit,
    onItemSelectionChanged: (BugReportViewModel.MetadataType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val type get() = itemView.tag as BugReportViewModel.MetadataType
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemSelectionChanged(type)
        }
    }

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked(type)
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemSelectionChanged(type)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.type
        binding.beagleTextView.setText(
            when (uiModel.type) {
                BugReportViewModel.MetadataType.BUILD_INFORMATION -> BeagleCore.implementation.appearance.bugReportTexts.buildInformation
                BugReportViewModel.MetadataType.DEVICE_INFORMATION -> BeagleCore.implementation.appearance.bugReportTexts.deviceInformation
            }
        )
        binding.beagleCheckBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val type: BugReportViewModel.MetadataType,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "metadataItem_${type.name}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (BugReportViewModel.MetadataType) -> Unit,
            onItemSelectionChanged: (BugReportViewModel.MetadataType) -> Unit
        ) = MetadataItemViewHolder(
            binding = BeagleItemBugReportMetadataItemBinding.inflate(parent.inflater, parent, false),
            onItemClicked = onItemClicked,
            onItemSelectionChanged = onItemSelectionChanged
        )
    }
}