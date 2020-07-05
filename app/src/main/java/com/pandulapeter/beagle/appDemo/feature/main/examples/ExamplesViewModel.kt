package com.pandulapeter.beagle.appDemo.feature.main.examples

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.CaseStudyViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class ExamplesViewModel : ListViewModel<ExamplesListItem>() {

    override val items: LiveData<List<ExamplesListItem>> = MutableLiveData(mutableListOf<ExamplesListItem>().apply {
        add(TextViewHolder.UiModel(R.string.examples_text))
        addAll(CaseStudy.values().map { caseStudy -> CaseStudyViewHolder.UiModel(caseStudy) })
    })
}