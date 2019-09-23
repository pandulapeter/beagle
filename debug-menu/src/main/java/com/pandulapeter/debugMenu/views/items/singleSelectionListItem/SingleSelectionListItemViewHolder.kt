package com.pandulapeter.debugMenu.views.items.singleSelectionListItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class SingleSelectionListItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val radioButton = itemView.findViewById<RadioButton>(R.id.radio_button)

    fun bind(viewModel: SingleSelectionListItemViewModel<*>, textColor: Int) {
        radioButton.text = viewModel.name
        radioButton.setTextColor(textColor)
        radioButton.setOnCheckedChangeListener(null)
        radioButton.isChecked = viewModel.isSelected
        radioButton.setOnCheckedChangeListener { _, isSelected -> if (isSelected) viewModel.invokeItemSelectedCallback() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            SingleSelectionListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_single_selection_list_item, parent, false))
    }
}