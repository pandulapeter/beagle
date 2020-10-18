package com.pandulapeter.beagle.core.view.bugReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.SendButtonViewHolder

internal class BugReportViewModel(
    private val shouldShowGallerySection: Boolean,
    private val shouldShowNetworkLogsSection: Boolean,
    private val logTagSectionsToShow: List<String?>
) : ViewModel() {

    private val _items = MutableLiveData(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private var isSendButtonEnabled = true
        set(value) {
            field = value
            refreshContents()
        }

    init {
        refreshContents()
    }

    fun onSendButtonPressed() {
        if (isSendButtonEnabled) {
            isSendButtonEnabled = false
            //TODO: Generate and share zip file
            isSendButtonEnabled = true
        }
    }

    private fun refreshContents() {
        _items.value = mutableListOf<BugReportListItem>().apply {
            if (shouldShowGallerySection) {
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle))
            }
            if (shouldShowNetworkLogsSection) {
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle))
            }
            logTagSectionsToShow.forEach { tag ->
                add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(tag)))
            }
            add(HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle))
            add(SendButtonViewHolder.UiModel(isSendButtonEnabled))
        }
    }
}