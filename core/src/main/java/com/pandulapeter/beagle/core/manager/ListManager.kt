package com.pandulapeter.beagle.core.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.CellAdapter
import com.pandulapeter.beagle.core.list.moduleDelegates.AnimationDurationSwitchDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.AppInfoButtonDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.ButtonDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.CheckBoxDelegateRenameMe
import com.pandulapeter.beagle.core.list.moduleDelegates.ItemListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.LabelDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.SingleSelectionListDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.SwitchDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.TextDelegate
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModuleRenameMe
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import kotlin.reflect.KClass

internal class ListManager {

    private val cellAdapter = CellAdapter()
    private val modules = mutableListOf<Module<*>>()
    private val moduleDelegates = mutableMapOf(
        AnimationDurationSwitchModule::class to AnimationDurationSwitchDelegate(),
        AppInfoButtonModule::class to AppInfoButtonDelegate(),
        ButtonModule::class to ButtonDelegate(),
        CheckBoxModuleRenameMe::class to CheckBoxDelegateRenameMe(),
        ItemListModule::class to ItemListDelegate(),
        LabelModule::class to LabelDelegate(),
        SingleSelectionListModule::class to SingleSelectionListDelegate(),
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
        refreshList()
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<*>> findModule(id: String): M? = modules.firstOrNull { it.id == id } as? M?

    @Suppress("UNCHECKED_CAST")
    fun <M : Module<M>> findModuleDelegate(type: KClass<out M>) = moduleDelegates[type] as Module.Delegate<M>

    //TODO: Move to a coroutine (watch out for threading issues)
    //TODO: Throw exception if no handler is found
    fun refreshList() = cellAdapter.submitList(modules.flatMap { module ->
        (moduleDelegates[module::class] ?: (module.createModuleDelegate().also {
            moduleDelegates[module::class] = it
        })).forceCreateCells(module)
    })
}