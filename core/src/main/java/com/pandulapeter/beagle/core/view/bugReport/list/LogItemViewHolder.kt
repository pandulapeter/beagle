package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.utils.consume

internal class LogItemViewHolder private constructor(
    itemView: View,
    onItemSelected: (String, String?) -> Unit,
    onItemLongTapped: (String, String?) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val entry get() = itemView.tag as LogEntry
    private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemLongTapped(entry.id, entry.label)
        }
    }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemSelected(entry.id, entry.label)
            }
        }
        itemView.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongTapped(entry.id, entry.label)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.entry
        textView.text = uiModel.entry.message
        checkBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: LogEntry,
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
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_log_item, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}