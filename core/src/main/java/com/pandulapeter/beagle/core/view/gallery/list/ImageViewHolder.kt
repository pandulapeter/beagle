package com.pandulapeter.beagle.core.view.gallery.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.isScaledDown
import com.pandulapeter.beagle.utils.consume

internal class ImageViewHolder private constructor(
    itemView: View,
    onMediaSelected: (Int) -> Unit,
    onLongTap: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val imageView = itemView.findViewById<ImageView>(R.id.beagle_image_view)
    private val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.beagle_constraint_layout)

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onMediaSelected(adapterPosition)
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
        textView.text = uiModel.fileName
        imageView.run { load(context.getScreenCapturesFolder().resolve(uiModel.fileName)) }
        constraintLayout.isScaledDown = uiModel.isSelected
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
        ) = ImageViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_gallery_image, parent, false),
            onMediaSelected = onMediaSelected,
            onLongTap = onLongTap
        )
    }
}