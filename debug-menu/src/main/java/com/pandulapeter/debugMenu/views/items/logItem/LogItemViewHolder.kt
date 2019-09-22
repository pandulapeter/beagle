package com.pandulapeter.debugMenu.views.items.logItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.visible

internal class LogItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val messageTextView = itemView.findViewById<TextView>(R.id.message)
    private val timestampTextView = itemView.findViewById<TextView>(R.id.timestamp)

    fun bind(viewModel: LogItemViewModel, textColor: Int) {
        val isClickable = viewModel.logItem.payload != null
        itemView.isClickable = isClickable
        messageTextView.text = viewModel.message + if (viewModel.hasPayload) " *" else ""
        messageTextView.setTextColor(textColor)
        timestampTextView.setTextColor(textColor)
        timestampTextView.text = viewModel.timestamp
        timestampTextView.visible = viewModel.timestamp != null
        if (isClickable) {
            itemView.setOnClickListener { viewModel.onItemSelected() }
        } else {
            itemView.setOnClickListener(null)
        }
    }

    companion object {
        fun create(parent: ViewGroup) =
            LogItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log_message, parent, false))
    }
}