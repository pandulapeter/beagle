package com.pandulapeter.beagle.views.drawerItems.singleSelectionListItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class SingleSelectionListItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val radioButton = itemView.findViewById<RadioButton>(R.id.radio_button)

    fun bind(viewModel: SingleSelectionListItemViewModel<*>) {
        radioButton.text = viewModel.name
        radioButton.setOnCheckedChangeListener(null)
        radioButton.isChecked = viewModel.isSelected
        radioButton.setOnCheckedChangeListener { _, isSelected -> if (isSelected) viewModel.invokeItemSelectedCallback() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            SingleSelectionListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_single_selection_list_item, parent, false))
    }
}