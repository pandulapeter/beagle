package com.pandulapeter.beagle.views.drawerItems.listHeader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.animatedDrawable
import com.pandulapeter.beagle.utils.visibleOrInvisible

internal class ListHeaderViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private var isExpanded: Boolean? = null

    fun bind(viewModel: ListHeaderViewModel) {
        titleTextView.text = viewModel.title
        if (viewModel.isExpanded != isExpanded) {
            isExpanded = viewModel.isExpanded
            iconImageView.setImageDrawable((itemView.context.animatedDrawable(if (viewModel.isExpanded) R.drawable.beagle_avd_expand else R.drawable.beagle_avd_collapse))?.apply {
                setTintList(titleTextView.textColors)
                start()
            })
        }
        iconImageView.visibleOrInvisible = viewModel.shouldShowIcon
        if (viewModel.shouldShowIcon) {
            itemView.setOnClickListener { if (bindingAdapterPosition != RecyclerView.NO_POSITION) viewModel.onItemSelected() }
        } else {
            itemView.setOnClickListener(null)
        }
        itemView.isClickable = viewModel.shouldShowIcon
    }

    companion object {
        fun create(parent: ViewGroup) =
            ListHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.beagle_item_list_header, parent, false))
    }
}