package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorLoadingIndicatorBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

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
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_request_interceptor_loading_indicator, parent, false)
        )
    }
}