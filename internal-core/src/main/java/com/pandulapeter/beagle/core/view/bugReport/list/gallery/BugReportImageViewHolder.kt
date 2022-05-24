package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pandulapeter.beagle.core.databinding.BeagleItemBugReportGalleryImageBinding
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class BugReportImageViewHolder private constructor(
    private val binding: BeagleItemBugReportGalleryImageBinding,
    onTap: (String) -> Unit,
    onLongTap: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val fileName get() = itemView.tag as String
    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        bindingAdapterPosition.let { bindingAdapterPosition ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(fileName)
            }
        }
    }

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onTap(fileName)
            }
        }
        binding.root.setOnLongClickListener {
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
            imageView.run { load(context.getScreenCapturesFolder().resolve(uiModel.fileName)) }
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
        ) = BugReportImageViewHolder(
            binding = BeagleItemBugReportGalleryImageBinding.inflate(parent.inflater, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}