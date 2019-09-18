package com.pandulapeter.debugMenu.views.items.header

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.visible

internal class HeaderViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val subtitleTextView = itemView.findViewById<TextView>(R.id.subtitle)
    private val buildTimeTextView = itemView.findViewById<TextView>(R.id.build_time)
    private val hintTextView = itemView.findViewById<TextView>(R.id.hint)

    fun bind(viewModel: HeaderViewModel, textColor: Int) {
        titleTextView.setUpWithText(viewModel.title, textColor)
        subtitleTextView.setUpWithText(viewModel.subtitle, textColor)
        buildTimeTextView.setUpWithText(viewModel.buildTime, textColor)
        hintTextView.setUpWithText(viewModel.text, textColor)
    }

    private fun TextView.setUpWithText(content: String?, textColor: Int) {
        visible = content != null
        text = content
        setTextColor(textColor)
    }

    companion object {
        fun create(parent: ViewGroup) = HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
    }
}