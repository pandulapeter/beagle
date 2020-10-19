package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.view.bugReport.BugReportViewModel
import com.pandulapeter.beagle.utils.consume

internal class MetadataItemViewHolder private constructor(
    itemView: View,
    onItemClicked: (BugReportViewModel.MetadataType) -> Unit,
    onItemSelectionChanged: (BugReportViewModel.MetadataType) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val type get() = itemView.tag as BugReportViewModel.MetadataType
    private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemSelectionChanged(type)
        }
    }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked(type)
            }
        }
        itemView.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemSelectionChanged(type)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.type
        textView.setText(
            when (uiModel.type) {
                BugReportViewModel.MetadataType.BUILD_INFORMATION -> BeagleCore.implementation.appearance.bugReportTexts.buildInformation
                BugReportViewModel.MetadataType.DEVICE_INFORMATION -> BeagleCore.implementation.appearance.bugReportTexts.deviceInformation
            }
        )
        checkBox.run {
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
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_metadata_item, parent, false),
            onItemClicked = onItemClicked,
            onItemSelectionChanged = onItemSelectionChanged
        )
    }
}