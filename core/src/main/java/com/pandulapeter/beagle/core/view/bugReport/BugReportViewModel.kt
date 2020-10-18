package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.GalleryViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.NetworkLogItemViewHolder
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
    private val logTagSectionsToShow: List<String?>,
    private val descriptionTemplate: String
) : ViewModel() {

    private val _items = MutableLiveData(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private val _shouldShowLoadingIndicator = MutableLiveData(true)
    val shouldShowLoadingIndicator: LiveData<Boolean> = _shouldShowLoadingIndicator
    private var mediaFiles = emptyList<File>()
    private var selectedMediaFileIds = emptyList<String>()
    private val allNetworkLogEntries = BeagleCore.implementation.getNetworkLogEntries()
    private val networkLogEntries get() = allNetworkLogEntries.take(lastNetworkLogIndex)
    private val areThereMoreNetworkLogEntries get() = allNetworkLogEntries.size > networkLogEntries.size
    private val firstNetworkLogIndex get() = _items.value?.indexOfFirst { it is NetworkLogItemViewHolder.UiModel } ?: 0
    private var lastNetworkLogIndex = LOG_INDEX_INCREMENT - 1
    private var selectedNetworkLogIds = emptyList<String>()
    private val selectedLogIds = logTagSectionsToShow.map { tag -> tag to mutableListOf<LogEntry>() }.toMap()
    private var isSendButtonEnabled = true
        set(value) {
            field = value
            viewModelScope.launch(listManagerContext) { refreshContents() }
        }
    private val listManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    init {
        viewModelScope.launch(listManagerContext) {
            mediaFiles = context.getScreenCapturesFolder().listFiles().orEmpty().toList().sortedByDescending { it.lastModified() }
            refreshContents()
        }
    }

    fun getMediaFileName(index: Int) = mediaFiles[index].name.orEmpty()

    fun onMediaFileLongTapped(index: Int) = onMediaFileSelectionChanged(getMediaFileName(index))

    fun getNetworkLogEntry(index: Int) = networkLogEntries[index - firstNetworkLogIndex]

    fun onNetworkLogLongTapped(index: Int) = onNetworkLogSelectionChanged(getNetworkLogEntry(index).id)

    fun onSendButtonPressed() {
        if (isSendButtonEnabled) {
            isSendButtonEnabled = false
            //TODO: Generate and share zip file
            isSendButtonEnabled = true
        }
    }

    private fun onNetworkLogSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedNetworkLogIds = if (selectedNetworkLogIds.contains(id)) {
                selectedNetworkLogIds.filterNot { it == id }
            } else {
                (selectedNetworkLogIds + id)
            }.distinct()
            refreshContents()
        }
    }

    private fun onMediaFileSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedMediaFileIds = if (selectedMediaFileIds.contains(id)) {
                selectedMediaFileIds.filterNot { it == id }
            } else {
                (selectedMediaFileIds + id)
            }.distinct()
            refreshContents()
        }
    }

    private suspend fun refreshContents() = withContext(listManagerContext) {
        _items.postValue(mutableListOf<BugReportListItem>().apply {
            if (shouldShowGallerySection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerGallery",
                        text = BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle(selectedMediaFileIds.size)
                    )
                )
                add(GalleryViewHolder.UiModel(mediaFiles, selectedMediaFileIds))
            }
            if (shouldShowNetworkLogsSection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerNetworkLogs",
                        text = BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle(selectedNetworkLogIds.size)
                    )
                )
                //TODO: Pagination
                addAll(networkLogEntries.map { entry ->
                    NetworkLogItemViewHolder.UiModel(
                        entry = entry,
                        isSelected = selectedNetworkLogIds.contains(entry.id)
                    )
                })
                if (areThereMoreNetworkLogEntries) {
                    //TODO: Add empty state
                }
            }
            logTagSectionsToShow.distinct().forEach { tag ->
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerLogs_$tag",
                        text = BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(tag, selectedLogIds[tag]?.size ?: 0)
                    )
                )
                //TODO: Add logs / empty state if needed
            }
            add(
                HeaderViewHolder.UiModel(
                    id = "headerDescription",
                    text = BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle
                )
            )
            //TODO: Add text input field
            add(SendButtonViewHolder.UiModel(isSendButtonEnabled))
        })
        _shouldShowLoadingIndicator.postValue(false)
    }

    companion object {
        private const val LOG_INDEX_INCREMENT = 10
    }
}