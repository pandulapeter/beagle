package com.pandulapeter.debugMenu.views.items.keylineOverlay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class KeylineOverlayViewHolder(root: View, private val onSwitchStateChanged: (isEnabled: Boolean) -> Unit) : RecyclerView.ViewHolder(root) {

    private val switch = itemView.findViewById<SwitchCompat>(R.id.keyline_overlay_switch)

    fun bind(viewModel: KeylineOverlayViewModel, textColor: Int) {
        switch.text = viewModel.title
        switch.setTextColor(textColor)
        switch.isChecked = viewModel.isEnabled
        switch.setOnCheckedChangeListener { _, isChecked -> onSwitchStateChanged(isChecked) }
    }

    companion object {
        fun create(parent: ViewGroup, onSwitchStateChanged: (isEnabled: Boolean) -> Unit) =
            KeylineOverlayViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_switch, parent, false), onSwitchStateChanged)
    }
}