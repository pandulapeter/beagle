package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorSongLyricsBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

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
            binding = ItemNetworkRequestInterceptorSongLyricsBinding.inflate(parent.inflater, parent, false),
            onSongCardPressed = onSongCardPressed
        )
    }
}