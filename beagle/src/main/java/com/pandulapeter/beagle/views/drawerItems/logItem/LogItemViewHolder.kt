package com.pandulapeter.beagle.views.drawerItems.logItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.visibleOrGone

internal class LogItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val messageTextView = itemView.findViewById<TextView>(R.id.message)
    private val timestampTextView = itemView.findViewById<TextView>(R.id.timestamp)

    @SuppressLint("SetTextI18n")
    fun bind(viewModel: LogItemViewModel) {
        val isClickable = viewModel.logItem.payload != null
        messageTextView.text = viewModel.message + if (viewModel.hasPayload) " *" else ""
        timestampTextView.text = viewModel.timestamp
        timestampTextView.visibleOrGone = viewModel.timestamp != null
        if (isClickable) {
            itemView.setOnClickListener { viewModel.onItemSelected() }
        } else {
            itemView.setOnClickListener(null)
        }
        itemView.isClickable = isClickable
    }

    companion object {
        fun create(parent: ViewGroup) =
            LogItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_log, parent, false))
    }
}