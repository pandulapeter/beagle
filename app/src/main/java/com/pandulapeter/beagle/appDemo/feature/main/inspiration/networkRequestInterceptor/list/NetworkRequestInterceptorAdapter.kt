package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class NetworkRequestInterceptorAdapter(
    scope: CoroutineScope,
    private val onSongSelected: (RadioButtonViewHolder.UiModel) -> Unit
) : BaseAdapter<NetworkRequestInterceptorListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is RadioButtonViewHolder.UiModel -> R.layout.item_network_request_interceptor_radio_button
        is SongLyricsViewHolder.UiModel -> R.layout.item_network_request_interceptor_song_lyrics
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_network_request_interceptor_radio_button -> RadioButtonViewHolder.create(parent, onSongSelected)
        R.layout.item_network_request_interceptor_song_lyrics -> SongLyricsViewHolder.create(parent)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is RadioButtonViewHolder -> holder.bind(getItem(position) as RadioButtonViewHolder.UiModel)
        is SongLyricsViewHolder -> holder.bind(getItem(position) as SongLyricsViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}