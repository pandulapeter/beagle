package com.pandulapeter.beagle.core.view.gallery.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemGalleryVideoBinding
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class VideoViewHolder private constructor(
    private val binding: BeagleItemGalleryVideoBinding,
    onTap: (Int) -> Unit,
    onLongTap: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var job: Job? = null
    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        adapterPosition.let { adapterPosition ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(adapterPosition)
            }
        }
    }

    init {
        binding.root.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onTap(adapterPosition)
                }
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                adapterPosition.let { adapterPosition ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onLongTap(adapterPosition)
                    }
                }
            }
        }
    }

    fun bind(uiModel: UiModel) = binding.beagleMediaView.run {
        textView.text = uiModel.fileName
        imageView.run {
            job?.cancel()
            job = GlobalScope.launch {
                BeagleCore.implementation.videoThumbnailLoader.execute(
                    ImageRequest.Builder(context)
                        .data(context.getScreenCapturesFolder().resolve(uiModel.fileName))
                        .target(binding.beagleMediaView.imageView)
                        .build()
                )
            }
        }
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = uiModel.isSelected
        checkBox.setOnCheckedChangeListener(onCheckChangeListener)
        checkBox.visible = uiModel.isInSelectionMode
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
            binding = BeagleItemGalleryVideoBinding.inflate(parent.inflater, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}