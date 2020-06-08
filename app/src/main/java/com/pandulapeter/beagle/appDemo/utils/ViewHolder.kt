package com.pandulapeter.beagle.appDemo.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.BR

abstract class ViewHolder<B : ViewDataBinding, M : ListItem>(protected val binding: B) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: M) = binding.run {
        setVariable(BR.uiModel, uiModel)
        executePendingBindings()
    }
}