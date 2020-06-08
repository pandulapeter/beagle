package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.data.CaseStudyRepository
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.CaseStudyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.IntroductionViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.HomeListItem

class InspirationViewModel(caseStudyRepository: CaseStudyRepository) : ViewModel() {

    private val _items = MutableLiveData<List<HomeListItem>>()
    val items: LiveData<List<HomeListItem>> = _items

    init {
        _items.value = listOf(IntroductionViewHolder.UiModel()) + caseStudyRepository.dataSet.map { caseStudy -> CaseStudyViewHolder.UiModel(caseStudy) }
    }
}