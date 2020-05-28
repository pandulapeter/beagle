package com.pandulapeter.beagle.core.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.BeagleModuleItem
import com.pandulapeter.beagle.core.R

internal class BeagleModuleAdapter : ListAdapter<BeagleModuleItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<BeagleModuleItem>() {

    override fun areItemsTheSame(oldItem: BeagleModuleItem, newItem: BeagleModuleItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BeagleModuleItem, newItem: BeagleModuleItem) = oldItem == newItem

    override fun getChangePayload(oldItem: BeagleModuleItem, newItem: BeagleModuleItem) = ""
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = TestViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    class TestViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false))
}