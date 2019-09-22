package com.pandulapeter.debugMenu.views.items.expandCollapseHeader

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.animatedDrawable
import com.pandulapeter.debugMenu.utils.visible

internal class ExpandCollapseHeaderViewHolder(root: View, onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private val drawableExpand by lazy { itemView.context.animatedDrawable(R.drawable.avd_expand) }
    private val drawableCollapse by lazy { itemView.context.animatedDrawable(R.drawable.avd_collapse) }

    init {
        itemView.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked(adapterPosition)
                }
            }
        }
    }

    fun bind(viewModel: ExpandCollapseHeaderViewModel, textColor: Int) {
        itemView.isClickable = viewModel.shouldShowIcon
        titleTextView.text = viewModel.title
        titleTextView.setTextColor(textColor)
        iconImageView.setImageDrawable((if (viewModel.isExpanded) drawableExpand else drawableCollapse)?.apply {
            setTintList(ColorStateList.valueOf(textColor))
            start()
        })
        iconImageView.visible = viewModel.shouldShowIcon
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: (position: Int) -> Unit) =
            ExpandCollapseHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_expand_collapse_header, parent, false), onItemClicked)
    }
}