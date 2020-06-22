package com.pandulapeter.beagle.appDemo.feature.main.about.licences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class LicencesViewModel : ListViewModel<ListItem>() {

    override val items: LiveData<List<ListItem>> = MutableLiveData(
        listOf<ListItem>(
            TextViewHolder.UiModel(R.string.coming_soon)
        )
    )
}