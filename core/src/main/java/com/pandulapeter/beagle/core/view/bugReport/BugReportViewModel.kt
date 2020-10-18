package com.pandulapeter.beagle.core.view.bugReport

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.SendButtonViewHolder
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

internal class BugReportViewModel(
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
    private val selectedGalleryItems = mutableListOf<Uri>()
    private val selectedNetworkLogs = mutableListOf<NetworkLogEntry>()
    private val selectedLogs = logTagSectionsToShow.map { tag -> tag to mutableListOf<LogEntry>() }.toMap()

    init {
        viewModelScope.launch(listManagerContext) { refreshContents() }
    }

    fun onSendButtonPressed() {
        if (isSendButtonEnabled) {
            isSendButtonEnabled = false
            //TODO: Generate and share zip file
            isSendButtonEnabled = true
        }
    }

    private suspend fun refreshContents() = withContext(listManagerContext) {
        _items.postValue(mutableListOf<BugReportListItem>().apply {
            delay(2000)
            if (shouldShowGallerySection) {
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle(selectedGalleryItems.size)))
            }
            if (shouldShowNetworkLogsSection) {
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle(selectedNetworkLogs.size)))
            }
            logTagSectionsToShow.distinct().forEach { tag ->
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(tag, selectedLogs[tag]?.size ?: 0)))
            }
            add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle))
            add(SendButtonViewHolder.UiModel(isSendButtonEnabled))
        })
        _shouldShowLoadingIndicator.postValue(false)
    }
}