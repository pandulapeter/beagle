package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.utils.consume
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class BugReportVideoViewHolder private constructor(
    itemView: View,
    onMediaSelected: (String) -> Unit,
    onLongTap: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val fileName get() = itemView.tag as String
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val imageView = itemView.findViewById<ImageView>(R.id.beagle_image_view)
    private var job: Job? = null

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onMediaSelected(fileName)
            }
        }
        itemView.setOnLongClickListener {
            consume {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onLongTap(fileName)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.fileName
        textView.text = uiModel.fileName
        imageView.run {
            job?.cancel()
            job = GlobalScope.launch {
                BeagleCore.implementation.videoThumbnailLoader.execute(
                    ImageRequest.Builder(context)
                        .data(context.getScreenCapturesFolder().resolve(uiModel.fileName))
                        .target(this@run)
                        .build()
                )
            }
        }
        itemView.scaleX = if (uiModel.isSelected) 0.8f else 1f
        itemView.scaleY = itemView.scaleX
    }

    data class UiModel(
        val fileName: String,
        val isSelected: Boolean,
        override val lastModified: Long
    ) : BugReportGalleryListItem {
        override val id = fileName
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onMediaSelected: (String) -> Unit,
            onLongTap: (String) -> Unit
        ) = BugReportVideoViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_gallery_video, parent, false),
            onMediaSelected = onMediaSelected,
            onLongTap = onLongTap
        )
    }
}