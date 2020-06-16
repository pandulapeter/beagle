package com.pandulapeter.beagle.appDemo.feature.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem

abstract class ListViewModel<LI : ListItem> : ViewModel() {

    abstract val items: LiveData<List<LI>>
}