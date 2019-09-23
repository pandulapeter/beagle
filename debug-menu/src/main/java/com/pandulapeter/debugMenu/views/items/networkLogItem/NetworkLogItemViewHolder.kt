package com.pandulapeter.debugMenu.views.items.networkLogItem

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.visible

internal class NetworkLogItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private val urlTextView = itemView.findViewById<TextView>(R.id.url)
    private val timestampTextView = itemView.findViewById<TextView>(R.id.timestamp)

    fun bind(viewModel: NetworkLogItemViewModel, textColor: Int) {
        iconImageView.setImageResource(if (viewModel.isOutgoing) R.drawable.ic_outgoing else R.drawable.ic_incoming)
        ImageViewCompat.setImageTintList(iconImageView, ColorStateList.valueOf(textColor))
        urlTextView.text = viewModel.url
        urlTextView.setTextColor(textColor)
        timestampTextView.setTextColor(textColor)
        timestampTextView.text = viewModel.timestamp
        timestampTextView.visible = viewModel.timestamp != null
        itemView.setOnClickListener { viewModel.onItemSelected() }
    }

    companion object {
        fun create(parent: ViewGroup) =
            NetworkLogItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_network_log, parent, false))
    }
}