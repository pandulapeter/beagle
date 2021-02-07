package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorLoadingIndicatorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class LoadingIndicatorViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorLoadingIndicatorBinding
) : BaseViewHolder<ItemNetworkRequestInterceptorLoadingIndicatorBinding, LoadingIndicatorViewHolder.UiModel>(binding) {

    data class UiModel(
        override val id: String = "loadingIndicator"
    ) : NetworkRequestInterceptorListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = LoadingIndicatorViewHolder(
            binding = ItemNetworkRequestInterceptorLoadingIndicatorBinding.inflate(parent.inflater, parent, false)
        )
    }
}