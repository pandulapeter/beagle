package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.View
import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class InspirationAdapter(
    private val onCaseStudyClicked: (CaseStudy, View) -> Unit,
    private val onGitHubButtonClicked: () -> Unit
) : BaseAdapter<InspirationListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is CaseStudyViewHolder.UiModel -> R.layout.item_inspiration_case_study
        is ButtonViewHolder.UiModel -> R.layout.item_inspiration_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_inspiration_case_study -> CaseStudyViewHolder.create(parent) { position, view ->
            onCaseStudyClicked((getItem(position) as CaseStudyViewHolder.UiModel).caseStudy, view)
        }
        R.layout.item_inspiration_button -> ButtonViewHolder.create(parent, onGitHubButtonClicked)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is CaseStudyViewHolder -> holder.bind(getItem(position) as CaseStudyViewHolder.UiModel)
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}