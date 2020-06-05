package com.pandulapeter.beagle.core.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.CellAdapter
import com.pandulapeter.beagle.core.list.moduleDelegates.SwitchModuleDelegate
import com.pandulapeter.beagle.core.list.moduleDelegates.TextModuleDelegate
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

internal class ListManager {

    private val cellAdapter = CellAdapter()
    private val modules = mutableListOf<Module<*>>()
    private val moduleHandlers = mutableMapOf(
        TextModule::class to TextModuleDelegate(),
        SwitchModule::class to SwitchModuleDelegate()
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
    fun <M : Module<*>> findModuleById(id: String): M? = modules.firstOrNull { it.id == id } as? M?

    //TODO: Move to coroutine
    //TODO: Throw exception if no handler is found
    fun refreshList() = cellAdapter.submitList(modules.flatMap { module ->
        (moduleHandlers[module::class] ?: (module.createModuleDelegate().also {
            moduleHandlers[module::class] = it
        })).forceCreateCells(module)
    })
}