package com.pandulapeter.beagle.core.view.gallery.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemGallerySectionHeaderBinding
import com.pandulapeter.beagle.utils.extensions.inflater

internal class SectionHeaderViewHolder private constructor(
    private val binding: BeagleItemGallerySectionHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: UiModel) {
        binding.beagleTextView.text = BeagleCore.implementation.appearance.galleryTimestampFormatter?.invoke(uiModel.timestamp) ?: ""
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
            BeagleItemGallerySectionHeaderBinding.inflate(parent.inflater, parent, false)
        )
    }
}