package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.utils.consume

internal class BugReportImageViewHolder private constructor(
    itemView: View,
    onMediaSelected: (String) -> Unit,
    onLongTap: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val fileName get() = itemView.tag as String
    private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
    private val imageView = itemView.findViewById<ImageView>(R.id.beagle_image_view)

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
            load(context.getScreenCapturesFolder().resolve(uiModel.fileName))
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
        ) = BugReportImageViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_gallery_image, parent, false),
            onMediaSelected = onMediaSelected,
            onLongTap = onLongTap
        )
    }
}