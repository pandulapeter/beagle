package com.pandulapeter.beagle.appDemo.feature.main.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.ItemHomeCaseStudyBinding
import com.pandulapeter.beagle.appDemo.utils.ViewHolder

class CaseStudyViewHolder private constructor(binding: ItemHomeCaseStudyBinding) : ViewHolder<ItemHomeCaseStudyBinding, CaseStudyViewHolder.UiModel>(binding) {

    data class UiModel(
        val caseStudy: CaseStudy
    ) : HomeListItem {

        override val id = caseStudy.id
    }

    companion object {
        fun create(parent: ViewGroup) = CaseStudyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_home_case_study, parent, false))
    }
}