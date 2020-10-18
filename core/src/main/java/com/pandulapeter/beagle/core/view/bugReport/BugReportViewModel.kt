package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.GalleryViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.SendButtonViewHolder
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

internal class BugReportViewModel(
    private val context: Context,
    private val shouldShowGallerySection: Boolean,
    private val shouldShowNetworkLogsSection: Boolean,
    private val logTagSectionsToShow: List<String?>
) : ViewModel() {

    private val _items = MutableLiveData(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private val _shouldShowLoadingIndicator = MutableLiveData(true)
    val shouldShowLoadingIndicator: LiveData<Boolean> = _shouldShowLoadingIndicator
    private val listManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    private var isSendButtonEnabled = true
        set(value) {
            field = value
            viewModelScope.launch(listManagerContext) { refreshContents() }
        }
    private var selectedGalleryItems = emptyList<String>()
    private val selectedNetworkLogs = mutableListOf<NetworkLogEntry>()
    private val selectedLogs = logTagSectionsToShow.map { tag -> tag to mutableListOf<LogEntry>() }.toMap()
    private var files = emptyList<File>()

    init {
        viewModelScope.launch(listManagerContext) {
            files = context.getScreenCapturesFolder().listFiles().orEmpty().toList().sortedByDescending { it.lastModified() }
            refreshContents()
        }
    }

    fun onSendButtonPressed() {
        if (isSendButtonEnabled) {
            isSendButtonEnabled = false
            //TODO: Generate and share zip file
            isSendButtonEnabled = true
        }
    }

    fun getFileName(index: Int) = files[index].name.orEmpty()

    fun onMediaFileLongTapped(index: Int) = selectItem(getFileName(index))

    private suspend fun refreshContents() = withContext(listManagerContext) {
        _items.postValue(mutableListOf<BugReportListItem>().apply {
            if (shouldShowGallerySection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerGallery",
                        text = BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle(selectedGalleryItems.size)
                    )
                )
                add(GalleryViewHolder.UiModel(files, selectedGalleryItems))
            }
            if (shouldShowNetworkLogsSection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerNetworkLogs",
                        text = BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle(selectedNetworkLogs.size)
                    )
                )
            }
            logTagSectionsToShow.distinct().forEach { tag ->
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerLogs_$tag",
                        text = BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(tag, selectedLogs[tag]?.size ?: 0)
                    )
                )
            }
            add(
                HeaderViewHolder.UiModel(
                    id = "headerDescription",
                    text = BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle
                )
            )
            add(SendButtonViewHolder.UiModel(isSendButtonEnabled))
        })
        _shouldShowLoadingIndicator.postValue(false)
    }

    private fun selectItem(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedGalleryItems = if (selectedGalleryItems.contains(id)) {
                selectedGalleryItems.filterNot { it == id }
            } else {
                (selectedGalleryItems + id).distinct()
            }
            refreshContents()
        }
    }
}