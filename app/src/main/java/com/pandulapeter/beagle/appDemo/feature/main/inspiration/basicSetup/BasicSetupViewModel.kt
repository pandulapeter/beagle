package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class BasicSetupViewModel : InspirationDetailViewModel<ListItem>() {

    private val _items = MutableLiveData<List<ListItem>>()
    override val items: LiveData<List<ListItem>> = _items

    init {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.inspiration_beagle_text_1),
            TextViewHolder.UiModel(R.string.inspiration_beagle_text_1),
            TextViewHolder.UiModel(R.string.inspiration_beagle_text_1),
            TextViewHolder.UiModel(R.string.inspiration_beagle_text_1),
            TextViewHolder.UiModel(R.string.inspiration_beagle_text_1)
        )
    }
}