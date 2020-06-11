package com.pandulapeter.beagle.appDemo.feature.main.inspiration.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.ItemInspirationCaseStudyBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class CaseStudyViewHolder private constructor(
    binding: ItemInspirationCaseStudyBinding,
    onItemSelected: (position: Int) -> Unit
) : BaseViewHolder<ItemInspirationCaseStudyBinding, CaseStudyViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            adapterPosition.let { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemSelected(adapterPosition)
                }
            }
        }
    }

    data class UiModel(
        val caseStudy: CaseStudy
    ) : InspirationListItem {

        override val id = caseStudy.id
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (position: Int) -> Unit
        ) = CaseStudyViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_inspiration_case_study, parent, false),
            onItemSelected = onItemSelected
        )
    }
}