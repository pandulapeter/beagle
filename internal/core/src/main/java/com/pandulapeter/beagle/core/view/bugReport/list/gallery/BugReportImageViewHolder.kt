package com.pandulapeter.beagle.core.view.bugReport.list.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.view.MediaView
import com.pandulapeter.beagle.utils.consume

internal class BugReportImageViewHolder private constructor(
    itemView: View,
    onTap: (String) -> Unit,
    onLongTap: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val fileName get() = itemView.tag as String
    private val mediaView = itemView.findViewById<MediaView>(R.id.beagle_media_view)
    private val onCheckChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
        adapterPosition.let { adapterPosition ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongTap(fileName)
            }
        }
    }

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onTap(fileName)
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
        mediaView.textView.text = uiModel.fileName
        mediaView.imageView.run { load(context.getScreenCapturesFolder().resolve(uiModel.fileName)) }
        mediaView.checkBox.setOnCheckedChangeListener(null)
        mediaView.checkBox.isChecked = uiModel.isSelected
        mediaView.checkBox.setOnCheckedChangeListener(onCheckChangeListener)
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
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_bug_report_gallery_image, parent, false),
            onTap = onTap,
            onLongTap = onLongTap
        )
    }
}