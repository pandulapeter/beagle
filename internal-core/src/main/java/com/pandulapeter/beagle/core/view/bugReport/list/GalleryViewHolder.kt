package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportGalleryBinding
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportGalleryAdapter
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportImageViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportVideoViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

internal class GalleryViewHolder private constructor(
    private val binding: BeagleItemBugReportGalleryBinding,
    onMediaSelected: (String) -> Unit,
    onMediaLongTapped: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val galleryAdapter = BugReportGalleryAdapter(
        onMediaSelected = onMediaSelected,
        onLongTap = onMediaLongTapped
    )

    init {
        binding.beagleRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = galleryAdapter
        }
    }

    fun bind(uiModel: UiModel) = galleryAdapter.submitList(uiModel.mediaFileNames.mapNotNull { (fileName, lastModified) ->
        when {
            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> BugReportImageViewHolder.UiModel(
                fileName,
                uiModel.selectedItemIds.contains(fileName),
                lastModified
            )
            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> BugReportVideoViewHolder.UiModel(
                fileName,
                uiModel.selectedItemIds.contains(fileName),
                lastModified
            )
            else -> null
        }
        //TODO: Append items from the device Gallery.
    })

    data class UiModel(
        val mediaFileNames: List<Pair<String, Long>>,
        val selectedItemIds: List<String>
    ) : BugReportListItem {

        override val id = "gallery"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onMediaSelected: (String) -> Unit,
            onMediaLongTapped: (String) -> Unit
        ) = GalleryViewHolder(
            binding = BeagleItemBugReportGalleryBinding.inflate(parent.inflater, parent, false),
            onMediaSelected = onMediaSelected,
            onMediaLongTapped = onMediaLongTapped
        )
    }
}