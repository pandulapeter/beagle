package com.pandulapeter.debugMenu.views.items.logging

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R

internal class LoggingHeaderViewHolder(root: View, onItemClicked: () -> Unit) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)

    init {
        itemView.setOnClickListener { onItemClicked() }
    }

    fun bind(viewModel: LoggingHeaderViewModel, textColor: Int) {
        titleTextView.text = viewModel.title
        titleTextView.setTextColor(textColor)
        iconImageView.setImageResource(if (viewModel.isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand)
        ImageViewCompat.setImageTintList(iconImageView, ColorStateList.valueOf(textColor))
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: () -> Unit) =
            LoggingHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_logging_header, parent, false), onItemClicked)
    }
}