package com.pandulapeter.debugMenu.views.items.listItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class ListItemViewHolder(root: View, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(root) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.name)

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked(adapterPosition)
                }
            }
        }
    }

    fun bind(viewModel: ListItemViewModel, textColor: Int) {
        nameTextView.text = viewModel.name
        nameTextView.setTextColor(textColor)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
            ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false), onItemClicked)
    }
}