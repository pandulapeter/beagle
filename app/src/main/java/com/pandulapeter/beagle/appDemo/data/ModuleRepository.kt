package com.pandulapeter.beagle.appDemo.data

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.pandulapeter.beagle.appDemo.data.model.ModuleWrapper
import java.util.*

class ModuleRepository {

    private val listeners = mutableListOf<Listener>()
    private val _modules = mutableListOf(
        ModuleWrapper.HeaderWrapper,
        ModuleWrapper.AppInfoButtonWrapper,
        ModuleWrapper.DeveloperOptionsButtonWrapper,
        ModuleWrapper.TextSectionHeaderWrapper(),
        ModuleWrapper.KeylineOverlaySwitchWrapper,
        ModuleWrapper.AnimationDurationSwitchWrapper,
        ModuleWrapper.ScreenCaptureToolboxWrapper,
        ModuleWrapper.DividerWrapper(),
        ModuleWrapper.TextSectionHeaderWrapper(),
        ModuleWrapper.NetworkLogListWrapper,
        ModuleWrapper.LogListWrapper(),
        ModuleWrapper.LifecycleLogListWrapper,
        ModuleWrapper.DividerWrapper(),
        ModuleWrapper.TextSectionHeaderWrapper(),
        ModuleWrapper.DeviceInfoWrapper,
        ModuleWrapper.BugReportButtonWrapper,
        ModuleWrapper.ForceCrashButtonWrapper
    )
    val modules: List<ModuleWrapper> get() = _modules

    @Suppress("unused")
    fun registerListener(lifecycleOwner: LifecycleOwner, listener: Listener) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                if (!listeners.contains(listener)) {
                    listeners.add(listener)
                    listener.onModuleListChanged()
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                listeners.remove(listener)
            }
        })
    }

    fun addModule(moduleWrapper: ModuleWrapper) {
        _modules.add(moduleWrapper)
        notifyListeners()
    }

    fun removeModule(id: String) {
        _modules.removeAll { it.id == id }
        notifyListeners()
    }

    fun onModulesSwapped(oldPosition: Int, newPosition: Int) {
        if (oldPosition < newPosition) {
            for (i in oldPosition until newPosition) {
                Collections.swap(_modules, i, i + 1)
            }
        } else {
            for (i in oldPosition downTo newPosition + 1) {
                Collections.swap(_modules, i, i - 1)
            }
        }
        notifyListeners()
    }

    private fun notifyListeners() = listeners.forEach { it.onModuleListChanged() }

    interface Listener {
        fun onModuleListChanged()
    }
}