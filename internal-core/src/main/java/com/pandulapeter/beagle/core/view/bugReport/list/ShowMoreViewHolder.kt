package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportShowMoreBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater

internal class ShowMoreViewHolder private constructor(
    private val binding: BeagleItemBugReportShowMoreBinding,
    onButtonPressed: (Type) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    val type get() = itemView.tag as? Type?

    init {
        binding.beagleTextView.run {
            setText(BeagleCore.implementation.appearance.bugReportTexts.showMoreText)
            setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
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
            binding = BeagleItemBugReportShowMoreBinding.inflate(parent.inflater, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}