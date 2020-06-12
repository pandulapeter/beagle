package com.pandulapeter.beagle.appDemo.feature.main.playground

import com.pandulapeter.beagle.appDemo.feature.main.ContainerFragment

class PlaygroundContainerFragment : ContainerFragment(PlaygroundFragment.Companion::newInstance) {

    companion object {
        fun newInstance() = PlaygroundContainerFragment()
    }
}