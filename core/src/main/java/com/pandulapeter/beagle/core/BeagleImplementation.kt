package com.pandulapeter.beagle.core

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.ContextThemeWrapper
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.common.contracts.BeagleContract
import com.pandulapeter.beagle.common.listeners.VisibilityListener
import com.pandulapeter.beagle.core.manager.DebugMenuInjector
import com.pandulapeter.beagle.core.manager.ListManager
import com.pandulapeter.beagle.core.manager.ShakeDetector
import com.pandulapeter.beagle.core.manager.UiManagerContract
import com.pandulapeter.beagle.core.manager.VisibilityListenerManager
import com.pandulapeter.beagle.core.util.extension.hideKeyboard
import com.pandulapeter.beagle.modules.TextModule
import kotlin.properties.Delegates

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BeagleImplementation(private val uiManager: UiManagerContract) : BeagleContract {

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
    private val shakeDetector by lazy { ShakeDetector { show() } }
    private val debugMenuInjector by lazy { DebugMenuInjector(uiManager) }
    private val listenerManager by lazy { VisibilityListenerManager() }
    private val listManager by lazy { ListManager() }

    init {
        BeagleCore.implementation = this
        //TODO: Remove this.
        val colors = listOf(Color.CYAN, Color.GREEN, Color.MAGENTA, null)
        (0..50).map { index ->
            listManager.addModule(
                TextModule(
                    text = "This is TextModule $index",
                    color = colors.random()
                )
            )
        }
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

    override fun updateCells() = listManager.refreshList()

    override fun addVisibilityListener(listener: VisibilityListener, lifecycleOwner: LifecycleOwner?) = listenerManager.addVisibilityListener(listener, lifecycleOwner)

    override fun removeVisibilityListener(listener: VisibilityListener) = listenerManager.removeVisibilityListener(listener)

    override fun clearVisibilityListeners() = listenerManager.clearVisibilityListeners()

    fun notifyVisibilityListenersOnShow() = listenerManager.notifyVisibilityListenersOnShow()

    fun notifyVisibilityListenersOnHide() = listenerManager.notifyVisibilityListenersOnHide()

    fun hideKeyboard() = currentActivity?.currentFocus?.hideKeyboard() ?: Unit

    fun setupRecyclerView(recyclerView: RecyclerView) = listManager.setupRecyclerView(recyclerView)

    fun getThemedContext(context: Context) = appearance.themeResourceId?.let { ContextThemeWrapper(context, it) } ?: context
}