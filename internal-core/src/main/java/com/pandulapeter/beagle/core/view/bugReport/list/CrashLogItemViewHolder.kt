package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportCrashLogItemBinding
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class CrashLogItemViewHolder private constructor(
    private val binding: BeagleItemBugReportCrashLogItemBinding,
    onItemSelected: (String) -> Unit,
    onItemLongTapped: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val id get() = itemView.tag as String
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            onItemLongTapped(id)
        }
    }

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemSelected(id)
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongTapped(id)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.entry.id
        binding.beagleTextView.text = uiModel.entry.getFormattedTitle(formatter = BeagleCore.implementation.appearance.logShortTimestampFormatter)
        binding.beagleCheckBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: SerializableCrashLogEntry,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "crashLog_${entry.id}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (String) -> Unit,
            onItemLongTapped: (String) -> Unit
        ) = CrashLogItemViewHolder(
            binding = BeagleItemBugReportCrashLogItemBinding.inflate(parent.inflater, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}