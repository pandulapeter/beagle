package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorSongLyricsBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class SongLyricsViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorSongLyricsBinding,
    onSongCardPressed: () -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorSongLyricsBinding, SongLyricsViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onSongCardPressed()
            }
        }
    }

    data class UiModel(
        val lyrics: CharSequence
    ) : NetworkRequestInterceptorListItem {

        override val id = "songLyrics"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSongCardPressed: () -> Unit
        ) = SongLyricsViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_request_interceptor_song_lyrics, parent, false),
            onSongCardPressed = onSongCardPressed
        )
    }
}