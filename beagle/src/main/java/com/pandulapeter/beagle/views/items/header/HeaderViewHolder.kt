package com.pandulapeter.beagle.views.items.header

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.visibleOrGone

internal class HeaderViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val subtitleTextView = itemView.findViewById<TextView>(R.id.subtitle)
    private val textTextView = itemView.findViewById<TextView>(R.id.text)

    fun bind(viewModel: HeaderViewModel) {
        titleTextView.setUpWithText(viewModel.title)
        subtitleTextView.setUpWithText(viewModel.subtitle)
        textTextView.setUpWithText(viewModel.text)
    }

    private fun TextView.setUpWithText(content: CharSequence?) {
        visibleOrGone = content != null
        text = content
    }

    companion object {
        fun create(parent: ViewGroup) = HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
    }
}