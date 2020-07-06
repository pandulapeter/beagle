package com.pandulapeter.beagle.core

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.configuration.Placement
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.LogListener
import com.pandulapeter.beagle.common.listeners.NetworkLogListener
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.manager.DebugMenuInjector
import com.pandulapeter.beagle.core.manager.ListManager
import com.pandulapeter.beagle.core.manager.LocalStorageManager
import com.pandulapeter.beagle.core.manager.LogManager
import com.pandulapeter.beagle.core.manager.MemoryStorageManager
import com.pandulapeter.beagle.core.manager.NetworkLogManager
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.manager.listener.LogListenerManager
import com.pandulapeter.beagle.core.manager.listener.NetworkLogListenerManager
import com.pandulapeter.beagle.core.manager.listener.OverlayListenerManager
import com.pandulapeter.beagle.core.manager.listener.UpdateListenerManager
import com.pandulapeter.beagle.core.manager.listener.VisibilityListenerManager
import com.pandulapeter.beagle.core.util.NetworkInterceptor
import com.pandulapeter.beagle.core.util.NetworkLogEntry
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import com.pandulapeter.beagle.core.view.AlertDialogFragment
import com.pandulapeter.beagle.core.view.GestureBlockingRecyclerView
import com.pandulapeter.beagle.modules.LogListModule
import com.pandulapeter.beagle.modules.NetworkLogListModule
import okhttp3.Interceptor
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class BeagleImplementation(val uiManager: UiManagerContract) : BeagleContract {

    override var isUiEnabled by Delegates.observable(true) { _, _, newValue ->
        if (!newValue) {
            hide()
        }
    }
    override val currentActivity get() = debugMenuInjector.currentActivity
    override val interceptor by lazy { NetworkInterceptor() as Interceptor }
    var appearance = Appearance()
        private set
    var behavior = Behavior()
        private set
    internal val hasPendingUpdates get() = listManager.hasPendingUpdates
    internal val memoryStorageManager by lazy { MemoryStorageManager() }
    internal lateinit var localStorageManager: LocalStorageManager
        private set
    private val shakeDetector by lazy { ShakeDetector { show() } }
    private val debugMenuInjector by lazy { DebugMenuInjector(uiManager) }
    private val logListenerManager by lazy { LogListenerManager() }
    private val networkLogListenerManager by lazy { NetworkLogListenerManager() }
    private val overlayListenerManager by lazy { OverlayListenerManager() }
    private val updateListenerManager by lazy { UpdateListenerManager() }
    private val visibilityListenerManager by lazy { VisibilityListenerManager() }
    private val logManager by lazy { LogManager() }
    private val networkLogManager by lazy { NetworkLogManager() }
    private val listManager by lazy { ListManager() }
    internal var onScreenshotReady: ((Bitmap) -> Unit)? = null

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(application: Application, appearance: Appearance, behavior: Behavior) =
        (behavior.shakeThreshold == null || shakeDetector.initialize(application)).also {
            this.appearance = appearance
            this.behavior = behavior
            this.localStorageManager = LocalStorageManager(application)
            debugMenuInjector.register(application)
        }

    override fun show() = (currentActivity?.let { if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) uiManager.show(it) else false } ?: false)

    override fun hide() = uiManager.hide(currentActivity)

    override fun set(vararg modules: Module<*>) = listManager.setModules(modules.toList(), updateListenerManager::notifyListeners)

    override fun add(vararg modules: Module<*>, placement: Placement, lifecycleOwner: LifecycleOwner?) =
        listManager.addModules(modules.toList(), placement, lifecycleOwner, updateListenerManager::notifyListeners)

    override fun remove(vararg ids: String) = listManager.removeModules(ids.toList(), updateListenerManager::notifyListeners)

    override fun contains(id: String) = listManager.contains(id)

    override fun <M : Module<M>> find(id: String) = listManager.findModule<M>(id)

    override fun <M : Module<M>> delegateFor(type: KClass<out M>) = listManager.findModuleDelegate(type)

    override fun addLogListener(listener: LogListener, lifecycleOwner: LifecycleOwner?) = logListenerManager.addListener(listener, lifecycleOwner)

    override fun removeLogListener(listener: LogListener) = logListenerManager.removeListener(listener)

    override fun clearLogListeners() = logListenerManager.clearListeners()

    override fun addNetworkLogListener(listener: NetworkLogListener, lifecycleOwner: LifecycleOwner?) = networkLogListenerManager.addListener(listener, lifecycleOwner)

    override fun removeNetworkLogListener(listener: NetworkLogListener) = networkLogListenerManager.removeListener(listener)

    override fun clearNetworkLogListeners() = networkLogListenerManager.clearListeners()

    internal fun addInternalOverlayListener(listener: OverlayListener) = overlayListenerManager.addInternalListener(listener)

    override fun addOverlayListener(listener: OverlayListener, lifecycleOwner: LifecycleOwner?) = overlayListenerManager.addListener(listener, lifecycleOwner)

    override fun removeOverlayListener(listener: OverlayListener) = overlayListenerManager.removeListener(listener)

    fun addInternalUpdateListener(listener: UpdateListener) = updateListenerManager.addInternalListener(listener)

    override fun addUpdateListener(listener: UpdateListener, lifecycleOwner: LifecycleOwner?) = updateListenerManager.addListener(listener, lifecycleOwner)

    override fun removeUpdateListener(listener: UpdateListener) = updateListenerManager.removeListener(listener)

    override fun clearUpdateListeners() = updateListenerManager.clearListeners()

    override fun clearOverlayListeners() = overlayListenerManager.clearListeners()

    internal fun addInternalVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.addInternalListener(listener)

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) = visibilityListenerManager.addListener(listener, lifecycleOwner)

    override fun removeVisibilityListener(listener: VisibilityListener) = visibilityListenerManager.removeListener(listener)

    override fun clearVisibilityListeners() = visibilityListenerManager.clearListeners()

    override fun log(message: CharSequence, tag: String?, payload: CharSequence?) {
        logManager.log(tag, message, payload)
        logListenerManager.notifyListeners(tag, message, payload)
        if (listManager.contains(LogListModule.formatId(null)) || listManager.contains(LogListModule.formatId(tag))) {
            refresh()
        }
    }

    override fun clearLogs(tag: String?) {
        logManager.clearLogs(tag)
        if (listManager.contains(LogListModule.formatId(null)) || listManager.contains(LogListModule.formatId(tag))) {
            refresh()
        }
    }

    override fun logNetworkEvent(isOutgoing: Boolean, url: String, payload: String?, headers: List<String>?, duration: Long?, timestamp: Long) {
        val entry = NetworkLogEntry(
            isOutgoing = isOutgoing,
            payload = payload.orEmpty(),
            headers = headers.orEmpty(),
            url = url,
            duration = duration,
            timestamp = timestamp
        )
        networkLogManager.log(entry)
        networkLogListenerManager.notifyListeners(entry)
        if (listManager.contains(NetworkLogListModule.ID)) {
            refresh()
        }
    }

    override fun clearNetworkLogs() {
        networkLogManager.clearLogs()
        if (listManager.contains(NetworkLogListModule.ID)) {
            refresh()
        }
    }

    override fun refresh() = listManager.refreshCells(updateListenerManager::notifyListeners)

    override fun invalidateOverlay() = debugMenuInjector.invalidateOverlay()

    override fun showDialog(contents: CharSequence, isHorizontalScrollEnabled: Boolean) {
        (uiManager.findHostFragmentManager() ?: currentActivity?.supportFragmentManager)?.let { fragmentManager ->
            AlertDialogFragment.show(
                fragmentManager = fragmentManager,
                content = contents,
                isHorizontalScrollEnabled = isHorizontalScrollEnabled
            )
        }
    }

    internal fun applyPendingChanges() = listManager.applyPendingChanges()

    internal fun resetPendingChanges() = listManager.resetPendingChanges()

    internal fun getLogEntries(tag: String?) = logManager.getEntries(tag)

    internal fun getNetworkLogEntries() = networkLogManager.getEntries()

    internal fun createOverlayLayout(activity: FragmentActivity) = uiManager.createOverlayLayout(activity)

    fun notifyVisibilityListenersOnShow() = visibilityListenerManager.notifyListenersOnShow()

    fun notifyVisibilityListenersOnHide() = visibilityListenerManager.notifyListenersOnHide()

    internal fun notifyOverlayListenersOnDrawOver(canvas: Canvas) = overlayListenerManager.notifyListeners(canvas)

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    internal fun setupRecyclerView(recyclerView: GestureBlockingRecyclerView) = listManager.setupRecyclerView(recyclerView)
}