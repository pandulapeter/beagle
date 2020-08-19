package com.pandulapeter.beagle.appDemo.feature.main.examples.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.ItemExamplesCaseStudyBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class CaseStudyViewHolder private constructor(
    binding: ItemExamplesCaseStudyBinding,
    onItemSelected: (UiModel) -> Unit
) : BaseViewHolder<ItemExamplesCaseStudyBinding, CaseStudyViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.let(onItemSelected)
            }
        }
    }

    data class UiModel(
        val caseStudy: CaseStudy
    ) : ExamplesListItem {

        override val id = caseStudy.id
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemSelected: (UiModel) -> Unit
        ) = CaseStudyViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_examples_case_study, parent, false),
            onItemSelected = onItemSelected
        )
    }
}