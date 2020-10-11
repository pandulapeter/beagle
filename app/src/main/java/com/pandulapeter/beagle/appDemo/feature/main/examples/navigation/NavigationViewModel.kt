package com.pandulapeter.beagle.appDemo.feature.main.examples.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class NavigationViewModel : ListViewModel<ListItem>() {

    var currentPage = Page.MAIN

    override val items: LiveData<List<ListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.case_study_navigation_text_1)
        )
    )

    enum class Page {
        MAIN,
        SECTION_1,
        SECTION_2,
        SECTION_3
    }
}