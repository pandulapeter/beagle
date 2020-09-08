package com.pandulapeter.beagle.core.view.gallery.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R

internal class SectionHeaderViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)

    fun bind(uiModel: UiModel) {
        textView.text = BeagleCore.implementation.appearance.galleryTimestampFormatter?.invoke(uiModel.timestamp) ?: ""
    }

    data class UiModel(
        val timestamp: Long
    ) : GalleryListItem {
        override val id = "sectionHeader_$timestamp"
        override val lastModified = timestamp
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = SectionHeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_gallery_section_header, parent, false)
        )
    }
}