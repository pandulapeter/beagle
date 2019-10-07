package com.pandulapeter.beagle.views.drawerItems.button

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class ButtonListItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val button = itemView.findViewById<TextView>(R.id.button)

    fun bind(viewModel: ButtonViewModel) {
        button.text = viewModel.text
        itemView.setOnClickListener { viewModel.onButtonPressed() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            ButtonListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_button_list_item, parent, false))
    }
}