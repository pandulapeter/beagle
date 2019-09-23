package com.pandulapeter.debugMenu.views.items.text

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class TextViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val button = itemView.findViewById<TextView>(R.id.text)

    fun bind(viewModel: TextViewModel, textColor: Int) {
        button.text = viewModel.text
        button.setTypeface(button.typeface, if (viewModel.isTitle) Typeface.BOLD else Typeface.NORMAL)
        button.setTextColor(textColor)
    }

    companion object {
        fun create(parent: ViewGroup) =
            TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
    }
}