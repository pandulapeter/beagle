package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportNetworkLogItemBinding
import com.pandulapeter.beagle.core.list.delegates.NetworkLogListDelegate
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.util.model.SerializableNetworkLogEntry
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class NetworkLogItemViewHolder private constructor(
    private val binding: BeagleItemBugReportNetworkLogItemBinding,
    onItemSelected: (String) -> Unit,
    onItemLongTapped: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val id get() = itemView.tag as String
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemLongTapped(id)
        }
    }

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemSelected(id)
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongTapped(id)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.entry.id
        binding.beagleTextView.setText(
            NetworkLogListDelegate.format(
                entry = uiModel.entry,
                formatter = BeagleCore.implementation.appearance.logShortTimestampFormatter
            )
        )
        binding.beagleCheckBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: SerializableNetworkLogEntry,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "networkLog_${entry.id}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (String) -> Unit,
            onItemLongTapped: (String) -> Unit
        ) = NetworkLogItemViewHolder(
            binding = BeagleItemBugReportNetworkLogItemBinding.inflate(parent.inflater, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}