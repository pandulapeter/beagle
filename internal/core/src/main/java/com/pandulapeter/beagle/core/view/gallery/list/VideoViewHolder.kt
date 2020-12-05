package com.pandulapeter.beagle.core.view.gallery.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.view.MediaView
import com.pandulapeter.beagle.utils.consume
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class VideoViewHolder private constructor(
    itemView: View,
    onTap: (Int) -> Unit,
    onLongTap: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val mediaView = itemView.findViewById<MediaView>(R.id.beagle_media_view)
    private var job: Job? = null
    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        adapterPosition.let { adapterPosition ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(adapterPosition)
            }
        }
    }

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onTap(adapterPosition)
                }
            }
        }
        itemView.setOnLongClickListener {
            consume {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onLongTap(adapterPosition)
                    }
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        mediaView.textView.text = uiModel.fileName
        mediaView.imageView.run {
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
        mediaView.checkBox.setOnCheckedChangeListener(null)
        mediaView.checkBox.isChecked = uiModel.isSelected
        mediaView.checkBox.setOnCheckedChangeListener(onCheckChangeListener)
        mediaView.checkBox.visible = uiModel.isInSelectionMode
    }

    data class UiModel(
        val fileName: String,
        val isSelected: Boolean,
        val isInSelectionMode: Boolean,
        override val lastModified: Long
    ) : GalleryListItem {
        override val id = fileName
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTap: (Int) -> Unit,
            onLongTap: (Int) -> Unit
        ) = VideoViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_gallery_video, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}