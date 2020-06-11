package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem

abstract class InspirationDetailViewModel<LI : ListItem> : ViewModel() {

    abstract val items: LiveData<List<LI>>
}