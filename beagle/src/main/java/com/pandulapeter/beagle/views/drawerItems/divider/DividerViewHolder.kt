package com.pandulapeter.beagle.views.drawerItems.divider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class DividerViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    companion object {
        fun create(parent: ViewGroup) =
            DividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_divider, parent, false))
    }
}