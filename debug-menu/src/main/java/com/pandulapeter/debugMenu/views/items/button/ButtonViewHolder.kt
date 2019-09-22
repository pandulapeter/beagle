package com.pandulapeter.debugMenu.views.items.button

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class ButtonViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val button = itemView.findViewById<TextView>(R.id.button)

    fun bind(viewModel: ButtonViewModel, textColor: Int) {
        button.text = viewModel.text
        button.setTextColor(textColor)
        button.setOnClickListener { viewModel.onButtonPressed() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            ButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false))
    }
}