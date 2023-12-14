package com.pandulapeter.beagle.appDemo.feature.main.about.licences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.Dependency
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.list.DependencyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.list.LicencesListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class LicencesViewModel : ListViewModel<LicencesListItem>() {

    override val items: LiveData<List<LicencesListItem>> = MutableLiveData(
        mutableListOf<LicencesListItem>().apply {
            add(TextViewHolder.UiModel(R.string.licences_description))
            Dependency.Type.entries.forEach { type ->
                add(TextViewHolder.UiModel(type.titleResourceId))
                addAll(Dependency.entries.filter { it.type == type }.sortedBy { it.title }.map { DependencyViewHolder.UiModel(it) })
            }
        }
    )
}