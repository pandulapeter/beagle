package com.pandulapeter.beagle.appDemo.feature.main.playground

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentHomeBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment

class PlaygroundFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_playground, true, R.string.playground_title) {

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}