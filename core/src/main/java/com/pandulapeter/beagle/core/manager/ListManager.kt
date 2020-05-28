package com.pandulapeter.beagle.core.manager

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.list.BeagleModuleAdapter
import com.pandulapeter.beagle.core.list.item.BeagleDisabledItem

internal class ListManager {

    private val moduleAdapter = BeagleModuleAdapter()

    fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.run {
        adapter = moduleAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
        refreshList()
    }

    fun refreshList() {
        moduleAdapter.submitList(
            if (BeagleCore.implementation.isUiEnabled) emptyList() else listOf(BeagleDisabledItem())
        )
    }
}