package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.appDemo.R

abstract class BaseAdapter<T : ListItem> : ListAdapter<T, BaseViewHolder<*, *>>(ListItem.DiffCallback()) {

    @CallSuper
    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TextViewHolder.UiModel -> R.layout.item_text
        is CodeSnippetViewHolder.UiModel -> R.layout.item_code_snippet
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_text -> TextViewHolder.create(parent)
        R.layout.item_code_snippet -> CodeSnippetViewHolder.create(parent)
        else -> throw  IllegalArgumentException("Unsupported item view type: $viewType.")
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CodeSnippetViewHolder -> holder.bind(getItem(position) as CodeSnippetViewHolder.UiModel)
        is TextViewHolder -> holder.bind(getItem(position) as TextViewHolder.UiModel)
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }
}