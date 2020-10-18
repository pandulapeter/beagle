package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportGalleryAdapter
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportImageViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.gallery.BugReportVideoViewHolder
import java.io.File

internal class GalleryViewHolder private constructor(
    itemView: View,
    onMediaSelected: (Int) -> Unit,
    onMediaLongTapped: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val galleryAdapter = BugReportGalleryAdapter(
        onMediaSelected = onMediaSelected,
        onLongTap = onMediaLongTapped
    )

    init {
        itemView.findViewById<RecyclerView>(R.id.beagle_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = galleryAdapter
        }
    }

    fun bind(uiModel: UiModel) = galleryAdapter.submitList(uiModel.mediaFiles.mapNotNull { file ->
        file.name.let { fileName ->
            when {
                fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> BugReportImageViewHolder.UiModel(
                    fileName,
                    uiModel.selectedItemIds.contains(fileName),
                    file.lastModified()
                )
                fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> BugReportVideoViewHolder.UiModel(
                    fileName,
                    uiModel.selectedItemIds.contains(fileName),
                    file.lastModified()
                )
                else -> null
            }
        }
        //TODO: Append an item that opens the device Gallery.
    })

    data class UiModel(
        val mediaFiles: List<File>,
        val selectedItemIds: List<String>
    ) : BugReportListItem {

        override val id = "gallery"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onMediaSelected: (Int) -> Unit,
            onMediaLongTapped: (Int) -> Unit
        ) = GalleryViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_gallery, parent, false),
            onMediaSelected = onMediaSelected,
            onMediaLongTapped = onMediaLongTapped
        )
    }
}