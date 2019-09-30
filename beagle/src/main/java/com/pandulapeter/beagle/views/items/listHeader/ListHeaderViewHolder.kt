package com.pandulapeter.beagle.views.items.listHeader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.animatedDrawable
import com.pandulapeter.beagle.utils.visible

internal class ListHeaderViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private val drawableExpand by lazy { itemView.context.animatedDrawable(R.drawable.avd_expand) }
    private val drawableCollapse by lazy { itemView.context.animatedDrawable(R.drawable.avd_collapse) }
    private var isExpanded: Boolean? = null

    fun bind(viewModel: ListHeaderViewModel) {
        titleTextView.text = viewModel.title
        iconImageView.setImageDrawable((if (viewModel.isExpanded) drawableExpand else drawableCollapse)?.apply {
            setTintList(titleTextView.textColors)
            if (viewModel.isExpanded != isExpanded) {
                isExpanded = viewModel.isExpanded
                start()
            }
        })
        iconImageView.visible = viewModel.shouldShowIcon
        if (viewModel.shouldShowIcon) {
            itemView.setOnClickListener { viewModel.onItemSelected() }
        } else {
            itemView.setOnClickListener(null)
        }
        itemView.isClickable = viewModel.shouldShowIcon
    }

    companion object {
        fun create(parent: ViewGroup) =
            ListHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_header, parent, false))
    }
}