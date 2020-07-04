package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorSongLyricsBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class SongLyricsViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorSongLyricsBinding
) : BaseViewHolder<ItemNetworkRequestInterceptorSongLyricsBinding, SongLyricsViewHolder.UiModel>(binding) {

    data class UiModel(
        val lyrics: CharSequence
    ) : NetworkRequestInterceptorListItem {

        override val id = "songLyrics"
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = SongLyricsViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_request_interceptor_song_lyrics, parent, false)
        )
    }
}