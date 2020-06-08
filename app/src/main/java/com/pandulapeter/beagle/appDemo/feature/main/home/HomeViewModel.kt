package com.pandulapeter.beagle.appDemo.feature.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.data.CaseStudyRepository
import com.pandulapeter.beagle.appDemo.feature.main.home.list.CaseStudyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.home.list.HintViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.home.list.HomeListItem

class HomeViewModel(caseStudyRepository: CaseStudyRepository) : ViewModel() {

    private val _items = MutableLiveData<List<HomeListItem>>()
    val items: LiveData<List<HomeListItem>> = _items

    init {
        _items.value = listOf(HintViewHolder.UiModel()) + caseStudyRepository.dataSet.map { caseStudy -> CaseStudyViewHolder.UiModel(caseStudy) }
    }
}