package com.pandulapeter.beagle.appDemo.feature.main.playground

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentPlaygroundBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : BaseViewModelFragment<FragmentPlaygroundBinding, PlaygroundViewModel>(R.layout.fragment_playground, true, R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}