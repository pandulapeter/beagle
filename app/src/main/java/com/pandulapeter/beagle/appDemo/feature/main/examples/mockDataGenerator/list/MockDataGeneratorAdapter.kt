package com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class MockDataGeneratorAdapter(
    scope: CoroutineScope,
    private val onCardPressed: () -> Unit
) : BaseAdapter<MockDataGeneratorListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is LoremIpsumViewHolder.UiModel -> R.layout.item_mock_data_generator_lorem_ipsum
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_mock_data_generator_lorem_ipsum -> LoremIpsumViewHolder.create(parent, onCardPressed)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is LoremIpsumViewHolder -> holder.bind(getItem(position) as LoremIpsumViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}