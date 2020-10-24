package com.pandulapeter.beagle.core.view.bugReport.list

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R

internal class DescriptionViewHolder private constructor(
    itemView: View,
    private val onTextChanged: (Int, CharSequence) -> Unit
) : RecyclerView.ViewHolder(itemView), TextWatcher {

    private val editText = itemView.findViewById<EditText>(R.id.beagle_edit_text)
    private val index get() = editText.tag as Int

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onTextChanged(index, s ?: "")
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit

    fun bind(uiModel: UiModel) {
        editText.tag = uiModel.index
        editText.removeTextChangedListener(this)
        editText.setText(uiModel.text)
        editText.addTextChangedListener(this)
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
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_description, parent, false),
            onTextChanged = onTextChanged
        )
    }
}