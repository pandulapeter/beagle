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
        refreshList()
    }

    fun setModules(newModules: List<Module>) {
        modules.clear()
        modules.addAll(newModules)
        refreshList()
    }

    fun refreshList() = cellAdapter.submitList(modules.flatMap { it.createCells() })
}