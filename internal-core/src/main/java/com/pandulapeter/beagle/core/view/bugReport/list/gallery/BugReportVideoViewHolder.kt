package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportGalleryVideoBinding
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class BugReportVideoViewHolder private constructor(
    private val binding: BeagleItemBugReportGalleryVideoBinding,
    onTap: (String) -> Unit,
    onLongTap: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val fileName get() = itemView.tag as String
    private var job: Job? = null
    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        bindingAdapterPosition.let { bindingAdapterPosition ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(fileName)
            }
        }
    }

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onTap(fileName)
            }
        }
        itemView.setOnLongClickListener {
            consume {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onLongTap(fileName)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) {
        itemView.tag = uiModel.fileName
        binding.beagleMediaView.run {
            textView.text = uiModel.fileName
            imageView.run {
                job?.cancel()
                job = GlobalScope.launch {
                    BeagleCore.implementation.videoThumbnailLoader.execute(
                        ImageRequest.Builder(context)
                            .data(context.getScreenCapturesFolder().resolve(uiModel.fileName))
                            .target(imageView)
                            .build()
                    )
                }
            }
            checkBox.run {
                setOnCheckedChangeListener(null)
                isChecked = uiModel.isSelected
                setOnCheckedChangeListener(onCheckChangeListener)
            }
        }
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
            onTap: (String) -> Unit,
            onLongTap: (String) -> Unit
        ) = BugReportVideoViewHolder(
            binding = BeagleItemBugReportGalleryVideoBinding.inflate(parent.inflater, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}