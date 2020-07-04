package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.NetworkingManager
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlinx.coroutines.launch

class NetworkRequestInterceptorViewModel(
    private val networkingManager: NetworkingManager
) : ListViewModel<ListItem>() {

    override val items: LiveData<List<ListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_1)
        )
    )

    init {
        viewModelScope.launch {
            try {
                Log.d("DEBBB", "Hello - loading request")
                val song = networkingManager.songService.getSongAsync("the_beatles-let_it_be")
                Log.d("DEBBB", "Song ${song.id} loaded")
                Beagle.showDialog(song.text, true)
            } catch (exception: Exception) {
                Log.d("DEBBB", "Error: ${exception.message}")
            }
        }
    }
}