package com.pandulapeter.beagle.views.drawerItems.padding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension

internal class PaddingViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val paddingView = itemView.findViewById<View>(R.id.padding)
    private val defaultPadding = itemView.context.dimension(R.dimen.beagle_content_padding)

    fun bind(viewModel: PaddingViewModel) {
        paddingView.layoutParams = (paddingView.layoutParams as FrameLayout.LayoutParams).apply {
            height = viewModel.size ?: defaultPadding
        }
    }

    companion object {
        fun create(parent: ViewGroup) =
            PaddingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_padding, parent, false))
    }
}