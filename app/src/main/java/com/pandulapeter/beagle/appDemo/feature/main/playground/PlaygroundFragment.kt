package com.pandulapeter.beagle.appDemo.feature.main.playground

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : ListFragment<PlaygroundViewModel, ListItem>(R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>() {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = "Work in progress")
    )

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}