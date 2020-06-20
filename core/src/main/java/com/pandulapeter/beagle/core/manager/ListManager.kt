package com.pandulapeter.beagle.core.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.configuration.Positioning
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.CellAdapter
import com.pandulapeter.beagle.core.list.moduleDelegates.AnimationDurationSwitchDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.AppInfoButtonDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.ButtonDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.CheckBoxDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.DeviceInfoDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.DividerDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.ForceCrashButtonDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.HeaderDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.ItemListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.KeyValueListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.LabelDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.LongTextDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.MultipleSelectionListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.PaddingDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.SingleSelectionListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.SwitchDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.TextDelegate
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.KeyValueListModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class ListManager {

    private val cellAdapter = CellAdapter()
    private val modules = mutableListOf<Module<*>>()
    private val moduleDelegates = mutableMapOf(
        AnimationDurationSwitchModule::class to AnimationDurationSwitchDelegate(),
        AppInfoButtonModule::class to AppInfoButtonDelegate(),
        ButtonModule::class to ButtonDelegate(),
        CheckBoxModule::class to CheckBoxDelegate(),
        DeviceInfoModule::class to DeviceInfoDelegate(),
        DividerModule::class to DividerDelegate(),
        ForceCrashButtonModule::class to ForceCrashButtonDelegate(),
        HeaderModule::class to HeaderDelegate(),
        LabelModule::class to LabelDelegate(),
        LongTextModule::class to LongTextDelegate(),
        ItemListModule::class to ItemListDelegate<BeagleListItemContract>(),
        KeyValueListModule::class to KeyValueListDelegate(),
        MultipleSelectionListModule::class to MultipleSelectionListDelegate<BeagleListItemContract>(),
        PaddingModule::class to PaddingDelegate(),
        SingleSelectionListModule::class to SingleSelectionListDelegate<BeagleListItemContract>(),
        SwitchModule::class to SwitchDelegate(),
        TextModule::class to TextDelegate()
    )

    fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.run {
        adapter = cellAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
    }

    fun setModules(newModules: List<Module<*>>) {
        modules.clear()
        modules.addAll(newModules.distinctBy { it.id })
        refreshCells()
    }

    fun addModules(newModules: List<Module<*>>, positioning: Positioning, lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = addModules(newModules, positioning)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeModules(newModules.map { it.id })
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addModules(newModules, positioning)
    }

    fun removeModules(ids: List<String>) {
        modules.removeAll { ids.contains(it.id) }
        refreshCells()
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<*>> findModule(id: String): M? = modules.firstOrNull { it.id == id } as? M?

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<M>> findModuleDelegate(type: KClass<out M>) = moduleDelegates[type] as Module.Delegate<M>

    //TODO: Throw exception if no handler is found
    fun refreshCells() {
        GlobalScope.launch {
            cellAdapter.submitList(modules.flatMap { module ->
                (moduleDelegates[module::class] ?: (module.createModuleDelegate().also {
                    moduleDelegates[module::class] = it
                })).forceCreateCells(module)
            })
        }
    }

    private fun addModules(newModules: List<Module<*>>, positioning: Positioning) {
        modules.apply {
            var newIndex = 0
            newModules.forEach { module ->
                indexOfFirst { it.id == module.id }.also { currentIndex ->
                    if (currentIndex != -1) {
                        removeAt(currentIndex)
                        add(currentIndex, module)
                    } else {
                        when (positioning) {
                            Positioning.Bottom -> add(module)
                            Positioning.Top -> add(newIndex++, module)
                            is Positioning.Below -> {
                                indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                    if (referencePosition == -1) {
                                        add(module)
                                    } else {
                                        add(referencePosition + 1 + newIndex++, module)
                                    }
                                }
                            }
                            is Positioning.Above -> {
                                indexOfFirst { it.id == positioning.id }.also { referencePosition ->
                                    if (referencePosition == -1) {
                                        add(newIndex++, module)
                                    } else {
                                        add(referencePosition, module)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        refreshCells()
    }
}