package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class PlaygroundAdapter(
    scope: CoroutineScope,
    private val onGenerateCodeClicked: () -> Unit,
    private val onAddModuleClicked: () -> Unit
) : BaseAdapter<PlaygroundListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ModuleViewHolder.UiModel -> R.layout.item_playground_module
        is AddModuleViewHolder.UiModel -> R.layout.item_playground_add_module
        is GenerateCodeViewHolder.UiModel -> R.layout.item_playground_generate_code
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_playground_module -> ModuleViewHolder.create(parent)
        R.layout.item_playground_add_module -> AddModuleViewHolder.create(parent, onAddModuleClicked)
        R.layout.item_playground_generate_code -> GenerateCodeViewHolder.create(parent, onGenerateCodeClicked)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is ModuleViewHolder -> holder.bind(getItem(position) as ModuleViewHolder.UiModel)
        is AddModuleViewHolder -> holder.bind(getItem(position) as AddModuleViewHolder.UiModel)
        is GenerateCodeViewHolder -> holder.bind(getItem(position) as GenerateCodeViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}