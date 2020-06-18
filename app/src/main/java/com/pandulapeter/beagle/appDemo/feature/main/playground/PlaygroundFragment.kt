package com.pandulapeter.beagle.appDemo.feature.main.playground

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundAdapter
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : ListFragment<PlaygroundViewModel, PlaygroundListItem>(R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()

    override fun createAdapter() = PlaygroundAdapter(viewModel.viewModelScope)

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = "Work in progress")
    )

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}