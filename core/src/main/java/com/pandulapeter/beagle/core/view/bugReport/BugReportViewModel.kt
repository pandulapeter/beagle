package com.pandulapeter.beagle.core.view.bugReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.SendButtonViewHolder

internal class BugReportViewModel : ViewModel() {

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
            //TODO
            isSendButtonEnabled = true
        }
    }

    private fun refreshContents() {
        //TODO: Sections should be configurable.
        _items.value = listOf(
            HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle),
            HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle),
            HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(null)),
            HeaderViewHolder.UiModel(BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle),
            SendButtonViewHolder.UiModel(isSendButtonEnabled)
        )
    }
}