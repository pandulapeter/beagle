package com.pandulapeter.beagle.appDemo.feature.main.about.licences.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.Dependency
import com.pandulapeter.beagle.appDemo.databinding.ItemLicencesDependencyBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class DependencyViewHolder private constructor(
    binding: ItemLicencesDependencyBinding,
    val onItemClicked: (String) -> Unit
) : BaseViewHolder<ItemLicencesDependencyBinding, DependencyViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                binding.uiModel?.dependency?.url?.let { onItemClicked(it) }
            }
        }
    }

    data class UiModel(
        val dependency: Dependency
    ) : LicencesListItem {

        override val id = dependency.title
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (String) -> Unit
        ) = DependencyViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_licences_dependency, parent, false),
            onItemClicked = onItemClicked
        )
    }
}