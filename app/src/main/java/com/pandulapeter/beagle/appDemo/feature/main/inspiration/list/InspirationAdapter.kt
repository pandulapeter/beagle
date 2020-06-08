package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.utils.ListItem
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class InspirationAdapter(
    private val onCaseStudyClicked: (CaseStudy) -> Unit
) : ListAdapter<HomeListItem, ViewHolder<*, *>>(ListItem.DiffCallback()) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is IntroductionViewHolder.UiModel -> R.layout.item_inspiration_introduction
        is CaseStudyViewHolder.UiModel -> R.layout.item_inspiration_case_study
        is SectionViewHolder.UiModel -> R.layout.item_inspiration_section
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<*, *> = when (viewType) {
        R.layout.item_inspiration_introduction -> IntroductionViewHolder.create(parent)
        R.layout.item_inspiration_case_study -> CaseStudyViewHolder.create(parent) { position ->
            onCaseStudyClicked((getItem(position) as CaseStudyViewHolder.UiModel).caseStudy)
        }
        R.layout.item_inspiration_section -> SectionViewHolder.create(parent)
        else -> throw  IllegalArgumentException("Unsupported item view type: $viewType.")
    }

    override fun onBindViewHolder(holder: ViewHolder<*, *>, position: Int) = when (holder) {
        is IntroductionViewHolder -> holder.bind(getItem(position) as IntroductionViewHolder.UiModel)
        is CaseStudyViewHolder -> holder.bind(getItem(position) as CaseStudyViewHolder.UiModel)
        is SectionViewHolder -> holder.bind(getItem(position) as SectionViewHolder.UiModel)
        else -> throw  IllegalArgumentException("Unsupported item type at position $position.")
    }
}