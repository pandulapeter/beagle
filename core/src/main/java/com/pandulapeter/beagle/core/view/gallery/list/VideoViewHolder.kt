package com.pandulapeter.beagle.core.view.gallery.list

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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class VideoViewHolder private constructor(
    itemView: View,
    onMediaSelected: (Int) -> Unit,
    onLongTap: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val imageView = itemView.findViewById<ImageView>(R.id.beagle_image_view)
    private var job: Job? = null

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onMediaSelected(adapterPosition)
                }
            }
        }
        itemView.setOnLongClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onLongTap(adapterPosition)
                }
            }
            true
        }
    }

    fun bind(uiModel: UiModel) {
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
    ) : GalleryListItem {
        override val id = fileName
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onMediaSelected: (Int) -> Unit,
            onLongTap: (Int) -> Unit
        ) = VideoViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_gallery_video, parent, false),
            onMediaSelected = onMediaSelected,
            onLongTap = onLongTap
        )
    }
}