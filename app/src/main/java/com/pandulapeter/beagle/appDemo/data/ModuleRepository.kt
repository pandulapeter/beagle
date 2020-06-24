package com.pandulapeter.beagle.appDemo.data

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.HeaderModule

class ModuleRepository {

    private val listeners = mutableListOf<Listener>()
    private val _modules = mutableListOf(
        ModuleWrapper(
            titleResourceId = R.string.add_module_header,
            module = HeaderModule(
                title = "Header title",
                subtitle = "Header subtitle"
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_app_info,
            module = AppInfoButtonModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_force_crash_button,
            module = ForceCrashButtonModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_animation_duration_switch,
            module = AnimationDurationSwitchModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_device_info,
            module = DeviceInfoModule()
        )
    )
    val modules: List<ModuleWrapper> get() = _modules

    @Suppress("unused")
    fun registerListener(lifecycleOwner: LifecycleOwner, listener: Listener) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                if (!listeners.contains(listener)) {
                    listeners.add(listener)
                    listener.onModuleListChanged()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                listeners.remove(listener)
            }
        })
    }

    fun addModule(moduleWrapper: ModuleWrapper) {
        _modules.add(moduleWrapper)
        notifyListeners()
    }

    private fun notifyListeners() = listeners.forEach { it.onModuleListChanged() }

    interface Listener {
        fun onModuleListChanged()
    }
}