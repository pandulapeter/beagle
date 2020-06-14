package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class FeatureTogglesViewModel : InspirationDetailViewModel<ListItem>() {

    private val _items = MutableLiveData<List<ListItem>>()
    override val items: LiveData<List<ListItem>> = _items

    init {
        updateItems()
    }

    private fun updateItems() {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.case_study_feature_toggles_text_1)
        )
    }
}