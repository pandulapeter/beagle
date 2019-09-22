package com.pandulapeter.debugMenu.views.items.settingsLink

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class SettingsLinkViewHolder(root: View, onButtonClicked: () -> Unit) : RecyclerView.ViewHolder(root) {

    private val button = itemView.findViewById<TextView>(R.id.button)

    init {
        button.setOnClickListener { onButtonClicked() }
    }

    fun bind(viewModel: SettingsLinkViewModel, textColor: Int) {
        button.text = viewModel.text
        button.setTextColor(textColor)
    }

    companion object {
        fun create(parent: ViewGroup, onButtonClicked: () -> Unit) =
            SettingsLinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false), onButtonClicked)
    }
}