package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.CaseStudyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class InspirationViewModel : ListViewModel<InspirationListItem>() {

    override val items: LiveData<List<InspirationListItem>> = MutableLiveData(mutableListOf<InspirationListItem>().apply {
        add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_5))
        CaseStudy.values().forEachIndexed { index, caseStudy ->
            add(CaseStudyViewHolder.UiModel(caseStudy))
            if (index == 0) {
                add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_6))
            }
        }
    })
}