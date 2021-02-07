package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorErrorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class ErrorViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorErrorBinding,
    onTryAgainButtonPressed: () -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorErrorBinding, ErrorViewHolder.UiModel>(binding) {

    init {
        binding.tryAgainButton.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onTryAgainButtonPressed()
            }
        }
    }

    data class UiModel(
        override val id: String = "error"
    ) : NetworkRequestInterceptorListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onTryAgainButtonPressed: () -> Unit
        ) = ErrorViewHolder(
            binding = ItemNetworkRequestInterceptorErrorBinding.inflate(parent.inflater, parent, false),
            onTryAgainButtonPressed = onTryAgainButtonPressed
        )
    }
}