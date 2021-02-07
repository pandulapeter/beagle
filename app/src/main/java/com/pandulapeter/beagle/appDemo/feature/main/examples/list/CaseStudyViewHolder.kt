package com.pandulapeter.beagle.appDemo.feature.main.examples.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.ItemExamplesCaseStudyBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

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
            binding = ItemExamplesCaseStudyBinding.inflate(parent.inflater, parent, false),
            onItemSelected = onItemSelected
        )
    }
}