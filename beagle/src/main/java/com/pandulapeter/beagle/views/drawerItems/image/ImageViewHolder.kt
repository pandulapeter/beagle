package com.pandulapeter.beagle.views.drawerItems.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class ImageViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val imageView = itemView.findViewById<ImageView>(R.id.image)

    fun bind(viewModel: ImageViewModel) {
        imageView.setImageDrawable(viewModel.drawable)
    }

    companion object {
        fun create(parent: ViewGroup) =
            ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
    }
}