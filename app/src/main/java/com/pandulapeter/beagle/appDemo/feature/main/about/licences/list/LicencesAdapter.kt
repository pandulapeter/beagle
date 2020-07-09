package com.pandulapeter.beagle.appDemo.feature.main.about.licences.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class LicencesAdapter(
    scope: CoroutineScope,
    private val onLicenceSelected: (String) -> Unit
) : BaseAdapter<LicencesListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is DependencyViewHolder.UiModel -> R.layout.item_licences_dependency
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_licences_dependency -> DependencyViewHolder.create(parent, onLicenceSelected)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is DependencyViewHolder -> holder.bind(getItem(position) as DependencyViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}