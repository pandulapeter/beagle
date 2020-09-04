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
    private val _isInSelectionMode = MutableLiveData(false)
    val isInSelectionMode: LiveData<Boolean> = _isInSelectionMode
    private var selectedItemIds = emptyList<String>()
        set(value) {
            if (field != value) {
                field = value
                _isInSelectionMode.value = value.isNotEmpty()
            }
        }

    init {
        _isInSelectionMode.observeForever {
            if (!it) {
                selectedItemIds = emptyList()
            }
        }
    }

    //TODO: Cache in memory
    fun loadMedia(context: Context) {
        if (_items.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _items.value = context.getScreenCapturesFolder().listFiles().orEmpty().mapNotNull { file ->
                    file.name.let { fileName ->
                        when {
                            fileName.endsWith(ScreenCaptureManager.IMAGE_EXTENSION) -> ImageViewHolder.UiModel(fileName, selectedItemIds.contains(fileName), file.lastModified())
                            fileName.endsWith(ScreenCaptureManager.VIDEO_EXTENSION) -> VideoViewHolder.UiModel(fileName, selectedItemIds.contains(fileName), file.lastModified())
                            else -> null
                        }
                    }
                }.sortedByDescending { it.lastModified }
            }
        }
    }

    fun selectItem(context: Context, id: String) {
        selectedItemIds = (selectedItemIds + id).distinct()
        loadMedia(context)
    }

    fun exitSelectionMode(context: Context) {
        _isInSelectionMode.value = false
        loadMedia(context)
    }

    fun deleteSelectedItems(context: Context) {
        //TODO: Confirmation dialog, delete logic
        exitSelectionMode(context)
    }
}