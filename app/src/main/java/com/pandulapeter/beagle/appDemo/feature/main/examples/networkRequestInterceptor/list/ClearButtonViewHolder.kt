package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorClearButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class ClearButtonViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorClearButtonBinding,
    onClearButtonPressed: () -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorClearButtonBinding, ClearButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClearButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "clearButton"
    ) : NetworkRequestInterceptorListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onClearButtonPressed: () -> Unit
        ) = ClearButtonViewHolder(
            binding = ItemNetworkRequestInterceptorClearButtonBinding.inflate(parent.inflater, parent, false),
            onClearButtonPressed = onClearButtonPressed
        )
    }
}