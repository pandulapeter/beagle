package com.pandulapeter.beagle.core.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.Module
import com.pandulapeter.beagle.core.list.ModuleAdapter

internal class ListManager {

    private val moduleAdapter = ModuleAdapter()
    private val modules = mutableListOf<Module>()

    fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.run {
        adapter = moduleAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
        refreshList()
    }

    fun addModule(module: Module) {
        modules.add(module)
        refreshList()
    }

    fun refreshList() = moduleAdapter.submitList(modules.flatMap { it.createCells() })
}