package com.pandulapeter.beagle.views.items.multipleSelectionListItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class MultipleSelectionListItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)

    fun bind(viewModel: MultipleSelectionListItemViewModel<*>) {
        checkbox.text = viewModel.name
        checkbox.setOnCheckedChangeListener(null)
        checkbox.isChecked = viewModel.isSelected
        checkbox.setOnCheckedChangeListener { _, _ -> viewModel.invokeItemSelectedCallback() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            MultipleSelectionListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_selection_list_item, parent, false))
    }
}