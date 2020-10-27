package com.pandulapeter.beagle.core.view.bugReport

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.core.util.extension.createBugReportTextFile
import com.pandulapeter.beagle.core.util.extension.createLogFile
import com.pandulapeter.beagle.core.util.extension.createZipFile
import com.pandulapeter.beagle.core.util.extension.getBugReportsFolder
import com.pandulapeter.beagle.core.util.extension.getLogsFolder
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.getUriForFile
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.DescriptionViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.GalleryViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.LogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.MetadataItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.NetworkLogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.ShowMoreLogsViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.ShowMoreNetworkLogsViewHolder
import com.pandulapeter.beagle.core.view.networkLogDetail.NetworkLogDetailDialogViewModel
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

internal class BugReportViewModel(
    application: Application,
    val buildInformation: CharSequence,
    val deviceInformation: CharSequence,
    private val textInputTitles: List<Text>,
    textInputDescriptions: List<Text>
) : AndroidViewModel(application) {

    private val pageSize = BeagleCore.implementation.behavior.bugReportingBehavior.pageSize
    private val shouldShowGallerySection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowGallerySection
    private val shouldShowCrashLogsSection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowCrashLogsSection
    private val shouldShowNetworkLogsSection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowNetworkLogsSection
    private val logLabelSectionsToShow = BeagleCore.implementation.behavior.bugReportingBehavior.logLabelSectionsToShow
    private val shouldShowLifecycleLogsSection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowLifecycleLogsSection
    private val shouldShowMetadataSection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowMetadataSection

    private val getNetworkLogFileName get() = BeagleCore.implementation.behavior.networkLogBehavior.getFileName
    private val getLogFileName get() = BeagleCore.implementation.behavior.logBehavior.getFileName
    private val getBugReportFileName get() = BeagleCore.implementation.behavior.bugReportingBehavior.getBugReportFileName
    private val onBugReportReady get() = BeagleCore.implementation.behavior.bugReportingBehavior.onBugReportReady

    private val _items = MutableLiveData(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private val _shouldShowLoadingIndicator = MutableLiveData(true)
    val shouldShowLoadingIndicator: LiveData<Boolean> = _shouldShowLoadingIndicator

    private var mediaFiles = emptyList<File>()
    private var selectedMediaFileIds = emptyList<String>()

    private val allNetworkLogEntries = BeagleCore.implementation.getNetworkLogEntries()
    private var lastNetworkLogIndex = pageSize - 1
    private var selectedNetworkLogIds = emptyList<String>()
    private fun getNetworkLogEntries() = allNetworkLogEntries.take(lastNetworkLogIndex)
    private fun areThereMoreNetworkLogEntries() = allNetworkLogEntries.size > getNetworkLogEntries().size

    private val allLogEntries = logLabelSectionsToShow.map { label -> label to BeagleCore.implementation.getLogEntries(label) }.toMap()
    private val lastLogIndex = logLabelSectionsToShow.map { label -> label to pageSize - 1 }.toMap().toMutableMap()
    private val selectedLogIds = logLabelSectionsToShow.map { label -> label to emptyList<String>() }.toMap().toMutableMap()
    private fun getLogEntries(label: String?) = allLogEntries[label]?.take(lastLogIndex[label] ?: 0).orEmpty()
    private fun areThereMoreLogEntries(label: String?) = allLogEntries[label]?.size ?: 0 > getLogEntries(label).size

    private var shouldAttachBuildInformation = true
    private var shouldAttachDeviceInformation = true

    private val textInputValues = textInputDescriptions.map(application::text).toMutableList()

    private val context = getApplication<Application>()
    private val listManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    private val _isSendButtonEnabled = MutableLiveData(false)
    val isSendButtonEnabled: LiveData<Boolean> = _isSendButtonEnabled
    private var isPreparingData = false
        set(value) {
            field = value
            _shouldShowLoadingIndicator.postValue(value)
            refreshSendButton()
        }
    val zipFileUriToShare = MutableLiveData<Uri?>()

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
            lastNetworkLogIndex += pageSize
            refreshContent()
        }
    }

    fun onLogLongTapped(id: String, label: String?) = onLogSelectionChanged(id, label)

    fun onShowMoreLogsTapped(label: String?) {
        viewModelScope.launch(listManagerContext) {
            lastLogIndex[label] = (lastLogIndex[label] ?: 0) + pageSize
            refreshContent()
        }
    }

    fun onDescriptionChanged(index: Int, newValue: CharSequence) {
        textInputValues[index] = newValue
        refreshSendButton()
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
            viewModelScope.launch {
                isPreparingData = true
                val filePaths = mutableListOf<String>()

                // Media files
                filePaths.addAll(
                    selectedMediaFileIds
                        .map { fileName -> context.getUriForFile(context.getScreenCapturesFolder().resolve(fileName)) }
                        .toPaths(context.getScreenCapturesFolder())
                )

                // TODO: Crash logs

                // Network log files
                filePaths.addAll(
                    selectedNetworkLogIds
                        .mapNotNull { id ->
                            allNetworkLogEntries.firstOrNull { it.id == id }?.let { entry ->
                                context.createLogFile(
                                    fileName = "${getNetworkLogFileName(currentTimestamp, entry.id)}.txt",
                                    content = NetworkLogDetailDialogViewModel.createLogFileContents(
                                        title = NetworkLogDetailDialogViewModel.createTitle(
                                            isOutgoing = entry.isOutgoing,
                                            url = entry.url
                                        ),
                                        metadata = NetworkLogDetailDialogViewModel.createMetadata(
                                            context = context,
                                            headers = entry.headers,
                                            timestamp = entry.timestamp,
                                            duration = entry.duration
                                        ),
                                        formattedJson = NetworkLogDetailDialogViewModel.formatJson(
                                            json = entry.payload,
                                            indentation = 4
                                        )
                                    )
                                )
                            }
                        }
                        .toPaths(context.getLogsFolder())
                )

                // Log files
                filePaths.addAll(
                    allLogEntries[null]?.let { allLogEntries ->
                        selectedLogIds.flatMap { it.value }.distinct().mapNotNull { id -> allLogEntries.firstOrNull { it.id == id } }
                    }.orEmpty().mapNotNull { entry ->
                        context.createLogFile(
                            fileName = "${getLogFileName(currentTimestamp, entry.id)}.txt",
                            content = entry.getFormattedContents(BeagleCore.implementation.appearance.logTimestampFormatter).toString()
                        )
                    }.toPaths(context.getLogsFolder())
                )

                // TODO: Lifecycle logs

                // Build information
                var content = ""
                if (shouldShowMetadataSection && shouldAttachBuildInformation && buildInformation.isNotBlank()) {
                    content = buildInformation.toString()
                }

                // Device information
                if (shouldShowMetadataSection && shouldAttachDeviceInformation) {
                    content = if (content.isBlank()) deviceInformation.toString() else "$content\n\n$deviceInformation"
                }

                // Text inputs
                textInputValues.forEachIndexed { index, textInputValue ->
                    if (textInputValue.trim().isNotBlank()) {
                        val text = "${context.text(textInputTitles[index])}\n${textInputValue.trim()}"
                        content = if (content.isBlank()) text else "$content\n\n$text"
                    }
                }

                // Create the log file
                if (content.isNotBlank()) {
                    context.createBugReportTextFile(
                        fileName = "${getBugReportFileName(currentTimestamp)}.txt",
                        content = content
                    )?.let { uri -> filePaths.add(uri.toPath(context.getBugReportsFolder())) }
                }

                // Create the zip file
                val uri = context.createZipFile(
                    filePaths = filePaths,
                    zipFileName = "${getBugReportFileName(currentTimestamp)}.zip",
                )
                onBugReportReady.let { onBugReportReady ->
                    if (onBugReportReady == null) {
                        zipFileUriToShare.postValue(uri)
                    } else {
                        onBugReportReady(uri)
                    }
                }
                isPreparingData = false
            }
        }
    }

    private fun refreshSendButton() {
        _isSendButtonEnabled.postValue(
            !isPreparingData && (selectedMediaFileIds.isNotEmpty() ||
                    selectedNetworkLogIds.isNotEmpty() ||
                    selectedLogIds.keys.any { label -> selectedLogIds[label]?.isNotEmpty() == true } ||
                    textInputValues.any { it.trim().isNotEmpty() })
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

    private fun onLogSelectionChanged(id: String, label: String?) {
        viewModelScope.launch(listManagerContext) {
            selectedLogIds[label] = if (selectedLogIds[label]?.contains(id) == true) {
                selectedLogIds[label].orEmpty().filterNot { it == id }
            } else {
                (selectedLogIds[label].orEmpty() + id)
            }.distinct()
            if (label != null && logLabelSectionsToShow.contains(null)) {
                selectedLogIds[null] = if (selectedLogIds[null]?.contains(id) == true) {
                    selectedLogIds[null].orEmpty().filterNot { it == id }
                } else {
                    (selectedLogIds[null].orEmpty() + id)
                }.distinct()
            }
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
            if (shouldShowCrashLogsSection) {
                // TODO: Crash logs
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
            if (shouldShowLifecycleLogsSection) {
                // TODO: Crash logs
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
            textInputTitles.forEachIndexed { index, title ->
                add(
                    HeaderViewHolder.UiModel(
                        id = "textInputTitle_$index",
                        text = title
                    )
                )
                add(
                    DescriptionViewHolder.UiModel(
                        index = index,
                        text = textInputValues[index]
                    )
                )
            }
        })
        refreshSendButton()
        _shouldShowLoadingIndicator.postValue(false)
    }

    private fun Uri.toPath(folder: File): String = "${folder.canonicalPath}/$realPath"

    private fun List<Uri>.toPaths(folder: File): List<String> = folder.canonicalPath.let { path -> map { "$path/${it.realPath}" } }

    private val Uri.realPath get() = path?.split("/")?.lastOrNull()

    enum class MetadataType {
        BUILD_INFORMATION,
        DEVICE_INFORMATION
    }
}