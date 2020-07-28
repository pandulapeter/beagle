package com.pandulapeter.beagle.core.view.gallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.core.manager.ScreenCaptureManager
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.view.gallery.list.GalleryListItem
import com.pandulapeter.beagle.core.view.gallery.list.ImageViewHolder
import com.pandulapeter.beagle.core.view.gallery.list.VideoViewHolder
import kotlinx.coroutines.launch

internal class GalleryViewModel : ViewModel() {

    private val _items = MutableLiveData<List<GalleryListItem>>()
    val items: LiveData<List<GalleryListItem>> = _items

    fun loadMedia(context: Context) {
        if (_items.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _items.value = context.getScreenCapturesFolder().listFiles().orEmpty().mapNotNull { file ->
                    file.name.let { fileName ->
                        when {
                            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> ImageViewHolder.UiModel(fileName)
                            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> VideoViewHolder.UiModel(fileName)
                            else -> null
                        }
                    }
                }
            }
        }
    }
}