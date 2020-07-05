package com.pandulapeter.beagle.appDemo.feature.main.examples.list

import android.view.View
import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class ExamplesAdapter(
    scope: CoroutineScope,
    private val onCaseStudyClicked: (CaseStudy, View) -> Unit
) : BaseAdapter<ExamplesListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CaseStudyViewHolder.UiModel -> R.layout.item_examples_case_study
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_examples_case_study -> CaseStudyViewHolder.create(parent) { uiModel, view ->
            onCaseStudyClicked(uiModel.caseStudy, view)
        }
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CaseStudyViewHolder -> holder.bind(getItem(position) as CaseStudyViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}