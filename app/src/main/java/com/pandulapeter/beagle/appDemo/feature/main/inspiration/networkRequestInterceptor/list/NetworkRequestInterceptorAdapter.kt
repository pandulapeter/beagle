package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class NetworkRequestInterceptorAdapter(
    scope: CoroutineScope,
    private val onTryAgainButtonPressed: () -> Unit,
    private val onSongSelected: (RadioButtonViewHolder.UiModel) -> Unit,
    private val onClearLogsButtonPressed: () -> Unit
) : BaseAdapter<NetworkRequestInterceptorListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is RadioButtonViewHolder.UiModel -> R.layout.item_network_request_interceptor_radio_button
        is LoadingIndicatorViewHolder.UiModel -> R.layout.item_network_request_interceptor_loading_indicator
        is ErrorViewHolder.UiModel -> R.layout.item_network_request_interceptor_error
        is SongLyricsViewHolder.UiModel -> R.layout.item_network_request_interceptor_song_lyrics
        is ClearButtonViewHolder.UiModel -> R.layout.item_network_request_interceptor_clear_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_network_request_interceptor_radio_button -> RadioButtonViewHolder.create(parent, onSongSelected)
        R.layout.item_network_request_interceptor_loading_indicator -> LoadingIndicatorViewHolder.create(parent)
        R.layout.item_network_request_interceptor_error -> ErrorViewHolder.create(parent, onTryAgainButtonPressed)
        R.layout.item_network_request_interceptor_song_lyrics -> SongLyricsViewHolder.create(parent)
        R.layout.item_network_request_interceptor_clear_button -> ClearButtonViewHolder.create(parent, onClearLogsButtonPressed)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is RadioButtonViewHolder -> holder.bind(getItem(position) as RadioButtonViewHolder.UiModel)
        is LoadingIndicatorViewHolder -> holder.bind(getItem(position) as LoadingIndicatorViewHolder.UiModel)
        is ErrorViewHolder -> holder.bind(getItem(position) as ErrorViewHolder.UiModel)
        is SongLyricsViewHolder -> holder.bind(getItem(position) as SongLyricsViewHolder.UiModel)
        is ClearButtonViewHolder -> holder.bind(getItem(position) as ClearButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}