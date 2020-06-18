package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class SetupAdapter(
    scope: CoroutineScope,
    private val onGitHubButtonClicked: () -> Unit,
    private val onRadioButtonSelected: (Int) -> Unit,
    private val onHeaderSelected: (Int) -> Unit
) : BaseAdapter<SetupListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is GithubButtonViewHolder.UiModel -> R.layout.item_setup_github_button
        is SpaceViewHolder.UiModel -> R.layout.item_setup_space
        is RadioButtonViewHolder.UiModel -> R.layout.item_setup_radio_button
        is HeaderViewHolder.UiModel -> R.layout.item_setup_header
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_setup_github_button -> GithubButtonViewHolder.create(parent, onGitHubButtonClicked)
        R.layout.item_setup_space -> SpaceViewHolder.create(parent)
        R.layout.item_setup_radio_button -> RadioButtonViewHolder.create(parent, onRadioButtonSelected)
        R.layout.item_setup_header -> HeaderViewHolder.create(parent, onHeaderSelected)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is GithubButtonViewHolder -> holder.bind(getItem(position) as GithubButtonViewHolder.UiModel)
        is SpaceViewHolder -> holder.bind(getItem(position) as SpaceViewHolder.UiModel)
        is RadioButtonViewHolder -> holder.bind(getItem(position) as RadioButtonViewHolder.UiModel)
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}