package com.pandulapeter.beagle.views.drawerItems.text

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class TextViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val textView = itemView.findViewById<TextView>(R.id.text)

    fun bind(viewModel: TextViewModel) {
        textView.text = viewModel.text
        textView.setTypeface(textView.typeface, if (viewModel.isTitle) Typeface.BOLD else Typeface.NORMAL)
    }

    companion object {
        fun create(parent: ViewGroup) =
            TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_text, parent, false))
    }
}