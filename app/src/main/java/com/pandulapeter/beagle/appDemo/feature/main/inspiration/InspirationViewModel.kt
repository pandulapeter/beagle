package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.CaseStudyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.HomeListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.IntroductionViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.SectionViewHolder

class InspirationViewModel : ViewModel() {

    private val _items = MutableLiveData<List<HomeListItem>>()
    val items: LiveData<List<HomeListItem>> = _items

    fun refreshItems(shouldShowTutorial: Boolean) {
        _items.value = mutableListOf<HomeListItem>().apply {
            add(IntroductionViewHolder.UiModel(shouldShowTutorial = shouldShowTutorial))
            CaseStudy.values().forEachIndexed { index, caseStudy ->
                add(CaseStudyViewHolder.UiModel(caseStudy))
                if (index == 0 && shouldShowTutorial) {
                    add(SectionViewHolder.UiModel())
                }
            }
        }
    }
}