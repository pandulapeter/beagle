package com.pandulapeter.debugMenu.views.items.authenticationHelperHeader

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.utils.animatedDrawable

internal class AuthenticationHelperHeaderViewHolder(root: View, onItemClicked: () -> Unit) : RecyclerView.ViewHolder(root) {

    private val titleTextView = itemView.findViewById<TextView>(R.id.title)
    private val iconImageView = itemView.findViewById<ImageView>(R.id.icon)
    private val drawableExpand by lazy { itemView.context.animatedDrawable(R.drawable.avd_expand) }
    private val drawableCollapse by lazy { itemView.context.animatedDrawable(R.drawable.avd_collapse) }

    init {
        itemView.setOnClickListener { onItemClicked() }
    }

    fun bind(viewModel: AuthenticationHelperHeaderViewModel, textColor: Int) {
        titleTextView.text = viewModel.title
        titleTextView.setTextColor(textColor)
        iconImageView.setImageDrawable((if (viewModel.isExpanded) drawableExpand else drawableCollapse)?.apply {
            setTintList(ColorStateList.valueOf(textColor))
            start()
        })
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: () -> Unit) =
            AuthenticationHelperHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_authentication_helper_header, parent, false), onItemClicked)
    }
}