package com.pandulapeter.beagle.views.drawerItems.longText

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class LongTextViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val textView = itemView.findViewById<TextView>(R.id.text)

    fun bind(viewModel: LongTextViewModel) {
        textView.text = viewModel.text
    }

    companion object {
        fun create(parent: ViewGroup) =
            LongTextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_long_text, parent, false))
    }
}