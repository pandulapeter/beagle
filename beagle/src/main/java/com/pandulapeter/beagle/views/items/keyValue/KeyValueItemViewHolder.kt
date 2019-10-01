package com.pandulapeter.beagle.views.items.keyValue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class KeyValueItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val keyTextView = itemView.findViewById<TextView>(R.id.key)
    private val valueTextView = itemView.findViewById<TextView>(R.id.value)

    fun bind(viewModel: KeyValueItemViewModel) {
        keyTextView.text = viewModel.key
        valueTextView.text = viewModel.value
    }

    companion object {
        fun create(parent: ViewGroup) =
            KeyValueItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_key_value, parent, false))
    }
}