package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportLogItemBinding
import com.pandulapeter.beagle.core.list.delegates.LogListDelegate
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class LogItemViewHolder private constructor(
    private val binding: BeagleItemBugReportLogItemBinding,
    onItemSelected: (String, String?) -> Unit,
    onItemLongTapped: (String, String?) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val entry get() = itemView.tag as SerializableLogEntry
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            onItemLongTapped(entry.id, entry.label)
        }
    }

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemSelected(entry.id, entry.label)
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongTapped(entry.id, entry.label)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.entry
        binding.beagleTextView.setText(LogListDelegate.format(uiModel.entry, BeagleCore.implementation.appearance.logShortTimestampFormatter))
        binding.beagleCheckBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: SerializableLogEntry,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "log_${entry.id}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (String, String?) -> Unit,
            onItemLongTapped: (String, String?) -> Unit
        ) = LogItemViewHolder(
            binding = BeagleItemBugReportLogItemBinding.inflate(parent.inflater, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}