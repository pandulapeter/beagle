package com.pandulapeter.beagle.core

import android.app.Application
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.Placement
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.*
import com.pandulapeter.beagle.commonBase.model.CrashLogEntry
import com.pandulapeter.beagle.commonBase.model.LifecycleLogEntry
import com.pandulapeter.beagle.commonBase.model.LogEntry
import com.pandulapeter.beagle.commonBase.model.NetworkLogEntry
import com.pandulapeter.beagle.core.manager.*
import com.pandulapeter.beagle.core.manager.listener.*
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.view.gallery.MediaPreviewDialogFragment
import com.pandulapeter.beagle.core.view.logDetail.LogDetailDialogFragment
import com.pandulapeter.beagle.core.view.networkLogDetail.NetworkLogDetailDialogFragment
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import com.pandulapeter.beagle.utils.extensions.hideKeyboard
import com.pandulapeter.beagle.utils.view.GestureBlockingRecyclerView
import kotlinx.coroutines.*
import java.io.File
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class BeagleImplementation(val uiManager: UiManagerContract) : BeagleContract {

    override var isUiEnabled by Delegates.observable(true) { _, _, newValue ->
        if (!newValue) {
            hide()
        }
    }
    override val currentActivity get() = debugMenuInjector.currentActivity
    var appearance = Appearance()
        private set
    var behavior = Behavior()
        private set
    lateinit var videoThumbnailLoader: ImageLoader
    internal val hasPendingUpdates get() = listManager.hasPendingUpdates
    internal val memoryStorageManager by lazy { MemoryStorageManager() }
    internal lateinit var localStorageManager: LocalStorageManager
        private set
    private val shakeDetector by lazy { ShakeDetector() }
    private val debugMenuInjector by lazy { DebugMenuInjector(uiManager) }
    private val logListenerManager by lazy { LogListenerManager() }
    private val networkLogListenerManager by lazy { NetworkLogListenerManager() }
    private val overlayListenerManager by lazy { OverlayListenerManager() }
    private val updateListenerManager by lazy { UpdateListenerManager() }
    private val visibilityListenerManager by lazy { VisibilityListenerManager() }
    private val crashLogManager by lazy { CrashLogManager() }
    private val logManager by lazy { LogManager(logListenerManager, listManager, ::refresh) }
    private val lifecycleLogManager by lazy { LifecycleLogManager(listManager, ::refresh) }
    private val networkLogManager by lazy { NetworkLogManager(networkLogListenerManager, listManager, ::refresh) }
    private val listManager by lazy { ListManager() }
    private val screenCaptureManager by lazy { ScreenCaptureManager() }
    private val bugReportManager by lazy { BugReportManager() }
    internal var onScreenCaptureReady: ((Uri?) -> Unit)?
        get() = screenCaptureManager.onScreenCaptureReady
        set(value) {
            screenCaptureManager.onScreenCaptureReady = value
        }

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ) = (behavior.shakeDetectionBehavior.threshold == null || shakeDetector.initialize(application)).also {
        this.appearance = appearance
        this.behavior = behavior
        logManager.application = application
        crashLogManager.application = application
        this.localStorageManager = LocalStorageManager(application)
        behavior.bugReportingBehavior.crashLoggers.forEach { it.initialize(application) }
        debugMenuInjector.register(application)
        behavior.logBehavior.loggers.forEach { it.register(::log, ::clearLogEntries) }
        behavior.networkLogBehavior.networkLoggers.forEach { it.register(::logNetworkEvent, ::clearNetworkLogEntries) }
        videoThumbnailLoader = ImageLoader.Builder(application)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
    }

    override fun show() = (currentActivity?.let { currentActivity ->
        if (currentActivity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            && currentActivity.supportFragmentManager.findFragmentByTag(MediaPreviewDialogFragment.TAG) == null
            && (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || !currentActivity.isInPictureInPictureMode)
        ) uiManager.show(currentActivity) else false
    } ?: false)

    override fun hide() = uiManager.hide(currentActivity)

    override fun set(vararg modules: Module<*>) = listManager.setModules(
        newModules = modules.toList(),
        onContentsChanged = updateListenerManager::notifyListenersOnContentsChanged
    )

    override fun add(
        vararg modules: Module<*>,
        placement: Placement,
        lifecycleOwner: LifecycleOwner?
    ) = listManager.addModules(
        newModules = modules.toList(),
        placement = placement,
        lifecycleOwner = lifecycleOwner,
        onContentsChanged = updateListenerManager::notifyListenersOnContentsChanged
    )

    override fun remove(vararg ids: String) = listManager.removeModules(
        ids = ids.toList(),
        onContentsChanged = updateListenerManager::notifyListenersOnContentsChanged
    )

    override fun contains(id: String) = listManager.contains(id)

    override fun <M : Module<M>> find(id: String) = listManager.findModule<M>(id)

    override fun <M : Module<M>> delegateFor(type: KClass<out M>) = listManager.findModuleDelegate(type)

    override fun addLogListener(
        listener: LogListener,
        lifecycleOwner: LifecycleOwner?
    ) = logListenerManager.addListener(
        listener = listener,
        lifecycleOwner = lifecycleOwner
    )

    override fun removeLogListener(listener: LogListener) = logListenerManager.removeListener(listener)

    override fun clearLogListeners() = logListenerManager.clearListeners()

    override fun addNetworkLogListener(
        listener: NetworkLogListener,
        lifecycleOwner: LifecycleOwner?
    ) = networkLogListenerManager.addListener(
        listener = listener,
        lifecycleOwner = lifecycleOwner
    )

    override fun removeNetworkLogListener(listener: NetworkLogListener) = networkLogListenerManager.removeListener(listener)

    override fun clearNetworkLogListeners() = networkLogListenerManager.clearListeners()

    internal fun addInternalOverlayListener(listener: OverlayListener) = overlayListenerManager.addInternalListener(listener)

    override fun addOverlayListener(
        listener: OverlayListener,
        lifecycleOwner: LifecycleOwner?
    ) = overlayListenerManager.addListener(
        listener = listener,
        lifecycleOwner = lifecycleOwner
    )

    override fun removeOverlayListener(listener: OverlayListener) = overlayListenerManager.removeListener(listener)

    fun addInternalUpdateListener(listener: UpdateListener) = updateListenerManager.addInternalListener(listener)

    override fun addUpdateListener(
        listener: UpdateListener,
        lifecycleOwner: LifecycleOwner?
    ) = updateListenerManager.addListener(
        listener = listener,
        lifecycleOwner = lifecycleOwner
    )

    override fun removeUpdateListener(listener: UpdateListener) = updateListenerManager.removeListener(listener)

    override fun clearUpdateListeners() = updateListenerManager.clearListeners()

    override fun clearOverlayListeners() = overlayListenerManager.clearListeners()

    private fun addInternalVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.addInternalListener(listener)

    override fun addVisibilityListener(
        listener: VisibilityListener,
        lifecycleOwner: LifecycleOwner?
    ) = visibilityListenerManager.addListener(
        listener = listener,
        lifecycleOwner = lifecycleOwner
    )

    override fun removeVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.removeListener(listener)

    override fun clearVisibilityListeners() = visibilityListenerManager.clearListeners()

    private fun log(logEntry: LogEntry) = log(
        message = logEntry.message,
        label = logEntry.label,
        payload = logEntry.payload,
        isPersisted = logEntry.isPersisted,
        timestamp = logEntry.timestamp,
        id = logEntry.id
    )

    override fun log(
        message: String,
        label: String?,
        payload: String?,
        isPersisted: Boolean,
        timestamp: Long,
        id: String
    ) = logManager.log(
        label = label,
        message = message,
        payload = payload,
        isPersisted = isPersisted,
        timestamp = timestamp,
        id = id
    )

    override fun clearLogEntries(label: String?) = logManager.clearLogs(label)

    private fun logNetworkEvent(networkLogEntry: NetworkLogEntry) = logNetwork(
        isOutgoing = networkLogEntry.isOutgoing,
        url = networkLogEntry.url,
        payload = networkLogEntry.payload,
        headers = networkLogEntry.headers,
        duration = networkLogEntry.duration,
        timestamp = networkLogEntry.timestamp,
        id = networkLogEntry.id,
    )

    override fun logNetwork(
        isOutgoing: Boolean,
        url: String,
        payload: String?,
        headers: List<String>?,
        duration: Long?,
        timestamp: Long,
        id: String
    ) = networkLogManager.log(
        isOutgoing = isOutgoing,
        url = url,
        payload = payload,
        headers = headers,
        duration = duration,
        timestamp = timestamp,
        id = id
    )

    fun getNetworkLogEntriesInternal() = networkLogManager.getEntries()

    override suspend fun getNetworkLogEntries() = withContext(Dispatchers.Main) {
        getNetworkLogEntriesInternal().map { it.toNetworkLogEntry() }
    }

    override fun clearNetworkLogEntries() = networkLogManager.clearLogs()

    override fun clearLifecycleLogEntries() = lifecycleLogManager.clearLogs()

    override fun clearCrashLogEntries() = crashLogManager.clearLogs()

    override fun takeScreenshot() = screenCaptureManager.takeScreenshot()

    override fun recordScreen() = screenCaptureManager.recordScreen()

    override fun openGallery() = screenCaptureManager.openGallery()

    override fun openBugReportingScreen() = bugReportManager.openBugReportingScreen()

    override fun shareBugReport(
        shouldIncludeMediaFile: (File) -> Boolean,
        shouldIncludeCrashLogEntry: (CrashLogEntry) -> Boolean,
        shouldIncludeNetworkLogEntry: (NetworkLogEntry) -> Boolean,
        shouldIncludeLogEntry: (LogEntry) -> Boolean,
        shouldIncludeLifecycleLogEntry: (LifecycleLogEntry) -> Boolean,
        shouldIncludeBuildInformation: Boolean,
        shouldIncludeDeviceInformation: Boolean,
        extraDataToInclude: String
    ) = shareBugReportInternal(
        shouldIncludeMediaFile = shouldIncludeMediaFile,
        shouldIncludeCrashLogEntry = shouldIncludeCrashLogEntry,
        shouldIncludeNetworkLogEntry = shouldIncludeNetworkLogEntry,
        shouldIncludeLogEntry = shouldIncludeLogEntry,
        shouldIncludeLifecycleLogEntry = shouldIncludeLifecycleLogEntry,
        shouldIncludeBuildInformation = shouldIncludeBuildInformation,
        shouldIncludeDeviceInformation = shouldIncludeDeviceInformation,
        extraDataToInclude = extraDataToInclude,
        scope = GlobalScope
    )

    internal fun shareBugReportInternal(
        shouldIncludeMediaFile: (File) -> Boolean,
        shouldIncludeCrashLogEntry: (CrashLogEntry) -> Boolean,
        shouldIncludeNetworkLogEntry: (NetworkLogEntry) -> Boolean,
        shouldIncludeLogEntry: (LogEntry) -> Boolean,
        shouldIncludeLifecycleLogEntry: (LifecycleLogEntry) -> Boolean,
        shouldIncludeBuildInformation: Boolean,
        shouldIncludeDeviceInformation: Boolean,
        extraDataToInclude: String,
        scope: CoroutineScope
    ) = bugReportManager.shareBugReport(
        shouldIncludeMediaFile = shouldIncludeMediaFile,
        shouldIncludeCrashLogEntry = shouldIncludeCrashLogEntry,
        shouldIncludeNetworkLogEntry = shouldIncludeNetworkLogEntry,
        shouldIncludeLogEntry = shouldIncludeLogEntry,
        shouldIncludeLifecycleLogEntry = shouldIncludeLifecycleLogEntry,
        shouldIncludeBuildInformation = shouldIncludeBuildInformation,
        shouldIncludeDeviceInformation = shouldIncludeDeviceInformation,
        extraDataToInclude = extraDataToInclude,
        scope = scope
    )

    override fun refresh() = listManager.refreshCells(updateListenerManager::notifyListenersOnContentsChanged)

    override fun invalidateOverlay() = debugMenuInjector.invalidateOverlay()

    override fun showDialog(
        content: Text,
        isHorizontalScrollEnabled: Boolean,
        shouldShowShareButton: Boolean,
        timestamp: Long,
        id: String
    ) = showDialog(
        content = content,
        isHorizontalScrollEnabled = isHorizontalScrollEnabled,
        shouldShowShareButton = shouldShowShareButton,
        timestamp = timestamp,
        id = id,
        fileName = ""
    )

    override fun showNetworkEventDialog(
        isOutgoing: Boolean,
        url: String,
        payload: String,
        headers: List<String>?,
        duration: Long?,
        timestamp: Long,
        id: String
    ) {
        (uiManager.findHostFragmentManager() ?: currentActivity?.supportFragmentManager)?.let { fragmentManager ->
            NetworkLogDetailDialogFragment.show(
                fragmentManager = fragmentManager,
                isOutgoing = isOutgoing,
                url = url,
                payload = payload,
                headers = headers,
                duration = duration,
                timestamp = timestamp,
                id = id
            )
        }
    }

    override fun performOnHide(action: () -> Any?) {
        val listener = object : VisibilityListener {
            override fun onHidden() {
                val reference = this
                action()
                GlobalScope.launch {
                    delay(100)
                    removeVisibilityListener(reference)
                }
            }
        }
        addInternalVisibilityListener(listener)
        if (!hide()) {
            removeVisibilityListener(listener)
            listener.onHidden()
        }
    }

    internal fun showDialog(
        content: Text,
        isHorizontalScrollEnabled: Boolean,
        shouldShowShareButton: Boolean,
        timestamp: Long,
        id: String,
        fileName: String
    ) {
        (uiManager.findHostFragmentManager() ?: currentActivity?.supportFragmentManager)?.let { fragmentManager ->
            LogDetailDialogFragment.show(
                fragmentManager = fragmentManager,
                content = content,
                isHorizontalScrollEnabled = isHorizontalScrollEnabled,
                shouldShowShareButton = shouldShowShareButton,
                timestamp = timestamp,
                id = id,
                fileName = fileName
            )
        }
    }

    internal fun applyPendingChanges() {
        listManager.applyPendingChanges()
        updateListenerManager.notifyListenersOnAllPendingChangesApplied()
    }

    internal fun resetPendingChanges() = listManager.resetPendingChanges()

    fun getLogEntriesInternal(label: String?) = logManager.getEntries(label)

    override suspend fun getLogEntries(label: String?) = withContext(Dispatchers.Default) {
        getLogEntriesInternal(label).map { it.toLogEntry() }
    }

    internal fun logLifecycle(
        classType: Class<*>,
        eventType: LifecycleLogListModule.EventType,
        hasSavedInstanceState: Boolean? = null
    ) = lifecycleLogManager.log(
        classType = classType,
        eventType = eventType,
        hasSavedInstanceState = hasSavedInstanceState
    )

    fun getLifecycleLogEntriesInternal(eventTypes: List<LifecycleLogListModule.EventType>?) = lifecycleLogManager.getEntries(eventTypes)

    override suspend fun getLifecycleLogEntries(eventTypes: List<LifecycleLogListModule.EventType>?) = withContext(Dispatchers.Default) {
        getLifecycleLogEntriesInternal(eventTypes).map { it.toLifecycleLogEntry() }
    }

    internal fun restoreAfterCrash(restoreModel: RestoreModel) {
        logManager.restore(restoreModel.logs)
        networkLogManager.restore(restoreModel.networkLogs)
        lifecycleLogManager.restore(restoreModel.lifecycleLogs)
    }

    internal fun logCrash(crashLogEntry: SerializableCrashLogEntry) = crashLogManager.log(crashLogEntry)

    internal suspend fun getCrashLogEntriesInternal() = crashLogManager.getCrashLogEntries()

    override suspend fun getCrashLogEntries() = getCrashLogEntriesInternal().map { it.toCrashLogEntry() }

    internal fun createOverlayLayout(activity: FragmentActivity, overlayFragment: Fragment) = uiManager.createOverlayLayout(activity, overlayFragment)

    fun notifyVisibilityListenersOnShow() = visibilityListenerManager.notifyListenersOnShow()

    fun notifyVisibilityListenersOnHide() = visibilityListenerManager.notifyListenersOnHide()

    internal fun notifyOverlayListenersOnDrawOver(canvas: Canvas) = overlayListenerManager.notifyListeners(canvas)

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    internal fun setupRecyclerView(recyclerView: GestureBlockingRecyclerView) = listManager.setupRecyclerView(recyclerView)
}