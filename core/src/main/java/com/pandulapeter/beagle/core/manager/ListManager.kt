package com.pandulapeter.beagle.core.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.core.list.CellAdapter

internal class ListManager {

    private val cellAdapter = CellAdapter()
    private val modules = mutableListOf<Module>()

    fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.run {
        adapter = cellAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
    }

    fun setModules(newModules: List<Module>) {
        modules.clear()
        modules.addAll(newModules.distinctBy { it.id })
        refreshList()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Module> findModuleById(id: String): T? = modules.firstOrNull { it.id == id } as? T?

    //TODO: Move to coroutine
    fun refreshList() = cellAdapter.submitList(modules.flatMap { it.createCells() })
}