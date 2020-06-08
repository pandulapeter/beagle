package com.pandulapeter.beagle.appDemo.utils

import androidx.recyclerview.widget.DiffUtil

interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    class DiffCallback<T : ListItem> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem

        override fun getChangePayload(oldItem: T, newItem: T) = ""
    }
}