package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.utils.consume

internal class NetworkLogItemViewHolder private constructor(
    itemView: View,
    onItemSelected: (Int) -> Unit,
    onItemLongTapped: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val checkBox = itemView.findViewById<CheckBox>(R.id.beagle_check_box)
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val checkedChangedListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        adapterPosition.let { adapterPosition ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemLongTapped(adapterPosition)
            }
        }
    }

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemSelected(adapterPosition)
                }
            }
        }
        itemView.setOnLongClickListener {
            consume {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemLongTapped(adapterPosition)
                    }
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        textView.text = uiModel.entry.url
        checkBox.run {
            setOnCheckedChangeListener(null)
            isChecked = uiModel.isSelected
            setOnCheckedChangeListener(checkedChangedListener)
        }
    }

    data class UiModel(
        val entry: NetworkLogEntry,
        val isSelected: Boolean
    ) : BugReportListItem {

        override val id: String = "networkLog_${entry.id}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (Int) -> Unit,
            onItemLongTapped: (Int) -> Unit
        ) = NetworkLogItemViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_network_log_item, parent, false),
            onItemSelected = onItemSelected,
            onItemLongTapped = onItemLongTapped
        )
    }
}