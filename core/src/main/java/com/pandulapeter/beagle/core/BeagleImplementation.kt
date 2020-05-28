package com.pandulapeter.beagle.core

import android.app.Application
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.contracts.VisibilityListener
import com.pandulapeter.beagle.core.manager.DebugMenuInjector
import com.pandulapeter.beagle.core.manager.ListManager
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.manager.VisibilityListenerManager
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import kotlin.properties.Delegates

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BeagleImplementation(private val uiManager: UiManagerContract) : BeagleContract {

    override var isUiEnabled by Delegates.observable(true) { _, _, newValue ->
        if (!newValue) {
            hide()
        }
        listManager.refreshList()
    }
    override val currentActivity get() = debugMenuInjector.currentActivity
    var appearance = Appearance()
        private set
    var behavior = Behavior()
        private set
    private val shakeDetector by lazy { ShakeDetector { show() } }
    private val debugMenuInjector by lazy { DebugMenuInjector(uiManager) }
    private val listenerManager by lazy { VisibilityListenerManager() }
    private val listManager by lazy { ListManager() }

    init {
        BeagleCore.implementation = this
    }

    override fun initialize(
        application: Application,
        appearance: Appearance,
        behavior: Behavior
    ) = (behavior.shakeThreshold == null || shakeDetector.initialize(application)).also {
        this.appearance = appearance
        this.behavior = behavior
        debugMenuInjector.register(application)
    }

    override fun show() = (currentActivity?.let { if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) uiManager.show(it) else false } ?: false)

    override fun hide() = (currentActivity?.let { uiManager.hide(it) } ?: false)

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) = listenerManager.addVisibilityListener(listener, lifecycleOwner)

    override fun removeVisibilityListener(listener: VisibilityListener) = listenerManager.removeVisibilityListener(listener)

    override fun clearVisibilityListeners() = listenerManager.clearVisibilityListeners()

    fun notifyVisibilityListenersOnShow() = listenerManager.notifyVisibilityListenersOnShow()

    fun notifyVisibilityListenersOnHide() = listenerManager.notifyVisibilityListenersOnHide()

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    fun setupRecyclerView(recyclerView: RecyclerView) = listManager.setupRecyclerView(recyclerView)
}