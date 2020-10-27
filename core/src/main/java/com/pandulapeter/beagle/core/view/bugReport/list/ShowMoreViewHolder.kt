package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.setText

internal class ShowMoreViewHolder private constructor(
    itemView: View,
    onButtonPressed: (Type) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    val type get() = itemView.tag as? Type?

    init {
        itemView.findViewById<TextView>(R.id.beagle_text_view).apply {
            setText(BeagleCore.implementation.appearance.bugReportTexts.showMoreText)
            setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    type?.let { onButtonPressed(it) }
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.type
    }

    data class UiModel(
        val type: Type
    ) : BugReportListItem {
        override val id: String = "showMore_${type.id}"
    }

    sealed class Type(val id: String) {

        object CrashLog : Type("crashLog")

        object NetworkLog : Type("networkLog")

        data class Log(val label: String?) : Type("log_$label")

        object LifecycleLog : Type("lifecycleLog")
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: (Type) -> Unit
        ) = ShowMoreViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_show_more, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}