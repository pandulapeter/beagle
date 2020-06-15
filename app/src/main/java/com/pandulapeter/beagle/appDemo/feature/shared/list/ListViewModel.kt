package com.pandulapeter.beagle.appDemo.feature.shared.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class ListViewModel<LI : ListItem> : ViewModel() {

    abstract val items: LiveData<List<LI>>
}