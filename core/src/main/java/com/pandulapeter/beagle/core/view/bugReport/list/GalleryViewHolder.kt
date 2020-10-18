package com.pandulapeter.beagle.core.view.bugReport.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.toGalleryModels
import com.pandulapeter.beagle.core.view.gallery.list.GalleryAdapter
import java.io.File

internal class GalleryViewHolder private constructor(
    itemView: View,
    spanCount: Int,
    onMediaSelected: (Int) -> Unit,
    onMediaLongTapped: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val galleryAdapter = GalleryAdapter(
        onMediaSelected = onMediaSelected,
        onLongTap = onMediaLongTapped
    )

    init {
        itemView.findViewById<RecyclerView>(R.id.beagle_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
            adapter = galleryAdapter
        }
    }

    fun bind(uiModel: UiModel) = galleryAdapter.submitList(uiModel.mediaFiles.toGalleryModels(uiModel.selectedItemIds))

    data class UiModel(
        val mediaFiles: List<File>,
        val selectedItemIds: List<String>
    ) : BugReportListItem {

        override val id = "gallery"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            spanCount: Int,
            onMediaSelected: (Int) -> Unit,
            onMediaLongTapped: (Int) -> Unit
        ) = GalleryViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_gallery, parent, false),
            spanCount = spanCount,
            onMediaSelected = onMediaSelected,
            onMediaLongTapped = onMediaLongTapped
        )
    }
}