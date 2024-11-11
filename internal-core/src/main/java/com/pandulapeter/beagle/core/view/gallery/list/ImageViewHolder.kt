package com.pandulapeter.beagle.core.view.gallery.list

import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.pandulapeter.beagle.core.databinding.BeagleItemGalleryImageBinding
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

internal class ImageViewHolder private constructor(
    private val binding: BeagleItemGalleryImageBinding,
    onTap: (Int) -> Unit,
    onLongTap: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        bindingAdapterPosition.let { bindingAdapterPosition ->
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(bindingAdapterPosition)
            }
        }
    }

    init {
        binding.root.setOnClickListener {
            bindingAdapterPosition.let { bindingAdapterPosition ->
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onTap(bindingAdapterPosition)
                }
            }
        }
        binding.root.setOnLongClickListener {
            consume {
                bindingAdapterPosition.let { bindingAdapterPosition ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        onLongTap(bindingAdapterPosition)
                    }
                }
            }
        }
    }

    fun bind(uiModel: UiModel) = binding.beagleMediaView.run {
        textView.text = uiModel.fileName
        imageView.run { load(context.getScreenCapturesFolder().resolve(uiModel.fileName)) }
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
        ) = ImageViewHolder(
            binding = BeagleItemGalleryImageBinding.inflate(parent.inflater, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}