package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import kotlinx.coroutines.CoroutineScope

class SetupAdapter(
    scope: CoroutineScope,
    onSectionHeaderSelected: (SectionHeaderViewHolder.UiModel) -> Unit,
    private val onGitHubButtonClicked: () -> Unit,
    private val onRadioButtonSelected: (RadioButtonViewHolder.UiModel) -> Unit
) : BaseAdapter<SetupListItem>(scope, onSectionHeaderSelected) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is GithubButtonViewHolder.UiModel -> R.layout.item_setup_github_button
        is RadioButtonViewHolder.UiModel -> R.layout.item_setup_radio_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_setup_github_button -> GithubButtonViewHolder.create(parent, onGitHubButtonClicked)
        R.layout.item_setup_radio_button -> RadioButtonViewHolder.create(parent, onRadioButtonSelected)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is GithubButtonViewHolder -> holder.bind(getItem(position) as GithubButtonViewHolder.UiModel)
        is RadioButtonViewHolder -> holder.bind(getItem(position) as RadioButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}