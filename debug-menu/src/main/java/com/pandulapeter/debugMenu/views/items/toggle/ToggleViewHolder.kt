package com.pandulapeter.debugMenu.views.items.toggle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class ToggleViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val switch = itemView.findViewById<SwitchCompat>(R.id.toggle)

    fun bind(viewModel: ToggleViewModel) {
        switch.text = viewModel.title
        switch.isChecked = viewModel.isEnabled
        switch.setOnCheckedChangeListener { _, isChecked -> viewModel.onToggleStateChanged(isChecked) }
    }

    companion object {
        fun create(parent: ViewGroup) = ToggleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_toggle, parent, false))
    }
}