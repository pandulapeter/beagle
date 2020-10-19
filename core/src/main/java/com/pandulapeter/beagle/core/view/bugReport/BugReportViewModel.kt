package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.DescriptionViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.GalleryViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.LogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.MetadataItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.NetworkLogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.ShowMoreLogsViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.ShowMoreNetworkLogsViewHolder
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

internal class BugReportViewModel(
    private val context: Context,
    private val shouldShowGallerySection: Boolean,
    private val shouldShowNetworkLogsSection: Boolean,
    private val logLabelSectionsToShow: List<String?>,
    private val shouldShowMetadataSection: Boolean,
    val buildInformation: CharSequence,
    val deviceInformation: CharSequence,
    descriptionTemplate: String
) : ViewModel() {

    private val _items = MutableLiveData(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private val _shouldShowLoadingIndicator = MutableLiveData(true)
    val shouldShowLoadingIndicator: LiveData<Boolean> = _shouldShowLoadingIndicator

    private var mediaFiles = emptyList<File>()
    private var selectedMediaFileIds = emptyList<String>()

    private val allNetworkLogEntries = BeagleCore.implementation.getNetworkLogEntries()
    private var lastNetworkLogIndex = LOG_INDEX_INCREMENT - 1
    private var selectedNetworkLogIds = emptyList<String>()
    private fun getNetworkLogEntries() = allNetworkLogEntries.take(lastNetworkLogIndex)
    private fun areThereMoreNetworkLogEntries() = allNetworkLogEntries.size > getNetworkLogEntries().size

    private val allLogEntries = logLabelSectionsToShow.map { label -> label to BeagleCore.implementation.getLogEntries(label) }.toMap()
    private val lastLogIndex = logLabelSectionsToShow.map { label -> label to LOG_INDEX_INCREMENT - 1 }.toMap().toMutableMap()
    private val selectedLogIds = logLabelSectionsToShow.map { label -> label to emptyList<String>() }.toMap().toMutableMap()
    private fun getLogEntries(label: String?) = allLogEntries[label]?.take(lastLogIndex[label] ?: 0).orEmpty()
    private fun areThereMoreLogEntries(label: String?) = allLogEntries[label]?.size ?: 0 > getLogEntries(label).size

    private var shouldAttachBuildInformation = true
    private var shouldAttachDeviceInformation = true

    private var description: CharSequence = descriptionTemplate
        set(value) {
            if (field != value) {
                field = value
                refreshSendButton()
            }
        }

    private val listManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    private val _isSendButtonEnabled = MutableLiveData(false)
    val isSendButtonEnabled: LiveData<Boolean> = _isSendButtonEnabled
    private var isPreparingData = false
        set(value) {
            field = value
            refreshSendButton()
        }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(listManagerContext) {
            _shouldShowLoadingIndicator.postValue(true)
            mediaFiles = context.getScreenCapturesFolder().listFiles().orEmpty().toList().sortedByDescending { it.lastModified() }
            selectedMediaFileIds = selectedMediaFileIds.filter { id -> mediaFiles.any { it.name == id } }
            refreshContent()
        }
    }

    fun onMediaFileLongTapped(fileName: String) = onMediaFileSelectionChanged(fileName)

    fun onNetworkLogLongTapped(id: String) = onNetworkLogSelectionChanged(id)

    fun onShowMoreNetworkLogsTapped() {
        viewModelScope.launch(listManagerContext) {
            lastNetworkLogIndex += LOG_INDEX_INCREMENT
            refreshContent()
        }
    }

    fun onLogLongTapped(id: String, label: String?) = onLogSelectionChanged(id, label)

    fun onShowMoreLogsTapped(label: String?) {
        viewModelScope.launch(listManagerContext) {
            lastLogIndex[label] = (lastLogIndex[label] ?: 0) + LOG_INDEX_INCREMENT
            refreshContent()
        }
    }

    fun onDescriptionChanged(newValue: CharSequence) {
        description = newValue
    }

    fun onMetadataItemSelectionChanged(type: MetadataType) {
        viewModelScope.launch(listManagerContext) {
            when (type) {
                MetadataType.BUILD_INFORMATION -> shouldAttachBuildInformation = !shouldAttachBuildInformation
                MetadataType.DEVICE_INFORMATION -> shouldAttachDeviceInformation = !shouldAttachDeviceInformation
            }
            refreshContent()
        }
    }

    fun onSendButtonPressed() {
        if (isSendButtonEnabled.value == true && _shouldShowLoadingIndicator.value == false) {
            isPreparingData = true
            //TODO: Generate and share zip file. File name: Behavior.getButReportFileName. Contents:
            // - selectedMediaFileIds (mapped to mediaFiles)
            // - selectedNetworkLogIds (mapped to allNetworkLogEntries)
            // - flatMap selectedLogIds (mapped to allLogEntries)
            // - if (shouldShowMetadataSection && shouldAttachBuildInformation) buildInformation
            // - if (shouldShowMetadataSection && shouldAttachDeviceInformation) deviceInformation
            // - description
            isPreparingData = false
        }
    }

    private fun refreshSendButton() {
        _isSendButtonEnabled.postValue(
            !isPreparingData && (selectedMediaFileIds.isNotEmpty() ||
                    selectedNetworkLogIds.isNotEmpty() ||
                    selectedLogIds.keys.any { label -> selectedLogIds[label]?.isNotEmpty() == true } ||
                    description.trim().isNotEmpty())
        )
    }

    private fun onMediaFileSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedMediaFileIds = if (selectedMediaFileIds.contains(id)) {
                selectedMediaFileIds.filterNot { it == id }
            } else {
                (selectedMediaFileIds + id)
            }.distinct()
            refreshContent()
        }
    }

    private fun onNetworkLogSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedNetworkLogIds = if (selectedNetworkLogIds.contains(id)) {
                selectedNetworkLogIds.filterNot { it == id }
            } else {
                (selectedNetworkLogIds + id)
            }.distinct()
            refreshContent()
        }
    }

    //TODO: Does not work when label = null
    private fun onLogSelectionChanged(id: String, label: String?) {
        viewModelScope.launch(listManagerContext) {
            selectedLogIds[label] = if (selectedLogIds[label]?.contains(id) == true) {
                selectedLogIds[label].orEmpty().filterNot { it == id }
            } else {
                (selectedLogIds[label].orEmpty() + id)
            }.distinct()
            refreshContent()
        }
    }

    private suspend fun refreshContent() = withContext(listManagerContext) {
        _items.postValue(mutableListOf<BugReportListItem>().apply {
            if (shouldShowGallerySection && mediaFiles.isNotEmpty()) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerGallery",
                        text = BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle(selectedMediaFileIds.size)
                    )
                )
                add(GalleryViewHolder.UiModel(mediaFiles.map { it.name to it.lastModified() }, selectedMediaFileIds))
            }
            getNetworkLogEntries().let { networkLogEntries ->
                if (shouldShowNetworkLogsSection && networkLogEntries.isNotEmpty()) {
                    add(
                        HeaderViewHolder.UiModel(
                            id = "headerNetworkLogs",
                            text = BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle(selectedNetworkLogIds.size)
                        )
                    )
                    addAll(networkLogEntries.map { entry ->
                        NetworkLogItemViewHolder.UiModel(
                            entry = entry,
                            isSelected = selectedNetworkLogIds.contains(entry.id)
                        )
                    })
                    if (areThereMoreNetworkLogEntries()) {
                        add(ShowMoreNetworkLogsViewHolder.UiModel())
                    }
                }
            }
            logLabelSectionsToShow.distinct().forEach { label ->
                getLogEntries(label).let { logEntries ->
                    if (logEntries.isNotEmpty()) {
                        add(
                            HeaderViewHolder.UiModel(
                                id = "headerLogs_$label",
                                text = BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(label, selectedLogIds[label]?.size ?: 0)
                            )
                        )
                        addAll(logEntries.map { entry ->
                            LogItemViewHolder.UiModel(
                                entry = entry,
                                isSelected = selectedLogIds[label].orEmpty().contains(entry.id)
                            )
                        })
                        if (areThereMoreLogEntries(label)) {
                            add(ShowMoreLogsViewHolder.UiModel(label))
                        }
                    }
                }
            }
            if (shouldShowMetadataSection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerMetadata",
                        text = BeagleCore.implementation.appearance.bugReportTexts.metadataSectionTitle
                    )
                )
                if (buildInformation.isNotBlank()) {
                    add(
                        MetadataItemViewHolder.UiModel(
                            type = MetadataType.BUILD_INFORMATION,
                            isSelected = shouldAttachBuildInformation
                        )
                    )
                }
                add(
                    MetadataItemViewHolder.UiModel(
                        type = MetadataType.DEVICE_INFORMATION,
                        isSelected = shouldAttachDeviceInformation
                    )
                )
            }
            add(
                HeaderViewHolder.UiModel(
                    id = "headerDescription",
                    text = BeagleCore.implementation.appearance.bugReportTexts.descriptionSectionTitle
                )
            )
            add(DescriptionViewHolder.UiModel(description))
        })
        refreshSendButton()
        _shouldShowLoadingIndicator.postValue(false)
    }

    enum class MetadataType {
        BUILD_INFORMATION,
        DEVICE_INFORMATION
    }

    companion object {
        private const val LOG_INDEX_INCREMENT = 5
    }
}