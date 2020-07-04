package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.NetworkingManager
import com.pandulapeter.beagle.appDemo.data.model.Song
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.ErrorViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.RadioButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.SongLyricsViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class NetworkRequestInterceptorViewModel(
    private val networkingManager: NetworkingManager
) : ListViewModel<NetworkRequestInterceptorListItem>() {

    private var selectedSong by Delegates.observable(SongTitle.SONG_1) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            loadSong()
            refreshItems()
        }
    }
    private var loadedSong: Song? = null
    private var isLoading: Boolean = false
    private var job: Job? = null
    private val _items = MutableLiveData(listOf<NetworkRequestInterceptorListItem>())
    override val items: LiveData<List<NetworkRequestInterceptorListItem>> = _items

    init {
        loadSong()
        refreshItems()
    }

    fun onSongSelected(selectedItem: RadioButtonViewHolder.UiModel) {
        SongTitle.fromResourceId(selectedItem.titleResourceId)?.let { selectedSong = it }
    }

    fun loadSong() {
        if (selectedSong.id != loadedSong?.id) {
            job?.cancel()
            isLoading = true
            job = viewModelScope.launch {
                loadedSong = try {
                    networkingManager.songService.getSongAsync(selectedSong.id)
                } catch (_: Exception) {
                    null
                }
                isLoading = false
                refreshItems()
            }
        }
    }

    private fun refreshItems() {
        _items.value = mutableListOf<NetworkRequestInterceptorListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_1))
            addAll(SongTitle.values().map { songTitle -> RadioButtonViewHolder.UiModel(songTitle.titleResourceId, selectedSong == songTitle) })
            if (isLoading) {
                add(LoadingIndicatorViewHolder.UiModel())
            } else {
                if (selectedSong.id == loadedSong?.id) {
                    add(SongLyricsViewHolder.UiModel(loadedSong?.text?.formatSongLyrics() ?: ""))
                } else {
                    add(ErrorViewHolder.UiModel())
                }
            }
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_2))
            add(CodeSnippetViewHolder.UiModel("NetworkRequestInterceptorFragment()"))
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_3))
            add(CodeSnippetViewHolder.UiModel("//TODO: Coming soon"))
        }
    }

    private fun String.formatSongLyrics() =
        replace(Regex("\\[(.*?)[]]"), "") // Remove chords
            .replace(Regex("\\{(.*?)[}]"), "") // Remove sections
            .replace(Regex("[ ][ ]+"), "") // Remove consecutive whitespaces
            .lines().filterNot { it.isEmpty() }.take(4) // Take the first four lines
            .joinToString("\n") + "\n…"

    private enum class SongTitle(@StringRes val titleResourceId: Int, val id: String) {
        SONG_1(titleResourceId = R.string.case_study_network_request_interceptor_song_1, id = "the_beatles-let_it_be"),
        SONG_2(titleResourceId = R.string.case_study_network_request_interceptor_song_2, id = "the_rembrandts-ill_be_there_for_you"),
        SONG_3(titleResourceId = R.string.case_study_network_request_interceptor_song_3, id = "the_proclaimers-im_gonna_be");

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}