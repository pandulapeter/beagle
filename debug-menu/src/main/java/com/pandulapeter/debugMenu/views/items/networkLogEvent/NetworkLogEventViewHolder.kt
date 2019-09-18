package com.pandulapeter.debugMenu.views.items.networkLogEvent

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

internal class NetworkLogEventViewHolder(root: View, onNetworkLogEventClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(root) {

    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private val urlTextView = itemView.findViewById<TextView>(R.id.url)
    private val timestampTextView = itemView.findViewById<TextView>(R.id.timestamp)

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onNetworkLogEventClicked(adapterPosition)
                }
            }
        }
    }

    fun bind(viewModel: NetworkLogEventViewModel, textColor: Int) {
        iconImageView.setImageResource(if (viewModel.isOutgoing) R.drawable.ic_outgoing else R.drawable.ic_incoming)
        ImageViewCompat.setImageTintList(iconImageView, ColorStateList.valueOf(textColor))
        urlTextView.text = viewModel.url
        urlTextView.setTextColor(textColor)
        timestampTextView.setTextColor(textColor)
        timestampTextView.text = viewModel.timestamp
        timestampTextView.visible = viewModel.timestamp != null
    }

    companion object {
        fun create(parent: ViewGroup, onNetworkLogEventClicked: (position: Int) -> Unit) =
            NetworkLogEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_network_log_event, parent, false), onNetworkLogEventClicked)
    }
}