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
import com.pandulapeter.beagle.core.util.extension.createZipFile
import com.pandulapeter.beagle.core.util.extension.getBugReportsFolder
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.getCrashLogsFolder
import com.pandulapeter.beagle.core.util.getLifecycleLogsFolder
import com.pandulapeter.beagle.core.util.getLogsFolder
import com.pandulapeter.beagle.core.util.getMediaFolder
import com.pandulapeter.beagle.core.util.getNetworkLogsFolder
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.util.realPath
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportListItem
import com.pandulapeter.beagle.core.view.bugReport.list.CrashLogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.DescriptionViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.GalleryViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.HeaderViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.LifecycleLogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.LogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.MetadataItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.NetworkLogItemViewHolder
import com.pandulapeter.beagle.core.view.bugReport.list.ShowMoreViewHolder
import com.pandulapeter.beagle.utils.mutableLiveDataOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executors

internal class BugReportViewModel(
    application: Application,
    restoreModel: RestoreModel?,
    crashLogEntryToShow: SerializableCrashLogEntry?,
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
    private val lifecycleSectionEventTypes = BeagleCore.implementation.behavior.bugReportingBehavior.lifecycleSectionEventTypes
    private val shouldShowMetadataSection = BeagleCore.implementation.behavior.bugReportingBehavior.shouldShowMetadataSection

    private val getBugReportFileName get() = BeagleCore.implementation.behavior.bugReportingBehavior.getBugReportFileName
    private val onBugReportReady get() = BeagleCore.implementation.behavior.bugReportingBehavior.onBugReportReady

    private val _items = mutableLiveDataOf(emptyList<BugReportListItem>())
    val items: LiveData<List<BugReportListItem>> = _items
    private val _shouldShowLoadingIndicator = mutableLiveDataOf(true)
    val shouldShowLoadingIndicator: LiveData<Boolean> = _shouldShowLoadingIndicator

    private var mediaFiles = emptyList<File>()
    private var selectedMediaFileIds = emptyList<String>()

    var allCrashLogEntries: List<SerializableCrashLogEntry>? = null
        private set
    private var lastCrashLogIndex = pageSize
    private var selectedCrashLogIds = emptyList<String>()
    private fun getCrashLogEntries() = allCrashLogEntries?.take(lastCrashLogIndex).orEmpty()
    private fun areThereMoreCrashLogEntries() = (allCrashLogEntries?.size ?: 0) > getCrashLogEntries().size

    val allNetworkLogEntries by lazy { BeagleCore.implementation.getNetworkLogEntriesInternal() }
    private var lastNetworkLogIndex = pageSize
    private var selectedNetworkLogIds = emptyList<String>()
    private fun getNetworkLogEntries() = allNetworkLogEntries.take(lastNetworkLogIndex)
    private fun areThereMoreNetworkLogEntries() = allNetworkLogEntries.size > getNetworkLogEntries().size

    private val allLogEntries by lazy { logLabelSectionsToShow.map { label -> label to BeagleCore.implementation.getLogEntriesInternal(label) }.toMap() }
    private val lastLogIndex = logLabelSectionsToShow.map { label -> label to pageSize }.toMap().toMutableMap()
    private val selectedLogIds = logLabelSectionsToShow.map { label -> label to emptyList<String>() }.toMap().toMutableMap()
    private fun getLogEntries(label: String?) = allLogEntries[label]?.take(lastLogIndex[label] ?: 0).orEmpty()
    private fun areThereMoreLogEntries(label: String?) = allLogEntries[label]?.size ?: 0 > getLogEntries(label).size

    val allLifecycleLogEntries by lazy { BeagleCore.implementation.getLifecycleLogEntriesInternal(lifecycleSectionEventTypes) }
    private var lastLifecycleLogIndex = pageSize
    private var selectedLifecycleLogIds = emptyList<String>()
    private fun getLifecycleLogEntries() = allLifecycleLogEntries.take(lastLifecycleLogIndex)
    private fun areThereMoreLifecycleEntries() = allLifecycleLogEntries.size > getLifecycleLogEntries().size

    private var shouldAttachBuildInformation = true
    private var shouldAttachDeviceInformation = true

    private val textInputValues = textInputDescriptions.map(application::text).toMutableList()

    private val context = getApplication<Application>()
    private val listManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    private val _isSendButtonEnabled = mutableLiveDataOf(false)
    val isSendButtonEnabled: LiveData<Boolean> = _isSendButtonEnabled
    private var isPreparingData = false
        set(value) {
            field = value
            _shouldShowLoadingIndicator.postValue(value)
            refreshSendButton()
        }
    val zipFileUriToShare = MutableLiveData<Uri?>()

    init {
        if (crashLogEntryToShow != null) {
            BeagleCore.implementation.logCrash(crashLogEntryToShow)
        }
        refresh(restoreModel)
    }

    fun refresh(restoreModel: RestoreModel? = null) {
        _shouldShowLoadingIndicator.postValue(true)
        viewModelScope.launch(listManagerContext) {
            restoreModel?.let(BeagleCore.implementation::restoreAfterCrash)
            mediaFiles = context.getScreenCapturesFolder().listFiles().orEmpty().toList().sortedByDescending { it.lastModified() }
            selectedMediaFileIds = selectedMediaFileIds.filter { id -> mediaFiles.any { it.name == id } }
            if (allCrashLogEntries == null) {
                allCrashLogEntries = BeagleCore.implementation.getCrashLogEntriesInternal()
            }
            refreshContent()
        }
    }

    fun onMediaFileLongTapped(fileName: String) = onMediaFileSelectionChanged(fileName)

    fun onCrashLogLongTapped(id: String) = onCrashLogSelectionChanged(id)

    fun onNetworkLogLongTapped(id: String) = onNetworkLogSelectionChanged(id)

    fun onLogLongTapped(id: String, label: String?) = onLogSelectionChanged(id, label)

    fun onLifecycleLogLongTapped(id: String) = onLifecycleLogSelectionChanged(id)

    fun onShowMoreTapped(type: ShowMoreViewHolder.Type) {
        viewModelScope.launch(listManagerContext) {
            when (type) {
                ShowMoreViewHolder.Type.CrashLog -> lastCrashLogIndex += pageSize
                ShowMoreViewHolder.Type.NetworkLog -> lastNetworkLogIndex += pageSize
                is ShowMoreViewHolder.Type.Log -> lastLogIndex[type.label] = (lastLogIndex[type.label] ?: 0) + pageSize
                ShowMoreViewHolder.Type.LifecycleLog -> lastLifecycleLogIndex += pageSize
            }
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

    fun onAttachAllButtonClicked(id: String) {
        viewModelScope.launch(listManagerContext) {
            when (id) {
                ID_CRASH_LOGS -> selectedCrashLogIds = allCrashLogEntries?.map { it.id }.orEmpty()
                ID_NETWORK_LOGS -> selectedNetworkLogIds = allNetworkLogEntries.map { it.id }
                ID_LIFECYCLE_LOGS -> selectedLifecycleLogIds = allLifecycleLogEntries.map { it.id }
                else -> if (id.startsWith(ID_LOGS)) {
                    val label = id.removePrefix(ID_LOGS).let { if (it == "null") null else it }
                    selectedLogIds[label] = allLogEntries[label]?.map { it.id }.orEmpty()
                }
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
                filePaths.addAll(getMediaFolder(selectedMediaFileIds, context))

                // Crash logs
                filePaths.addAll(getCrashLogsFolder(selectedCrashLogIds, allCrashLogEntries, context))

                // Network log files
                filePaths.addAll(getNetworkLogsFolder(selectedNetworkLogIds, allNetworkLogEntries, context))

                // Log files
                filePaths.addAll(getLogsFolder(selectedLogIds, allLogEntries, context))

                // Lifecycle logs
                filePaths.addAll(getLifecycleLogsFolder(selectedLifecycleLogIds, allLifecycleLogEntries, context))

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
                    selectedCrashLogIds.isNotEmpty() ||
                    selectedNetworkLogIds.isNotEmpty() ||
                    selectedLogIds.keys.any { label -> selectedLogIds[label]?.isNotEmpty() == true } ||
                    selectedLifecycleLogIds.isNotEmpty() ||
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

    private fun onCrashLogSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedCrashLogIds = if (selectedCrashLogIds.contains(id)) {
                selectedCrashLogIds.filterNot { it == id }
            } else {
                (selectedCrashLogIds + id)
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

    private fun onLifecycleLogSelectionChanged(id: String) {
        viewModelScope.launch(listManagerContext) {
            selectedLifecycleLogIds = if (selectedLifecycleLogIds.contains(id)) {
                selectedLifecycleLogIds.filterNot { it == id }
            } else {
                (selectedLifecycleLogIds + id)
            }.distinct()
            refreshContent()
        }
    }

    private suspend fun refreshContent() = withContext(listManagerContext) {
        _items.postValue(mutableListOf<BugReportListItem>().apply {
            // Media files
            if (shouldShowGallerySection && mediaFiles.isNotEmpty()) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerGallery",
                        text = BeagleCore.implementation.appearance.bugReportTexts.gallerySectionTitle(selectedMediaFileIds.size),
                        shouldShowAttachAllButton = false
                    )
                )
                add(GalleryViewHolder.UiModel(mediaFiles.map { it.name to it.lastModified() }, selectedMediaFileIds))
            }

            // Crash logs
            getCrashLogEntries().let { crashLogEntries ->
                if (shouldShowCrashLogsSection && crashLogEntries.isNotEmpty()) {
                    add(
                        HeaderViewHolder.UiModel(
                            id = ID_CRASH_LOGS,
                            text = BeagleCore.implementation.appearance.bugReportTexts.crashLogsSectionTitle(selectedCrashLogIds.size),
                            shouldShowAttachAllButton = selectedCrashLogIds.size < allCrashLogEntries?.size ?: 0
                        )
                    )
                    addAll(crashLogEntries.map { entry ->
                        CrashLogItemViewHolder.UiModel(
                            entry = entry,
                            isSelected = selectedCrashLogIds.contains(entry.id)
                        )
                    })
                    if (areThereMoreCrashLogEntries()) {
                        add(ShowMoreViewHolder.UiModel(ShowMoreViewHolder.Type.CrashLog))
                    }
                }
            }

            // Network logs
            getNetworkLogEntries().let { networkLogEntries ->
                if (shouldShowNetworkLogsSection && networkLogEntries.isNotEmpty()) {
                    add(
                        HeaderViewHolder.UiModel(
                            id = ID_NETWORK_LOGS,
                            text = BeagleCore.implementation.appearance.bugReportTexts.networkLogsSectionTitle(selectedNetworkLogIds.size),
                            shouldShowAttachAllButton = selectedNetworkLogIds.size < allNetworkLogEntries.size
                        )
                    )
                    addAll(networkLogEntries.map { entry ->
                        NetworkLogItemViewHolder.UiModel(
                            entry = entry,
                            isSelected = selectedNetworkLogIds.contains(entry.id)
                        )
                    })
                    if (areThereMoreNetworkLogEntries()) {
                        add(ShowMoreViewHolder.UiModel(ShowMoreViewHolder.Type.NetworkLog))
                    }
                }
            }

            // Logs
            logLabelSectionsToShow.distinct().forEach { label ->
                getLogEntries(label).let { logEntries ->
                    if (logEntries.isNotEmpty()) {
                        add(
                            HeaderViewHolder.UiModel(
                                id = "${ID_LOGS}$label",
                                text = BeagleCore.implementation.appearance.bugReportTexts.logsSectionTitle(label, selectedLogIds[label]?.size ?: 0),
                                shouldShowAttachAllButton = selectedLogIds[label]?.size ?: 0 < allLogEntries[label]?.size ?: 0
                            )
                        )
                        addAll(logEntries.map { entry ->
                            LogItemViewHolder.UiModel(
                                entry = entry,
                                isSelected = selectedLogIds[label].orEmpty().contains(entry.id)
                            )
                        })
                        if (areThereMoreLogEntries(label)) {
                            add(ShowMoreViewHolder.UiModel(ShowMoreViewHolder.Type.Log(label)))
                        }
                    }
                }
            }

            // Lifecycle logs
            getLifecycleLogEntries().let { lifecycleLogEntries ->
                if (lifecycleLogEntries.isNotEmpty()) {
                    add(
                        HeaderViewHolder.UiModel(
                            id = ID_LIFECYCLE_LOGS,
                            text = BeagleCore.implementation.appearance.bugReportTexts.lifecycleLogsSectionTitle(selectedLifecycleLogIds.size),
                            shouldShowAttachAllButton = selectedLifecycleLogIds.size < allLifecycleLogEntries.size
                        )
                    )
                    addAll(lifecycleLogEntries.map { entry ->
                        LifecycleLogItemViewHolder.UiModel(
                            entry = entry,
                            isSelected = selectedLifecycleLogIds.contains(entry.id)
                        )
                    })
                    if (areThereMoreLifecycleEntries()) {
                        add(ShowMoreViewHolder.UiModel(ShowMoreViewHolder.Type.LifecycleLog))
                    }
                }
            }

            // Metadata
            if (shouldShowMetadataSection) {
                add(
                    HeaderViewHolder.UiModel(
                        id = "headerMetadata",
                        text = BeagleCore.implementation.appearance.bugReportTexts.metadataSectionTitle,
                        shouldShowAttachAllButton = false
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

            // Input fields
            textInputTitles.forEachIndexed { index, title ->
                add(
                    HeaderViewHolder.UiModel(
                        id = "textInputTitle_$index",
                        text = title,
                        shouldShowAttachAllButton = false
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

    enum class MetadataType {
        BUILD_INFORMATION,
        DEVICE_INFORMATION
    }

    companion object {
        private const val ID_CRASH_LOGS = "headerCrashLogs"
        private const val ID_NETWORK_LOGS = "headerNetworkLogs"
        private const val ID_LOGS = "headerLogs_"
        private const val ID_LIFECYCLE_LOGS = "headerLifecycleLogs"
    }
}