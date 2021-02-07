package com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.databinding.ItemMockDataGeneratorLoremIpsumBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class LoremIpsumViewHolder private constructor(
    binding: ItemMockDataGeneratorLoremIpsumBinding,
    onCardPressed: () -> Unit
) : BaseViewHolder<ItemMockDataGeneratorLoremIpsumBinding, LoremIpsumViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onCardPressed()
            }
        }
    }

    data class UiModel(
        val generatedText: String
    ) : MockDataGeneratorListItem {

        override val id = "loremIpsum"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onCardPressed: () -> Unit
        ) = LoremIpsumViewHolder(
            binding = ItemMockDataGeneratorLoremIpsumBinding.inflate(parent.inflater, parent, false),
            onCardPressed = onCardPressed
        )
    }
}