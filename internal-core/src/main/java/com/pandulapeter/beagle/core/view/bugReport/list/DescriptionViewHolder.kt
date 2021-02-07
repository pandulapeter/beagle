package com.pandulapeter.beagle.core.view.bugReport.list

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportDescriptionBinding
import com.pandulapeter.beagle.utils.extensions.inflater

internal class DescriptionViewHolder private constructor(
    private val binding: BeagleItemBugReportDescriptionBinding,
    private val onTextChanged: (Int, CharSequence) -> Unit
) : RecyclerView.ViewHolder(binding.root), TextWatcher {

    private val index get() = binding.beagleEditText.tag as Int

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onTextChanged(index, s ?: "")
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit

    fun bind(uiModel: UiModel) = binding.beagleEditText.run {
        tag = uiModel.index
        removeTextChangedListener(this@DescriptionViewHolder)
        setText(uiModel.text)
        addTextChangedListener(this@DescriptionViewHolder)
    }

    data class UiModel(
        val index: Int,
        val text: CharSequence
    ) : BugReportListItem {

        override val id = "description_${index}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTextChanged: (Int, CharSequence) -> Unit
        ) = DescriptionViewHolder(
            binding = BeagleItemBugReportDescriptionBinding.inflate(parent.inflater, parent, false),
            onTextChanged = onTextChanged
        )
    }
}