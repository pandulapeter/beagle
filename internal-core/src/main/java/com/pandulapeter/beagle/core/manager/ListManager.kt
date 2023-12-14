package com.pandulapeter.beagle.core.manager

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Placement
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.contracts.module.ValueWrapperModule
import com.pandulapeter.beagle.core.list.CellAdapter
import com.pandulapeter.beagle.core.list.delegates.*
import com.pandulapeter.beagle.modules.*
import com.pandulapeter.beagle.utils.view.GestureBlockingRecyclerView
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.reflect.KClass

internal class ListManager {

    val hasPendingUpdates get() = persistableModules.any { it.hasPendingChanges(BeagleCore.implementation) }
    private var countDownJob: Job? = null
    private var shouldBlockGestures = true
    private val cellAdapter = CellAdapter()
    private val moduleManagerContext = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    private val modules = mutableListOf<Module<*>>(
        TextModule(
            id = HINT_MODULE_ID,
            text = SpannableString("Welcome to Beagle!\n\nUse Beagle.set() or Beagle.add() to add modules to the debug menu.").apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, 18, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(StyleSpan(Typeface.ITALIC), 24, 36, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(StyleSpan(Typeface.ITALIC), 40, 52, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
        )
    )
    private val moduleDelegates = mutableMapOf(
        AnimationDurationSwitchModule::class to AnimationDurationSwitchDelegate(),
        AppInfoButtonModule::class to AppInfoButtonDelegate(),
        BugReportButtonModule::class to BugReportButtonDelegate(),
        CheckBoxModule::class to CheckBoxDelegate(),
        DeveloperOptionsButtonModule::class to DeveloperOptionsButtonDelegate(),
        DeviceInfoModule::class to DeviceInfoDelegate(),
        DividerModule::class to DividerDelegate(),
        ForceCrashButtonModule::class to ForceCrashButtonDelegate(),
        GalleryButtonModule::class to GalleryButtonDelegate(),
        HeaderModule::class to HeaderDelegate(),
        LifecycleLogListModule::class to LifecycleLogListDelegate(),
        LoadingIndicatorModule::class to LoadingIndicatorDelegate(),
        LogListModule::class to LogListDelegate(),
        LongTextModule::class to LongTextDelegate(),
        LoremIpsumGeneratorButtonModule::class to LoremIpsumGeneratorButtonDelegate(),
        ItemListModule::class to ItemListDelegate<BeagleListItemContract>(),
        KeylineOverlaySwitchModule::class to KeylineOverlaySwitchDelegate(),
        KeyValueListModule::class to KeyValueListDelegate(),
        MultipleSelectionListModule::class to MultipleSelectionListDelegate<BeagleListItemContract>(),
        NetworkLogListModule::class to NetworkLogListDelegate(),
        PaddingModule::class to PaddingDelegate(),
        ScreenCaptureToolboxModule::class to ScreenCaptureToolboxDelegate(),
        ScreenRecordingButtonModule::class to ScreenRecordingButtonDelegate(),
        ScreenshotButtonModule::class to ScreenshotButtonDelegate(),
        SingleSelectionListModule::class to SingleSelectionListDelegate<BeagleListItemContract>(),
        SliderModule::class to SliderDelegate(),
        SwitchModule::class to SwitchDelegate(),
        TextModule::class to TextDelegate(),
        TextInputModule::class to TextInputDelegate()
    )

    private val persistableModules get() = synchronized(modules) { modules.filterIsInstance<ValueWrapperModule<*, *>>().toList() }

    fun setupRecyclerView(recyclerView: GestureBlockingRecyclerView) = recyclerView.run {
        adapter = cellAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
        shouldBlockGestures = { this@ListManager.shouldBlockGestures }
    }

    fun setModules(newModules: List<Module<*>>, onContentsChanged: () -> Unit) {
        GlobalScope.launch { setModulesInternal(newModules, onContentsChanged) }
    }

    fun addModules(newModules: List<Module<*>>, placement: Placement, lifecycleOwner: LifecycleOwner?, onContentsChanged: () -> Unit) {
        GlobalScope.launch(if (lifecycleOwner == null) Dispatchers.Default else Dispatchers.Main) {
            removeModulesInternal(listOf(HINT_MODULE_ID)) {}
            addModulesInternal(newModules, placement, lifecycleOwner, onContentsChanged)
        }
    }

    fun removeModules(ids: List<String>, onContentsChanged: () -> Unit = {}) {
        GlobalScope.launch { removeModulesInternal(ids, onContentsChanged) }
    }

    fun refreshCells(onContentsChanged: () -> Unit) {
        GlobalScope.launch { refreshCellsInternal(onContentsChanged) }
    }

    fun contains(id: String) = synchronized(modules) { modules.any { it.id == id } }

    fun applyPendingChanges() {
        persistableModules.forEach { it.applyPendingChanges(BeagleCore.implementation) }
        BeagleCore.implementation.refresh()
    }

    fun resetPendingChanges() {
        persistableModules.forEach { it.resetPendingChanges(BeagleCore.implementation) }
        BeagleCore.implementation.refresh()
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<*>> findModule(id: String): M? = synchronized(modules) { modules.firstOrNull { it.id == id } as? M? }

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<M>> findModuleDelegate(type: KClass<out M>) = synchronized(modules) { moduleDelegates[type] as? Module.Delegate<M>? }

    private suspend fun setModulesInternal(newModules: List<Module<*>>, onContentsChanged: () -> Unit) = withContext(moduleManagerContext) {
        synchronized(modules) {
            modules.clear()
            modules.addAll(newModules.distinctBy { it.id })
        }
        refreshCellsInternal(onContentsChanged)
    }

    private suspend fun addModulesInternal(newModules: List<Module<*>>, placement: Placement, lifecycleOwner: LifecycleOwner?, onContentsChanged: () -> Unit) =
        lifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                GlobalScope.launch { addModulesInternal(newModules, placement, onContentsChanged) }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                GlobalScope.launch { removeModules(newModules.map { it.id }, onContentsChanged) }
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }) ?: addModulesInternal(newModules, placement, onContentsChanged)

    private suspend fun addModulesInternal(newModules: List<Module<*>>, placement: Placement, onContentsChanged: () -> Unit) = withContext(moduleManagerContext) {
        synchronized(modules) {
            modules.apply {
                var newIndex = 0
                newModules.distinctBy { it.id }.forEach { module ->
                    indexOfFirst { it.id == module.id }.also { currentIndex ->
                        if (currentIndex != -1) {
                            removeAt(currentIndex)
                            add(currentIndex, module)
                        } else {
                            when (placement) {
                                Placement.Bottom -> add(module)
                                Placement.Top -> add(newIndex++, module)
                                is Placement.Below -> {
                                    indexOfFirst { it.id == placement.id }.also { referencePosition ->
                                        if (referencePosition == -1) {
                                            add(module)
                                        } else {
                                            add(referencePosition + 1 + newIndex++, module)
                                        }
                                    }
                                }
                                is Placement.Above -> {
                                    indexOfFirst { it.id == placement.id }.also { referencePosition ->
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
        }
        refreshCellsInternal(onContentsChanged)
    }

    private suspend fun removeModulesInternal(ids: List<String>, onContentsChanged: () -> Unit) = withContext(moduleManagerContext) {
        synchronized(modules) {
            modules.removeAll { ids.contains(it.id) }
        }
        refreshCellsInternal(onContentsChanged)
    }

    private suspend fun refreshCellsInternal(onContentsChanged: () -> Unit) = withContext(moduleManagerContext) {
        val newCells = synchronized(modules) {
            modules.flatMap { module ->
                (moduleDelegates[module::class] ?: (module.createModuleDelegate().also {
                    moduleDelegates[module::class] = it
                })).forceCreateCells(module)
            }
        }
        if (cellAdapter.itemCount > 0 && cellAdapter.itemCount != newCells.size) {
            shouldBlockGestures = true
        }
        cellAdapter.submitList(newCells) {
            onContentsChanged()
            countDownJob?.cancel()
            countDownJob = GlobalScope.launch {
                delay(300)
                shouldBlockGestures = false
            }
        }
    }

    companion object {
        private const val HINT_MODULE_ID = "beagleHintModule"
    }
}