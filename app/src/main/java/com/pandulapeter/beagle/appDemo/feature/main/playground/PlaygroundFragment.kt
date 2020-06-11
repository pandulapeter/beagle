package com.pandulapeter.beagle.appDemo.feature.main.playground

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentPlaygroundBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainChildFragment
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : MainChildFragment<FragmentPlaygroundBinding, PlaygroundViewModel>(R.layout.fragment_playground, R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()
    override val appBar get() = binding.appBar

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = "Work in progress")
    )

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}