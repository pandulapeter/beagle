package com.pandulapeter.beagle.appDemo.feature.main.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AboutViewModel : ListViewModel<ListItem>() {

    override val items: LiveData<List<ListItem>> = MutableLiveData(listOf(TextViewHolder.UiModel(R.string.about_description)))
}