package com.pandulapeter.debugMenu.views.items.logMessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.visible

internal class LogMessageViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val messageTextView = itemView.findViewById<TextView>(R.id.message)
    private val timestampTextView = itemView.findViewById<TextView>(R.id.timestamp)

    fun bind(viewModel: LogMessageViewModel, textColor: Int) {
        messageTextView.text = viewModel.message
        messageTextView.setTextColor(textColor)
        timestampTextView.setTextColor(textColor)
        timestampTextView.text = viewModel.timestamp
        timestampTextView.visible = viewModel.timestamp != null
    }

    companion object {
        fun create(parent: ViewGroup) =
            LogMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log_message, parent, false))
    }
}